package org.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.example.app.analytics.AnalyticsManager
import org.example.app.notifications.NotificationPermissionHandler
import org.example.app.notifications.NotificationScheduler

class MainActivity : AppCompatActivity() {
    private val analyticsManager = AnalyticsManager(this)
    private val notificationPermissionHandler = NotificationPermissionHandler(this)
    private val notificationScheduler = NotificationScheduler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        checkNotificationPermission()
        scheduleNotifications()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            analyticsManager.logScreenView(destination.label.toString())
        }
    }

    private fun checkNotificationPermission() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("enable_notifications", true)) {
            notificationPermissionHandler.checkNotificationPermission(this)
        }
    }

    private fun scheduleNotifications() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("enable_notifications", true)) {
            notificationScheduler.scheduleNotifications()
        }
    }
}
