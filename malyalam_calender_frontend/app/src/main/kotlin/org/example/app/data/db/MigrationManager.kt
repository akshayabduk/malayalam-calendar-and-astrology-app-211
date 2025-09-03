package org.example.app.data.db

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.notifications.NotificationHelper

class MigrationManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val notificationHelper = NotificationHelper(context)

    suspend fun checkAndMigrate() = withContext(Dispatchers.IO) {
        try {
            val currentVersion = getCurrentVersion()
            val latestVersion = getLatestVersion()

            if (currentVersion < latestVersion) {
                notificationHelper.showMigrationProgress()
                performMigration(currentVersion, latestVersion)
                updateVersion(latestVersion)
                notificationHelper.showMigrationSuccess()
            }
        } catch (e: Exception) {
            notificationHelper.showMigrationError()
            throw e
        }
    }

    private fun getCurrentVersion(): Int {
        return prefs.getInt(KEY_DB_VERSION, 1)
    }

    private fun getLatestVersion(): Int {
        return 3 // Update this when adding new migrations
    }

    private suspend fun performMigration(fromVersion: Int, toVersion: Int) {
        val db = AppDatabase.getDatabase(context)

        // Apply migrations in sequence
        for (version in fromVersion until toVersion) {
            when (version) {
                1 -> migrateV1ToV2(db)
                2 -> migrateV2ToV3(db)
            }
        }
    }

    private suspend fun migrateV1ToV2(db: AppDatabase) {
        // Example: Update all existing events with current timestamp
        val currentTime = System.currentTimeMillis()
        db.eventDao().getEventsForPeriod(0, Long.MAX_VALUE).forEach { event ->
            db.eventDao().updateEvent(event.copy(lastModified = currentTime))
        }
    }

    private suspend fun migrateV2ToV3(db: AppDatabase) {
        // Example: Set sync status for existing records
        db.eventDao().getEventsForPeriod(0, Long.MAX_VALUE).forEach { event ->
            db.eventDao().updateEvent(event.copy(isSynced = false))
        }
    }

    private fun updateVersion(version: Int) {
        prefs.edit().putInt(KEY_DB_VERSION, version).apply()
    }

    companion object {
        private const val PREFS_NAME = "migration_prefs"
        private const val KEY_DB_VERSION = "db_version"
    }
}
