package org.example.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.app.data.db.AppDatabase
import org.example.app.data.repository.RoomCalendarRepository
import java.util.*

class EventReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val db = AppDatabase.getDatabase(context)
    private val repository = RoomCalendarRepository(db)
    private val notificationManager = NotificationManager(context)

    override suspend fun doWork(): Result {
        try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Get tomorrow's events
            val startOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis
            
            val endOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.timeInMillis

            val events = repository.getEvents(
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
            ).toList().firstOrNull() ?: emptyList()

            if (events.isNotEmpty()) {
                events.forEach { event ->
                    notificationManager.showEventNotification(event)
                }
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
