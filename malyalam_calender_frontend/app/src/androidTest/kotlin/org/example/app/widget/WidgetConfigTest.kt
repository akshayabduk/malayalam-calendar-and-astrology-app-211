package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WidgetConfigTest {
    private val widgetId = AppWidgetManager.INVALID_APPWIDGET_ID + 1

    @get:Rule
    val activityRule = ActivityScenarioRule<WidgetConfigActivity>(
        Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
    )

    @Before
    fun setup() {
        // Clear any existing preferences
        activityRule.scenario.onActivity { activity ->
            activity.getSharedPreferences(WidgetConfigActivity.PREFS_NAME, 0)
                .edit()
                .clear()
                .apply()
        }
    }

    @Test
    fun testDefaultSettings() {
        onView(withId(R.id.showDateSwitch))
            .check(matches(isChecked()))

        onView(withId(R.id.showEventsSwitch))
            .check(matches(isChecked()))
    }

    @Test
    fun testToggleSettings() {
        onView(withId(R.id.showDateSwitch))
            .perform(click())
            .check(matches(isNotChecked()))

        onView(withId(R.id.showEventsSwitch))
            .perform(click())
            .check(matches(isNotChecked()))
    }

    @Test
    fun testSaveSettings() {
        onView(withId(R.id.showDateSwitch))
            .perform(click())

        onView(withId(R.id.saveButton))
            .perform(click())

        // Verify activity finishes
        activityRule.scenario.onActivity { activity ->
            assert(activity.isFinishing)

            // Verify preferences were saved
            val prefs = activity.getSharedPreferences(
                WidgetConfigActivity.PREFS_NAME,
                0
            )
            assert(!prefs.getBoolean("show_date_$widgetId", true))
            assert(prefs.getBoolean("show_events_$widgetId", true))
        }
    }
}
