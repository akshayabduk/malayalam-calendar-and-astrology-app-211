package org.example.app.notifications

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.example.app.analytics.AnalyticsManager

/**
 * Handles notification permission requests and checks
 */
class NotificationPermissionHandler(private val context: Context) {
    private val analytics = AnalyticsManager.getInstance(context)

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission()) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )

                analytics.logEvent("notification_permission_requested")
            }
        }
    }

    fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val granted = grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

            analytics.logEvent("notification_permission_result", Bundle().apply {
                putBoolean("granted", granted)
            })

            if (granted) {
                NotificationScheduler.getInstance(context).scheduleNotifications()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001

        @Volatile
        private var instance: NotificationPermissionHandler? = null

        fun getInstance(context: Context): NotificationPermissionHandler {
            return instance ?: synchronized(this) {
                instance ?: NotificationPermissionHandler(context).also { instance = it }
            }
        }
    }
}
