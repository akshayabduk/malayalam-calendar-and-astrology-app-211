package org.example.app.data.models

import java.util.Date

enum class EventType {
    HOLIDAY,
    LEAVE,
    REMINDER
}

enum class LeaveType {
    CASUAL,
    SICK,
    PERSONAL
}

data class CalendarEvent(
    val id: String,
    val title: String,
    val description: String,
    val startTime: Date,
    val endTime: Date,
    val type: EventType
)

data class Leave(
    val id: String,
    val title: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
    val type: LeaveType
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
