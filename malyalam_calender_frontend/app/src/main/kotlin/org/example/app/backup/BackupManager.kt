package org.example.app.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Manages backup and restore operations for app data
 */
class BackupManager(private val context: Context) {
    private val backupDir = File(context.filesDir, "backups")
    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)

    init {
        backupDir.mkdirs()
    }

    /**
     * Create a new backup of the app data
     * @return the filename of the created backup
     */
    suspend fun createBackup(): String = withContext(Dispatchers.IO) {
        try {
            val data = JSONObject().apply {
                put("timestamp", System.currentTimeMillis())
                put("version", getAppVersion())
                put("leaves", getLeaveData())
                put("settings", getSettings())
            }

            val filename = "backup_${dateFormat.format(Date())}.json"
            val backupFile = File(backupDir, filename)

            backupFile.writeText(data.toString(2))
            Log.i(TAG, "Backup created: $filename")
            filename
        } catch (e: Exception) {
            Log.e(TAG, "Backup creation failed", e)
            throw BackupException("Failed to create backup", e)
        }
    }

    /**
     * Restore app data from a backup file
     * @param uri URI of the backup file to restore from
     */
    suspend fun restoreFromUri(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader().readText()
            } ?: throw BackupException("Could not read backup file")

            val data = JSONObject(jsonString)
            validateBackupData(data)

            // Restore data
            restoreLeaveData(data.getJSONArray("leaves"))
            restoreSettings(data.getJSONObject("settings"))

            Log.i(TAG, "Backup restored from URI: $uri")
        } catch (e: Exception) {
            Log.e(TAG, "Backup restoration failed", e)
            throw BackupException("Failed to restore backup", e)
        }
    }

    /**
     * Get list of available backup files
     * @return List of backup file names sorted by date (newest first)
     */
    fun getBackupFiles(): List<String> {
        return backupDir.listFiles()
            ?.filter { it.name.startsWith("backup_") && it.name.endsWith(".json") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { it.name }
            ?: emptyList()
    }

    /**
     * Delete a backup file
     * @param filename Name of the backup file to delete
     */
    fun deleteBackup(filename: String) {
        val file = File(backupDir, filename)
        if (file.exists() && file.name.startsWith("backup_")) {
            file.delete()
            Log.i(TAG, "Backup deleted: $filename")
        }
    }

    private fun getAppVersion(): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    }

    private fun getLeaveData(): JSONArray {
        // TODO: Implement getting leave data from database
        return JSONArray()
    }

    private fun getSettings(): JSONObject {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return JSONObject().apply {
            put("theme", prefs.getString("theme", "system"))
            put("language", prefs.getString("language", "en"))
            put("notifications_enabled", prefs.getBoolean("notifications_enabled", true))
        }
    }

    private fun validateBackupData(data: JSONObject) {
        if (!data.has("version") || !data.has("timestamp") || 
            !data.has("leaves") || !data.has("settings")) {
            throw BackupException("Invalid backup format")
        }
    }

    private fun restoreLeaveData(leaves: JSONArray) {
        // TODO: Implement restoring leave data to database
    }

    private fun restoreSettings(settings: JSONObject) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("theme", settings.optString("theme", "system"))
            putString("language", settings.optString("language", "en"))
            putBoolean("notifications_enabled", settings.optBoolean("notifications_enabled", true))
            apply()
        }
    }

    companion object {
        private const val TAG = "BackupManager"
    }
}

class BackupException(message: String, cause: Throwable? = null) : Exception(message, cause)
