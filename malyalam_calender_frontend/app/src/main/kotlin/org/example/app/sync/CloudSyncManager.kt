package org.example.app.sync

import org.example.app.data.db.*
import org.example.app.data.models.*
import org.example.app.data.repository.CalendarRepository
import java.util.*

class CloudSyncManager(private val repository: CalendarRepository) {
    
    private fun CalendarEvent.toRemoteEntity(): Map<String, Any> = mapOf(
        "id" to id,
        "date" to date.time,
        "title" to title,
        "type" to type.name,
        "description" to (description ?: ""),
        "lastModified" to System.currentTimeMillis()
    )

    private fun Leave.toRemoteEntity(): Map<String, Any> = mapOf(
        "id" to id,
        "date" to date.time,
        "title" to title,
        "type" to type.name,
        "description" to (description ?: ""),
        "lastModified" to System.currentTimeMillis()
    )

    private fun Map<String, Any>.toCalendarEvent() = CalendarEvent(
        id = this["id"] as String,
        date = Date(this["date"] as Long),
        title = this["title"] as String,
        type = EventType.valueOf(this["type"] as String),
        description = this["description"] as String?
    )

    private fun Map<String, Any>.toLeave() = Leave(
        id = this["id"] as String,
        date = Date(this["date"] as Long),
        title = this["title"] as String,
        type = LeaveType.valueOf(this["type"] as String),
        description = this["description"] as String?
    )

    suspend fun syncData(lastSyncTime: Long) {
        // Implementation will be added when cloud service is configured
    }
}
