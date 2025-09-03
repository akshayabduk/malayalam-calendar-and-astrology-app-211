package org.example.app.data.models

import java.util.Date

/**
 * Represents a calendar event (leave or holiday)
 */
data class CalendarEvent(
    val date: Date,
    val title: String,
    val type: EventType,
    val description: String? = null
)

/**
 * Type of calendar event
 */
enum class EventType {
    LEAVE,
    HOLIDAY
}

/**
 * Represents user leave details
 */
data class Leave(
    val id: String,
    val date: Date,
    val title: String,
    val type: LeaveType,
    val description: String? = null
)

/**
 * Type of leave
 */
enum class LeaveType {
    PERSONAL,
    SICK,
    CASUAL
}

/**
 * Daily astrology details
 */
data class AstrologyDetails(
    val date: Date,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String? = null
)
