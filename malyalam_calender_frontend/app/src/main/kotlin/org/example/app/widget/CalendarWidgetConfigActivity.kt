package org.example.app.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import org.example.app.R

/**
 * Widget configuration activity
 */
class CalendarWidgetConfigActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var showDateCheck: CheckBox
    private lateinit var showEventsCheck: CheckBox
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.activity_widget_config)

        // Find the widget ID from the intent
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        // Initialize views
        showDateCheck = findViewById(R.id.showDateCheck)
        showEventsCheck = findViewById(R.id.showEventsCheck)
        saveButton = findViewById(R.id.saveButton)

        // Load saved preferences
        val prefs = getSharedPreferences(CalendarWidget.PREFS_NAME, MODE_PRIVATE)
        showDateCheck.isChecked = prefs.getBoolean(
            CalendarWidget.getShowDateKey(appWidgetId),
            true
        )
        showEventsCheck.isChecked = prefs.getBoolean(
            CalendarWidget.getShowEventsKey(appWidgetId),
            true
        )

        saveButton.setOnClickListener {
            saveConfiguration()
        }
    }

    private fun saveConfiguration() {
        // Save preferences
        CalendarWidget.saveWidgetSettings(
            this,
            appWidgetId,
            showDateCheck.isChecked,
            showEventsCheck.isChecked
        )

        // Update widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        CalendarWidget().onUpdate(
            this,
            appWidgetManager,
            intArrayOf(appWidgetId)
        )

        // Return result
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }
}
