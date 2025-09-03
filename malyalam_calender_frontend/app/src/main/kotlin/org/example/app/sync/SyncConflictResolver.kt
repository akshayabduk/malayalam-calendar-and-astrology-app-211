package org.example.app.sync

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.example.app.R
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.Leave
import java.util.*

class SyncConflictResolver(private val context: Context) {
    fun resolveEventConflict(
        localEvent: CalendarEvent,
        remoteEvent: CalendarEvent,
        onResolved: (CalendarEvent) -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.sync_conflict_title)
            .setMessage(R.string.sync_conflict_message)
            .setPositiveButton(R.string.sync_keep_local) { _, _ ->
                onResolved(localEvent.copy(lastModified = System.currentTimeMillis()))
            }
            .setNegativeButton(R.string.sync_keep_remote) { _, _ ->
                onResolved(remoteEvent)
            }
            .setNeutralButton(R.string.sync_merge) { _, _ ->
                onResolved(mergeEvents(localEvent, remoteEvent))
            }
            .show()
    }

    fun resolveLeaveConflict(
        localLeave: Leave,
        remoteLeave: Leave,
        onResolved: (Leave) -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.sync_conflict_title)
            .setMessage(R.string.sync_conflict_message)
            .setPositiveButton(R.string.sync_keep_local) { _, _ ->
                onResolved(localLeave.copy(lastModified = System.currentTimeMillis()))
            }
            .setNegativeButton(R.string.sync_keep_remote) { _, _ ->
                onResolved(remoteLeave)
            }
            .setNeutralButton(R.string.sync_merge) { _, _ ->
                onResolved(mergeLeaves(localLeave, remoteLeave))
            }
            .show()
    }

    private fun mergeEvents(local: CalendarEvent, remote: CalendarEvent): CalendarEvent {
        return local.copy(
            title = if (local.title.length > remote.title.length) local.title else remote.title,
            description = local.description ?: remote.description,
            lastModified = System.currentTimeMillis()
        )
    }

    private fun mergeLeaves(local: Leave, remote: Leave): Leave {
        return local.copy(
            title = if (local.title.length > remote.title.length) local.title else remote.title,
            description = local.description ?: remote.description,
            lastModified = System.currentTimeMillis()
        )
    }
}
