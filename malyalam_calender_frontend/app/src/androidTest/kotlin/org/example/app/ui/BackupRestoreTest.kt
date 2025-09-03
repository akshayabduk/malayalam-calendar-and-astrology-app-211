package org.example.app.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackupRestoreTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(BackupActivity::class.java)

    @Test
    fun testBackupCreation() {
        // Click backup button
        onView(withId(R.id.backupButton))
            .perform(click())

        // Verify success message
        onView(withText(R.string.backup_success))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyBackupList() {
        // Verify empty state message
        onView(withId(R.id.emptyView))
            .check(matches(withText(R.string.backup_list_empty)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testBackupDeletion() {
        // First create a backup
        onView(withId(R.id.backupButton))
            .perform(click())

        // Click delete on the backup item
        onView(withId(R.id.deleteButton))
            .perform(click())

        // Confirm deletion
        onView(withText(R.string.backup_delete))
            .perform(click())

        // Verify success message
        onView(withText(R.string.backup_delete_success))
            .check(matches(isDisplayed()))
    }
}
