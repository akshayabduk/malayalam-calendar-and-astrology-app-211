package org.example.app.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testTabNavigation() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Verify calendar tab is selected by default
            onView(withText(R.string.tab_calendar))
                .check(matches(isSelected()))

            // Navigate to leaves tab
            onView(withText(R.string.tab_leaves))
                .perform(click())
                .check(matches(isSelected()))

            // Navigate to astrology tab
            onView(withText(R.string.tab_astrology))
                .perform(click())
                .check(matches(isSelected()))
        }
    }

    @Test
    fun testThemeToggle() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.action_toggle_theme))
                .perform(click())
        }
    }
}
