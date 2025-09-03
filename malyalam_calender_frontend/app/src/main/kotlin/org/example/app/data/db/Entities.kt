package org.example.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val type: String,
    val lastModified: Long = System.currentTimeMillis()
)

@Entity(tableName = "leaves")
data class LeaveEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val startDate: Long,
    val endDate: Long,
    val type: String,
    val lastModified: Long = System.currentTimeMillis()
)

@Entity(tableName = "astrology_details")
data class AstrologyEntity(
    @PrimaryKey
    val id: String,
    val date: Long,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String?,
    val lastModified: Long = System.currentTimeMillis()
)
