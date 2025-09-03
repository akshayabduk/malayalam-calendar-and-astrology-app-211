package org.example.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Calendar event entity for Room database
 */
@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val title: String,
    val type: String,
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
    val type: String,
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
 * Extension functions to convert between entity and model classes
 */
fun CalendarEventEntity.toModel() = CalendarEvent(
    id = id,
    date = date,
    title = title,
    type = EventType.valueOf(type),
    description = description
)

fun LeaveEntity.toModel() = Leave(
    id = id,
    date = date,
    title = title,
    type = LeaveType.valueOf(type),
    description = description
)

fun AstrologyEntity.toModel() = AstrologyDetails(
    id = id,
    date = date,
    raasi = raasi,
    nakshatra = nakshatra,
    sunrise = sunrise,
    sunset = sunset,
    specialNotes = specialNotes
)

/**
 * Extension functions to convert between model and entity classes
 */
fun CalendarEvent.toEntity() = CalendarEventEntity(
    id = id,
    date = date,
    title = title,
    type = type.name,
    description = description
)

fun Leave.toEntity() = LeaveEntity(
    id = id,
    date = date,
    title = title,
    type = type.name,
    description = description
)

fun AstrologyDetails.toEntity() = AstrologyEntity(
    id = id,
    date = date,
    raasi = raasi,
    nakshatra = nakshatra,
    sunrise = sunrise,
    sunset = sunset,
    specialNotes = specialNotes
)
