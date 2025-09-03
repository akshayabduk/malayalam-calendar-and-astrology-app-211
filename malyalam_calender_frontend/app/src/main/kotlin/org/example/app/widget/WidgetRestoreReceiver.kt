package org.example.app.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager

/**
 * Restores widget states after device reboot or app update
 */
class WidgetRestoreReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val widgetIds = appWidgetManager.getAppWidgetIds(
                    intent.component
                )

                if (widgetIds.isNotEmpty()) {
                    WidgetStateManager.getInstance(context)
                        .restoreWidgets(widgetIds)
                }
            }
        }
    }
}
