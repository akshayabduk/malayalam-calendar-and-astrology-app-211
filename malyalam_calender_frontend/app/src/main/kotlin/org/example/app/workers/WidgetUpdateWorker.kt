package org.example.app.workers

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.app.widget.CalendarWidget
import org.example.app.widget.AstrologyWidget

class WidgetUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)

            // Update calendar widgets
            val calendarWidgetProvider = ComponentName(context, CalendarWidget::class.java)
            val calendarWidgetIds = appWidgetManager.getAppWidgetIds(calendarWidgetProvider)
            calendarWidgetIds.forEach { widgetId ->
                CalendarWidget.updateWidget(context, appWidgetManager, widgetId)
            }

            // Update astrology widgets
            val astrologyWidgetProvider = ComponentName(context, AstrologyWidget::class.java)
            val astrologyWidgetIds = appWidgetManager.getAppWidgetIds(astrologyWidgetProvider)
            astrologyWidgetIds.forEach { widgetId ->
                AstrologyWidget.updateWidget(context, appWidgetManager, widgetId)
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
