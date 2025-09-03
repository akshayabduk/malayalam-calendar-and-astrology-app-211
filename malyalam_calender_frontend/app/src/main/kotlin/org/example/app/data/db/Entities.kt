package org.example.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.example.app.data.models.EventType
import org.example.app.data.models.LeaveType
import java.util.Date

@Entity(tableName = "calendar_events")
@TypeConverters(Converters::class)
data class CalendarEventEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val title: String,
    val type: EventType,
    val description: String?,
    val lastModified: Date = Date()
)

@Entity(tableName = "leaves")
@TypeConverters(Converters::class)
data class LeaveEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val title: String,
    val type: LeaveType,
    val description: String?,
    val lastModified: Date = Date()
)

@Entity(tableName = "astrology")
@TypeConverters(Converters::class)
data class AstrologyEntity(
    @PrimaryKey val id: String,
    val date: Date,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String?,
    val lastModified: Date = Date()
)
