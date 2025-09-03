package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.example.app.R
import org.example.app.data.db.AppDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

/**
 * Widget provider for displaying calendar events on the home screen
 */
class CalendarWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            updateAppWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) = runBlocking {
        val database = AppDatabase.getDatabase(context)
        val views = RemoteViews(context.packageName, R.layout.widget_calendar_preview)

        // Update date
        val dateFormat = SimpleDateFormat(
            context.getString(R.string.month_format),
            Locale.getDefault()
        )
        views.setTextViewText(R.id.dateText, dateFormat.format(Date()))

        // Get today's events
        val calendar = Calendar.getInstance()
        val startOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val endOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time

        val events = database.calendarEventDao()
            .getEventsForPeriod(startOfDay, endOfDay)
            .first()

        // Update events text
        val eventsText = when {
            events.isEmpty() -> context.getString(R.string.no_leaves)
            else -> events.take(3).joinToString("\n") { event -> event.title }
        }
        views.setTextViewText(R.id.eventsText, eventsText)

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
