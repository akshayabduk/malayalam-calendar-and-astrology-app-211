package org.example.app.notifications

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    fun scheduleNotifications() {
        scheduleEventReminders()
        scheduleWidgetUpdates()
    }

    private fun scheduleEventReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val reminderWork = PeriodicWorkRequestBuilder<EventReminderWorker>(
            1, TimeUnit.DAYS
        )
        .setConstraints(constraints)
        .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "event_reminders",
                ExistingPeriodicWorkPolicy.KEEP,
                reminderWork
            )
    }

    private fun scheduleWidgetUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val widgetWork = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        )
        .setConstraints(constraints)
        .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "widget_updates",
                ExistingPeriodicWorkPolicy.KEEP,
                widgetWork
            )
    }
}
