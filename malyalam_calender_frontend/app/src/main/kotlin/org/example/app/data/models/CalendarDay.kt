package org.example.app.data.models

import java.util.Date

/**
 * Represents a day in the calendar
 */
data class CalendarDay(
    val date: Date,
    val dayOfMonth: Int,
    val isCurrentMonth: Boolean,
    val hasEvent: Boolean = false,
    val isHoliday: Boolean = false,
    val isLeave: Boolean = false
)
