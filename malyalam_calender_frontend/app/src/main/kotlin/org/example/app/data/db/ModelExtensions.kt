package org.example.app.data.db

import org.example.app.data.models.AstrologyDetails
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.Leave

fun CalendarEvent.toEntity() = CalendarEventEntity(
    id = id,
    date = date,
    title = title,
    type = type,
    description = description
)

fun CalendarEventEntity.toModel() = CalendarEvent(
    id = id,
    date = date,
    title = title,
    type = type,
    description = description
)

fun Leave.toEntity() = LeaveEntity(
    id = id,
    date = date,
    title = title,
    type = type,
    description = description
)

fun LeaveEntity.toModel() = Leave(
    id = id,
    date = date,
    title = title,
    type = type,
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

fun AstrologyEntity.toModel() = AstrologyDetails(
    id = id,
    date = date,
    raasi = raasi,
    nakshatra = nakshatra,
    sunrise = sunrise,
    sunset = sunset,
    specialNotes = specialNotes
)
