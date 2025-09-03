package org.example.app.ui.models

import java.util.*

data class CalendarDay(
    val date: Date,
    val isCurrentMonth: Boolean = true,
    val events: List<EventUI> = emptyList(),
    val isToday: Boolean = false
)

data class EventUI(
    val id: String,
    val title: String,
    val type: EventTypeUI
)

enum class EventTypeUI {
    LEAVE,
    HOLIDAY
}
