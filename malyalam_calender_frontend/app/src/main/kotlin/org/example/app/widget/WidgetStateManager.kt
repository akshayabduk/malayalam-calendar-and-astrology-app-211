package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import org.example.app.analytics.AnalyticsManager

/**
 * Manages widget state persistence and restoration
 */
class WidgetStateManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val analytics = AnalyticsManager.getInstance(context)

    fun saveWidgetState(widgetId: Int, options: Bundle) {
        prefs.edit().apply {
            putBoolean("${KEY_SHOW_DATE}_$widgetId", options.getBoolean(KEY_SHOW_DATE, true))
            putBoolean("${KEY_SHOW_EVENTS}_$widgetId", options.getBoolean(KEY_SHOW_EVENTS, true))
            putInt("${KEY_THEME}_$widgetId", options.getInt(KEY_THEME, THEME_LIGHT))
        }.apply()

        analytics.logEvent("widget_state_saved", Bundle().apply {
            putInt("widget_id", widgetId)
        })
    }

    fun loadWidgetState(widgetId: Int): Bundle {
        return Bundle().apply {
            putBoolean(KEY_SHOW_DATE, prefs.getBoolean("${KEY_SHOW_DATE}_$widgetId", true))
            putBoolean(KEY_SHOW_EVENTS, prefs.getBoolean("${KEY_SHOW_EVENTS}_$widgetId", true))
            putInt(KEY_THEME, prefs.getInt("${KEY_THEME}_$widgetId", THEME_LIGHT))
        }
    }

    fun deleteWidgetState(widgetId: Int) {
        prefs.edit().apply {
            remove("${KEY_SHOW_DATE}_$widgetId")
            remove("${KEY_SHOW_EVENTS}_$widgetId")
            remove("${KEY_THEME}_$widgetId")
        }.apply()

        analytics.logEvent("widget_state_deleted", Bundle().apply {
            putInt("widget_id", widgetId)
        })
    }

    fun restoreWidgets(widgetIds: IntArray) {
        widgetIds.forEach { widgetId ->
            if (prefs.contains("${KEY_SHOW_DATE}_$widgetId")) {
                // Widget exists, trigger update
                val appWidgetManager = AppWidgetManager.getInstance(context)
                CalendarWidget().updateAppWidget(context, appWidgetManager, widgetId)

                analytics.logEvent("widget_restored", Bundle().apply {
                    putInt("widget_id", widgetId)
                })
            }
        }
    }

    companion object {
        const val PREFS_NAME = "widget_preferences"
        const val KEY_SHOW_DATE = "show_date"
        const val KEY_SHOW_EVENTS = "show_events"
        const val KEY_THEME = "theme"
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_SYSTEM = 2

        @Volatile
        private var instance: WidgetStateManager? = null

        fun getInstance(context: Context): WidgetStateManager {
            return instance ?: synchronized(this) {
                instance ?: WidgetStateManager(context).also { instance = it }
            }
        }
    }
}
