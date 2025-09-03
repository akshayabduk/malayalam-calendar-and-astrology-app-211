package org.example.app.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.example.app.R
import org.example.app.analytics.AnalyticsManager
import java.net.UnknownHostException
import java.sql.SQLException

/**
 * Utility class for handling errors consistently across the app
 */
object ErrorHandler {
    fun handleError(
        context: Context,
        view: View,
        error: Throwable,
        retryAction: (() -> Unit)? = null
    ) {
        val analytics = AnalyticsManager.getInstance(context)
        
        val message = when (error) {
            is UnknownHostException -> {
                analytics.logEvent("error_network")
                context.getString(R.string.error_no_connection)
            }
            is SQLException -> {
                analytics.logEvent("error_database")
                context.getString(R.string.error_database)
            }
            else -> {
                analytics.logEvent("error_generic", Bundle().apply {
                    putString("error_type", error.javaClass.simpleName)
                    putString("error_message", error.message)
                })
                context.getString(R.string.error_generic)
            }
        }

        showError(view, message, retryAction)
    }

    private fun showError(
        view: View,
        message: String,
        retryAction: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(
            view,
            message,
            if (retryAction != null) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        )

        retryAction?.let {
            snackbar.setAction(R.string.retry) { it() }
        }

        snackbar.show()
    }
}
