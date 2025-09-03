package org.example.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.MalayalamCalendarApp
import org.example.app.R
import java.util.*
import java.util.concurrent.TimeUnit

class EventReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val currentDate = Calendar.getInstance()
            val repository = (applicationContext as MalayalamCalendarApp).repository
            
            val events = repository.getEvents(
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.YEAR)
            )

            // Check for upcoming events (next day)
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
            val upcomingEvents = events.filter { event ->
                val eventCal = Calendar.getInstance().apply { time = event.date }
                eventCal.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)
            }

            if (upcomingEvents.isNotEmpty()) {
                showNotification(upcomingEvents.size)
            }

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private fun showNotification(eventCount: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_desc)
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("You have $eventCount upcoming events tomorrow")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }

    companion object {
        private const val CHANNEL_ID = "event_reminders"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val request = PeriodicWorkRequestBuilder<EventReminderWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "event_reminders",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }
}
