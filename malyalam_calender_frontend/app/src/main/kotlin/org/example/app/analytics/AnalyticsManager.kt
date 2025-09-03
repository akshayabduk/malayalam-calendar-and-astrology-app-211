package org.example.app.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log

/**
 * Manages analytics tracking throughout the app
 */
class AnalyticsManager private constructor(context: Context) {

    companion object {
        private const val TAG = "AnalyticsManager"

        @Volatile
        private var instance: AnalyticsManager? = null

        fun getInstance(context: Context): AnalyticsManager {
            return instance ?: synchronized(this) {
                instance ?: AnalyticsManager(context.applicationContext).also {
                    instance = it
                }
            }
        }

        // Event Names
        const val EVENT_VIEW_SCREEN = "view_screen"
        const val EVENT_MARK_LEAVE = "mark_leave"
        const val EVENT_MARK_HOLIDAY = "mark_holiday"
        const val EVENT_VIEW_ASTROLOGY = "view_astrology"
        const val EVENT_BACKUP = "backup"
        const val EVENT_RESTORE = "restore"
        const val EVENT_THEME_CHANGE = "theme_change"
        const val EVENT_LANGUAGE_CHANGE = "language_change"

        // Parameter Names
        const val PARAM_SCREEN_NAME = "screen_name"
        const val PARAM_LEAVE_TYPE = "leave_type"
        const val PARAM_THEME = "theme"
        const val PARAM_LANGUAGE = "language"
        const val PARAM_STATUS = "status"
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        // In a real app, this would send events to Firebase Analytics or similar
        Log.d(TAG, "Event: $eventName, Params: $params")
    }

    fun logScreenView(screenName: String) {
        val params = Bundle().apply {
            putString(PARAM_SCREEN_NAME, screenName)
        }
        logEvent(EVENT_VIEW_SCREEN, params)
    }

    fun logLeaveMarked(leaveType: String) {
        val params = Bundle().apply {
            putString(PARAM_LEAVE_TYPE, leaveType)
        }
        logEvent(EVENT_MARK_LEAVE, params)
    }

    fun logThemeChange(theme: String) {
        val params = Bundle().apply {
            putString(PARAM_THEME, theme)
        }
        logEvent(EVENT_THEME_CHANGE, params)
    }

    fun logLanguageChange(language: String) {
        val params = Bundle().apply {
            putString(PARAM_LANGUAGE, language)
        }
        logEvent(EVENT_LANGUAGE_CHANGE, params)
    }

    fun logBackup(success: Boolean) {
        val params = Bundle().apply {
            putString(PARAM_STATUS, if (success) "success" else "failure")
        }
        logEvent(EVENT_BACKUP, params)
    }

    fun logRestore(success: Boolean) {
        val params = Bundle().apply {
            putString(PARAM_STATUS, if (success) "success" else "failure")
        }
        logEvent(EVENT_RESTORE, params)
    }
}
