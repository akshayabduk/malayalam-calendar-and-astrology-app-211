package org.example.app.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import org.example.app.R
import org.example.app.analytics.AnalyticsManager

class WidgetConfigActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var analytics: AnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        analytics = AnalyticsManager.getInstance(this)
        analytics.logScreenView("widget_config")

        // Get widget ID from intent
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setupViews()
    }

    private fun setupViews() {
        val showDateSwitch = findViewById<SwitchMaterial>(R.id.showDateSwitch)
        val showEventsSwitch = findViewById<SwitchMaterial>(R.id.showEventsSwitch)

        showDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveWidgetSetting("show_date_$appWidgetId", isChecked)
        }

        showEventsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveWidgetSetting("show_events_$appWidgetId", isChecked)
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)
            .setOnClickListener {
                updateWidget()
                analytics.logEvent("widget_configured", Bundle().apply {
                    putBoolean("show_date", showDateSwitch.isChecked)
                    putBoolean("show_events", showEventsSwitch.isChecked)
                })
                
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                })
                finish()
            }
    }

    private fun saveWidgetSetting(key: String, value: Boolean) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        CalendarWidget().updateAppWidget(this, appWidgetManager, appWidgetId)
    }

    companion object {
        const val PREFS_NAME = "org.example.app.widget.WidgetPreferences"
    }
}
