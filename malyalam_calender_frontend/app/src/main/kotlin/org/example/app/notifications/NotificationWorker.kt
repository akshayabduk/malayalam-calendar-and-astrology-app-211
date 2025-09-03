package org.example.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.data.db.AppDatabase
import org.example.app.utils.DateUtils.toTimestamp
import java.util.Date

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)
    private val notificationManager = LocalNotificationManager(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val now = Date()
            val events = database.calendarEventDao().getEventsForDate(now.toTimestamp())
            
            if (events.isNotEmpty()) {
                events.forEach { event ->
                    notificationManager.showEventNotification(
                        event.id,
                        event.title,
                        event.description ?: ""
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
