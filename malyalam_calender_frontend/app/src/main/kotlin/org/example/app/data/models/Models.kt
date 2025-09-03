package org.example.app.data.models

import java.util.Date

enum class EventType {
    HOLIDAY,
    LEAVE,
    REMINDER
}

enum class LeaveType {
    PERSONAL,
    SICK,
    CASUAL
}

data class CalendarEvent(
    val id: String,
    val date: Date,
    val title: String,
    val type: EventType,
    val description: String? = null
)

data class Leave(
    val id: String,
    val date: Date,
    val title: String,
    val type: LeaveType,
    val description: String? = null
)

data class AstrologyDetails(
    val id: String,
    val date: Date,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String? = null
)
