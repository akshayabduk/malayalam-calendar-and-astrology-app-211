package org.example.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import org.example.app.R
import org.example.app.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Calendar widget implementation
 */
class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val showDate = prefs.getBoolean(getShowDateKey(appWidgetId), true)
        val showEvents = prefs.getBoolean(getShowEventsKey(appWidgetId), true)

        val views = RemoteViews(context.packageName, R.layout.widget_calendar)

        // Set click intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

        // Update date if enabled
        if (showDate) {
            val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
            views.setTextViewText(R.id.dateText, dateFormat.format(Date()))
            views.setViewVisibility(R.id.dateText, android.view.View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.dateText, android.view.View.GONE)
        }

        // Update events if enabled
        if (showEvents) {
            val intent = Intent(context, CalendarWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            views.setRemoteAdapter(R.id.eventsList, intent)
            views.setEmptyView(R.id.eventsList, R.id.emptyView)
            views.setViewVisibility(R.id.eventsList, android.view.View.VISIBLE)
            views.setViewVisibility(R.id.emptyView, android.view.View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.eventsList, android.view.View.GONE)
            views.setViewVisibility(R.id.emptyView, android.view.View.GONE)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)

        if (showEvents) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.eventsList)
        }
    }

    companion object {
        private const val PREFS_NAME = "calendar_widget"

        fun getShowDateKey(widgetId: Int) = "show_date_$widgetId"
        fun getShowEventsKey(widgetId: Int) = "show_events_$widgetId"

        fun saveWidgetSettings(
            context: Context,
            appWidgetId: Int,
            showDate: Boolean,
            showEvents: Boolean
        ) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(getShowDateKey(appWidgetId), showDate)
                .putBoolean(getShowEventsKey(appWidgetId), showEvents)
                .apply()
        }
    }
}
