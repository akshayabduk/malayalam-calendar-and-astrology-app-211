package org.example.app.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.example.app.analytics.AnalyticsManager
import org.example.app.ui.MainActivity
import org.example.app.data.models.CalendarEvent

/**
 * Handles click events for the calendar widget
 */
class WidgetClickHandler(private val context: Context) {
    private val analytics = AnalyticsManager.getInstance(context)

    fun getOpenAppIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        analytics.logEvent("widget_click_open_app")

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun getEventClickIntent(event: CalendarEvent): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = ACTION_VIEW_EVENT
            putExtra(EXTRA_EVENT_ID, event.id)
        }

        analytics.logEvent("widget_click_event", Bundle().apply {
            putString("event_type", event.type.name)
        })

        return PendingIntent.getActivity(
            context,
            event.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun getRefreshIntent(widgetId: Int): PendingIntent {
        val intent = Intent(context, CalendarWidget::class.java).apply {
            action = CalendarWidget.ACTION_REFRESH
            putExtra(EXTRA_WIDGET_ID, widgetId)
        }

        analytics.logEvent("widget_click_refresh")

        return PendingIntent.getBroadcast(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_VIEW_EVENT = "org.example.app.ACTION_VIEW_EVENT"
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_WIDGET_ID = "widget_id"
    }
}
