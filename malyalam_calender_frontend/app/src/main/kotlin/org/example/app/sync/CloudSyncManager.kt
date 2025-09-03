package org.example.app.sync

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.example.app.data.db.AppDatabase
import org.example.app.data.db.EventEntity
import org.example.app.data.db.LeaveEntity
import org.example.app.notifications.NotificationHelper
import java.util.*

class CloudSyncManager(private val context: Context) {
    private val db = FirebaseFirestore.getInstance()
    private val database = AppDatabase.getDatabase(context)
    private val notificationHelper = NotificationHelper(context)

    suspend fun syncData() = withContext(Dispatchers.IO) {
        try {
            // Sync events
            val localEvents = database.eventDao().getEventsForPeriod(0, Long.MAX_VALUE)
            val remoteEvents = db.collection("events")
                .whereGreaterThan("date", 0)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    doc.toObject(EventEntity::class.java)?.copy(id = doc.id)
                }

            // Merge events
            mergeEvents(localEvents, remoteEvents)

            // Sync leaves
            val localLeaves = database.leaveDao().getAllLeaves()
            val remoteLeaves = db.collection("leaves")
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    doc.toObject(LeaveEntity::class.java)?.copy(id = doc.id)
                }

            // Merge leaves
            mergeLeaves(localLeaves, remoteLeaves)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private val conflictResolver = SyncConflictResolver(context)
    
    private suspend fun mergeEvents(
        localEvents: List<EventEntity>,
        remoteEvents: List<EventEntity>
    ) {
        withContext(Dispatchers.Main) {
        val localById = localEvents.associateBy { it.id }
        val remoteById = remoteEvents.associateBy { it.id }

        // Find events to add, update, and delete
        val toAdd = remoteEvents.filter { !localById.containsKey(it.id) }
        val toUpdate = remoteEvents.filter { event ->
            localById[event.id]?.let { local ->
                local.lastModified < event.lastModified
            } ?: false
        }
        val toDelete = localEvents.filter { !remoteById.containsKey(it.id) }

        // Apply changes to local database
        database.eventDao().apply {
            toAdd.forEach { insertEvent(it) }
            toUpdate.forEach { updateEvent(it) }
            toDelete.forEach { deleteEvent(it) }
        }

        // Upload local changes to cloud
        val batch = db.batch()
        toAdd.forEach { event ->
            batch.set(db.collection("events").document(event.id), event)
        }
        toUpdate.forEach { event ->
            batch.update(db.collection("events").document(event.id), "lastModified", event.lastModified)
        }
        toDelete.forEach { event ->
            batch.delete(db.collection("events").document(event.id))
        }
        batch.commit().await()
    }

    private suspend fun mergeLeaves(
        localLeaves: List<LeaveEntity>,
        remoteLeaves: List<LeaveEntity>
    ) {
        val localById = localLeaves.associateBy { it.id }
        val remoteById = remoteLeaves.associateBy { it.id }

        // Find leaves to add, update, and delete
        val toAdd = remoteLeaves.filter { !localById.containsKey(it.id) }
        val toUpdate = remoteLeaves.filter { leave ->
            localById[leave.id]?.let { local ->
                local.lastModified < leave.lastModified
            } ?: false
        }
        val toDelete = localLeaves.filter { !remoteById.containsKey(it.id) }

        // Apply changes to local database
        database.leaveDao().apply {
            toAdd.forEach { insertLeave(it) }
            toUpdate.forEach { updateLeave(it) }
            toDelete.forEach { deleteLeave(it) }
        }

        // Upload local changes to cloud
        val batch = db.batch()
        toAdd.forEach { leave ->
            batch.set(db.collection("leaves").document(leave.id), leave)
        }
        toUpdate.forEach { leave ->
            batch.update(db.collection("leaves").document(leave.id), "lastModified", leave.lastModified)
        }
        toDelete.forEach { leave ->
            batch.delete(db.collection("leaves").document(leave.id))
        }
        batch.commit().await()
    }

    companion object {
        private const val TAG = "CloudSyncManager"
    }
}
