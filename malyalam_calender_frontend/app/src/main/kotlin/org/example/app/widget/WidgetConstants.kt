package org.example.app.widget

/**
 * Constants for widget functionality
 */
object WidgetConstants {
    const val ACTION_REFRESH = "org.example.app.widget.ACTION_REFRESH"
    const val PREFS_NAME = "org.example.app.widget.preferences"
    const val PREF_SHOW_DATE = "show_date"
    const val PREF_SHOW_EVENTS = "show_events"
    
    /**
     * Update widget using the AppWidgetManager
     */
    fun updateWidget(context: android.content.Context, appWidgetId: Int) {
        val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
        val views = android.widget.RemoteViews(context.packageName, org.example.app.R.layout.widget_calendar)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
