package org.example.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.data.db.AppDatabase
import java.util.*
import kotlin.time.Duration.Companion.days

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getDatabase(context)
    private val notificationService = NotificationService(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Get events for next 24 hours
            val startTime = Calendar.getInstance().time
            val endTime = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
            }.time

            val events = database.calendarEventDao()
                .getEventsForPeriod(startTime, endTime)

            // Show notification for each event
            events.forEach { event ->
                notificationService.showEventNotification(event)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "event_notifications"
    }
}
