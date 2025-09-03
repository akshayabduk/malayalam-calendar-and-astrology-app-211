package org.example.app.sync

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.app.analytics.AnalyticsManager
import org.example.app.data.db.AppDatabase
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.Leave
import org.example.app.utils.ErrorHandler
import java.util.*

class DataSyncService(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val analytics = AnalyticsManager.getInstance(context)
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun sync(): Flow<SyncResult> = flow {
        try {
            emit(SyncResult.Started)
            analytics.logEvent("sync_started")

            val lastSyncTime = getLastSyncTime()
            val changes = getLocalChanges(lastSyncTime)
            val remoteChanges = getRemoteChanges(lastSyncTime)

            val conflicts = findConflicts(changes, remoteChanges)
            if (conflicts.isNotEmpty()) {
                emit(SyncResult.Conflict(conflicts))
                analytics.logEvent("sync_conflict_detected")
                return@flow
            }

            // Apply remote changes
            applyRemoteChanges(remoteChanges)

            // Update last sync time
            updateLastSyncTime()

            emit(SyncResult.Success)
            analytics.logEvent("sync_completed")
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            analytics.logEvent("sync_failed", Bundle().apply {
                putString("error", e.message)
            })
            emit(SyncResult.Error(e))
        }
    }

    private fun getLocalChanges(since: Date): List<SyncableItem> {
        val events = database.calendarEventDao().getEventsModifiedAfter(since)
        val leaves = database.leaveDao().getLeavesModifiedAfter(since)
        return events.map { SyncableItem.fromEvent(it) } + 
               leaves.map { SyncableItem.fromLeave(it) }
    }

    private suspend fun getRemoteChanges(since: Date): List<SyncableItem> {
        // In real app, would fetch from remote server
        return emptyList()
    }

    private fun findConflicts(
        local: List<SyncableItem>,
        remote: List<SyncableItem>
    ): List<SyncConflict> {
        return local.mapNotNull { localItem ->
            remote.find { it.id == localItem.id }?.let { remoteItem ->
                if (localItem.lastModified != remoteItem.lastModified) {
                    SyncConflict(localItem, remoteItem)
                } else null
            }
        }
    }

    private suspend fun applyRemoteChanges(changes: List<SyncableItem>) {
        changes.forEach { item ->
            when (item) {
                is SyncableItem.Event -> {
                    database.calendarEventDao().insertEvent(item.toEntity())
                }
                is SyncableItem.Leave -> {
                    database.leaveDao().insertLeave(item.toEntity())
                }
            }
        }
    }

    private fun getLastSyncTime(): Date {
        val timestamp = prefs.getLong(KEY_LAST_SYNC, 0)
        return Date(timestamp)
    }

    private fun updateLastSyncTime() {
        prefs.edit()
            .putLong(KEY_LAST_SYNC, System.currentTimeMillis())
            .apply()
    }

    companion object {
        private const val TAG = "DataSyncService"
        private const val PREFS_NAME = "sync_prefs"
        private const val KEY_LAST_SYNC = "last_sync_time"
    }
}

sealed class SyncResult {
    object Started : SyncResult()
    object Success : SyncResult()
    data class Error(val error: Throwable) : SyncResult()
    data class Conflict(val conflicts: List<SyncConflict>) : SyncResult()
}

sealed class SyncableItem {
    abstract val id: String
    abstract val lastModified: Date

    data class Event(
        override val id: String,
        val event: CalendarEvent,
        override val lastModified: Date
    ) : SyncableItem()

    data class Leave(
        override val id: String,
        val leave: org.example.app.data.models.Leave,
        override val lastModified: Date
    ) : SyncableItem()

    companion object {
        fun fromEvent(event: CalendarEventEntity) = Event(
            id = event.id,
            event = event.toModel(),
            lastModified = event.lastModified
        )

        fun fromLeave(leave: LeaveEntity) = Leave(
            id = leave.id,
            leave = leave.toModel(),
            lastModified = leave.lastModified
        )
    }
}

data class SyncConflict(
    val localItem: SyncableItem,
    val remoteItem: SyncableItem
)
