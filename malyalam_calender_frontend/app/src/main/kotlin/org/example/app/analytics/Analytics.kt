package org.example.app.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.util.Date

object Analytics {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = Firebase.analytics
    }

    fun logCalendarView(date: Date) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "calendar")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "CalendarFragment")
            param("date", date.time)
        }
    }

    fun logLeaveAdded(type: String) {
        firebaseAnalytics.logEvent("leave_added") {
            param("leave_type", type)
        }
    }

    fun logAstrologyView(date: Date) {
        firebaseAnalytics.logEvent("astrology_view") {
            param("date", date.time)
        }
    }

    fun logBackupCreated() {
        firebaseAnalytics.logEvent("backup_created", null)
    }

    fun logBackupRestored() {
        firebaseAnalytics.logEvent("backup_restored", null)
    }

    fun logError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
    }
}
