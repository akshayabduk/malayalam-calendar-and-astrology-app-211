package org.example.app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.example.app.BuildConfig
import org.example.app.R
import org.example.app.notifications.NotificationScheduler
import org.example.app.ui.backup.BackupActivity
import org.example.app.ui.theme.ThemeManager
import java.util.*

/**
 * Fragment to display app settings
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Theme preference
        findPreference<ListPreference>("theme")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                ThemeManager.applyTheme(requireContext(), newValue as String)
                true
            }
        }

        // Language preference
        findPreference<ListPreference>("language")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                updateLocale(newValue as String)
                true
            }
        }

        // Notification preferences
        findPreference<SwitchPreference>("enable_notifications")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    NotificationScheduler.scheduleReminders(requireContext())
                } else {
                    NotificationScheduler.cancelReminders(requireContext())
                }
                true
            }
        }

        // Backup & Restore
        findPreference<Preference>("backup_restore")?.apply {
            setOnPreferenceClickListener {
                startActivity(Intent(requireContext(), BackupActivity::class.java))
                true
            }
        }

        // Version
        findPreference<Preference>("version")?.apply {
            summary = BuildConfig.VERSION_NAME
        }
    }

    private fun updateLocale(languageCode: String) {
        val locale = when (languageCode) {
            "ml" -> Locale("ml")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Recreate activity to apply changes
        activity?.recreate()
    }
}
