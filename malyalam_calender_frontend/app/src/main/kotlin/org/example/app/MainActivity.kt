package org.example.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.example.app.analytics.Analytics
import org.example.app.ui.adapters.MainTabsAdapter
import org.example.app.ui.theme.ThemeManager
import com.google.android.material.appbar.MaterialToolbar
import org.example.app.ui.dialogs.AddLeaveDialog
import org.example.app.ui.onboarding.OnboardingActivity
import org.example.app.notifications.EventReminderWorker

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        if (OnboardingActivity.shouldShow(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        ThemeManager.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup toolbar with theme toggle
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_toggle_theme -> {
                    ThemeManager.setTheme(this, 
                        if (ThemeManager.getCurrentTheme(this) == ThemeManager.Theme.LIGHT) 
                        ThemeManager.Theme.DARK else ThemeManager.Theme.LIGHT
                    )
                    recreate()
                    true
                }
                else -> false
            }
        }

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = MainTabsAdapter(this)

        val titles = listOf(
            getString(R.string.tab_calendar),
            getString(R.string.tab_leaves),
            getString(R.string.tab_astrology)
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        // Handle navigation intents
        handleNavigationIntent(intent)

        requestNotificationPermissionIfNeeded()
        EventReminderWorker.schedule(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleNavigationIntent(it) }
    }

    private fun handleNavigationIntent(intent: Intent) {
        when (intent.action) {
            "org.example.app.action.ADD_LEAVE" -> {
                viewPager.setCurrentItem(1, false) // Leaves tab
                Analytics.logEvent("shortcut_add_leave")
                AddLeaveDialog().show(supportFragmentManager, "addLeave")
            }
            "org.example.app.action.VIEW_ASTROLOGY" -> {
                viewPager.setCurrentItem(2, false) // Astrology tab
                Analytics.logEvent("shortcut_view_astrology")
            }
            else -> {
                // Handle widget clicks
                intent.getIntExtra("openTab", -1).let { tabIndex ->
                    if (tabIndex >= 0 && tabIndex < 3) {
                        viewPager.setCurrentItem(tabIndex, false)
                    }
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            
            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}
