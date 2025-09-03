package org.example.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.app.R
import org.example.app.leaves.LeaveManager
import org.example.app.ui.MainActivity
import java.util.*

/**
 * Worker service for event reminders
 */
class EventReminderWorkerService(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val leaveManager = LeaveManager.getInstance(context)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        createNotificationChannel()

        // Check for upcoming leaves
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Check tomorrow's leaves
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endTime = calendar.timeInMillis

        val upcomingLeaves = leaveManager.leaves.value?.filter {
            it.date.time in startTime until endTime
        } ?: emptyList()

        if (upcomingLeaves.isNotEmpty()) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Create notification for each leave
            upcomingLeaves.forEachIndexed { index, leave ->
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.upcoming_leave))
                    .setContentText(leave.title)
                    .setSmallIcon(R.drawable.ic_calendar_notification)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(index, notification)
            }
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_desc)
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "event_reminders"
    }
}
