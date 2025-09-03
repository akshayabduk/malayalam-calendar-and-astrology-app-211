package org.example.app.sync

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.app.data.db.AppDatabase
import java.util.*

/**
 * Manages data synchronization between local database and remote storage
 */
class SyncManager(private val context: Context) {

    private val database = AppDatabase.getDatabase(context)

    fun sync(): Flow<SyncStatus> = flow {
        try {
            emit(SyncStatus.Started)
            
            // Here we would normally sync with a remote server
            // For now, we'll just handle local data
            val lastSyncTime = getLastSyncTime()
            val currentTime = Date()

            // Get all changes since last sync
            val events = database.calendarEventDao().getEventsModifiedAfter(lastSyncTime)
            val leaves = database.leaveDao().getLeavesModifiedAfter(lastSyncTime)
            val astrology = database.astrologyDao().getAstrologyModifiedAfter(lastSyncTime)

            // Save last sync time
            saveLastSyncTime(currentTime)
            
            emit(SyncStatus.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            emit(SyncStatus.Error(e.message ?: "Unknown error"))
        }
    }

    private fun getLastSyncTime(): Date {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_LAST_SYNC, 0)
            .let { Date(it) }
    }

    private fun saveLastSyncTime(time: Date) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong(KEY_LAST_SYNC, time.time)
            .apply()
    }

    companion object {
        private const val TAG = "SyncManager"
        private const val PREFS_NAME = "sync_prefs"
        private const val KEY_LAST_SYNC = "last_sync_time"
    }
}

sealed class SyncStatus {
    object Started : SyncStatus()
    object Success : SyncStatus()
    data class Error(val message: String) : SyncStatus()
}
