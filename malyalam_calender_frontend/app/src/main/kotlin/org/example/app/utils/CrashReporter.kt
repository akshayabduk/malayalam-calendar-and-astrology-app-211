package org.example.app.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.example.app.R
import org.example.app.analytics.AnalyticsManager
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Handles app crash reporting and user feedback
 */
class CrashReporter private constructor(context: Context) {
    private val analytics = AnalyticsManager.getInstance(context)
    private val appContext = context.applicationContext

    fun handleException(activity: Activity, throwable: Throwable) {
        val stackTrace = getStackTrace(throwable)
        
        // Log crash immediately
        analytics.logEvent("app_crash", Bundle().apply {
            putString("stack_trace", stackTrace)
            putString("cause", throwable.cause?.toString())
        })

        // Show dialog to user
        showCrashDialog(activity, stackTrace)
    }

    private fun getStackTrace(throwable: Throwable): String {
        return StringWriter().apply {
            throwable.printStackTrace(PrintWriter(this))
        }.toString()
    }

    private fun showCrashDialog(activity: Activity, stackTrace: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.crash_report_dialog_title)
            .setMessage(R.string.crash_report_dialog_message)
            .setPositiveButton(R.string.send_report) { _, _ ->
                sendCrashReport(stackTrace)
            }
            .setNegativeButton(R.string.dont_send, null)
            .show()
    }

    private fun sendCrashReport(stackTrace: String) {
        // In real app, would send to crash reporting service
        analytics.logEvent("crash_report_sent", Bundle().apply {
            putString("stack_trace", stackTrace)
        })
    }

    companion object {
        @Volatile
        private var instance: CrashReporter? = null

        fun getInstance(context: Context): CrashReporter {
            return instance ?: synchronized(this) {
                instance ?: CrashReporter(context).also { instance = it }
            }
        }
    }
}
