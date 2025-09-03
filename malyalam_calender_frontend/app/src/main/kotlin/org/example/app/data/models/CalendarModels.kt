package org.example.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Event types supported in the calendar
 */
enum class EventType {
    LEAVE,
    HOLIDAY
}

/**
 * Leave types available for users
 */
enum class LeaveType {
    PERSONAL,
    SICK,
    CASUAL
}

/**
 * Calendar event entity for Room database
 */
@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val title: String,
    val type: EventType,
    val description: String? = null,
    val lastModified: Long = System.currentTimeMillis()
)

/**
 * Leave entity for Room database
 */
@Entity(tableName = "leaves")
data class LeaveEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val title: String,
    val type: LeaveType,
    val description: String? = null,
    val lastModified: Long = System.currentTimeMillis()
)

/**
 * Astrology entity for Room database
 */
@Entity(tableName = "astrology_details")
data class AstrologyEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String? = null,
    val lastModified: Long = System.currentTimeMillis()
)

/**
 * Calendar Event data model
 */
data class CalendarEvent(
    val id: String,
    val date: Date,
    val title: String,
    val type: EventType,
    val description: String? = null
)

/**
 * Leave data model
 */
data class Leave(
    val id: String,
    val date: Date,
    val title: String,
    val type: LeaveType,
    val description: String? = null
)

/**
 * Astrology details data model
 */
data class AstrologyDetails(
    val id: String,
    val date: Date,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String? = null
)
