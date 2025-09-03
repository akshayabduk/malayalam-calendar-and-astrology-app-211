package org.example.app.widget

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Update all widgets
            val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(applicationContext)
            
            // Update Calendar Widgets
            val calendarWidgets = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(applicationContext, CalendarWidgetProvider::class.java)
            )
            calendarWidgets.forEach { widgetId ->
                CalendarWidgetProvider.updateAppWidget(applicationContext, appWidgetManager, widgetId)
            }

            // Update Astrology Widgets
            val astrologyWidgets = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(applicationContext, AstrologyWidgetProvider::class.java)
            )
            astrologyWidgets.forEach { widgetId ->
                AstrologyWidgetProvider.updateAppWidget(applicationContext, appWidgetManager, widgetId)
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

    companion object {
        private const val WORK_NAME = "widget_update_worker"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val request = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
                30, TimeUnit.MINUTES,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }
}
