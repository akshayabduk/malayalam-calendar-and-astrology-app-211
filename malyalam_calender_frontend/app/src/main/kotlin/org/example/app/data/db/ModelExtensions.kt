package org.example.app.data.db

import org.example.app.data.models.*
import java.util.Date
import java.util.UUID

fun CalendarEventEntity.toDomainModel(): CalendarEvent {
    return CalendarEvent(
        id = id,
        title = title,
        description = description,
        startTime = Date(startTime),
        endTime = Date(endTime),
        type = EventType.valueOf(type)
    )
}

fun CalendarEvent.toEntity(): CalendarEventEntity {
    return CalendarEventEntity(
        id = id,
        title = title,
        description = description,
        startTime = startTime.time,
        endTime = endTime.time,
        type = type.name
    )
}

fun LeaveEntity.toDomainModel(): Leave {
    return Leave(
        id = id,
        title = title,
        description = description,
        startDate = Date(startDate),
        endDate = Date(endDate),
        type = LeaveType.valueOf(type)
    )
}

fun Leave.toEntity(): LeaveEntity {
    return LeaveEntity(
        id = id,
        title = title,
        description = description,
        startDate = startDate.time,
        endDate = endDate.time,
        type = type.name
    )
}

fun AstrologyEntity.toDomainModel(): AstrologyDetails {
    return AstrologyDetails(
        id = id,
        date = Date(date),
        raasi = raasi,
        nakshatra = nakshatra,
        sunrise = sunrise,
        sunset = sunset,
        specialNotes = specialNotes
    )
}

fun AstrologyDetails.toEntity(): AstrologyEntity {
    return AstrologyEntity(
        id = id,
        date = date.time,
        raasi = raasi,
        nakshatra = nakshatra,
        sunrise = sunrise,
        sunset = sunset,
        specialNotes = specialNotes
    )
}
