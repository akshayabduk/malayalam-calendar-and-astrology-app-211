package org.example.app.data.repository

import android.content.Context
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.example.app.data.db.AppDatabase
import org.example.app.data.db.AstrologyEntity
import org.example.app.data.db.CalendarEventEntity
import org.example.app.data.db.LeaveEntity
import org.example.app.data.models.*
import java.util.*

class RoomCalendarRepository(context: Context) : CalendarRepository {
    private val database = AppDatabase.getDatabase(context)
    private val calendarEventDao = database.calendarEventDao()
    private val leaveDao = database.leaveDao()
    private val astrologyDao = database.astrologyDao()

    override suspend fun getEvents(month: Int, year: Int): List<CalendarEvent> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val startDate = calendar.time
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endDate = calendar.time

        return calendarEventDao.getEventsForPeriod(startDate, endDate)
            .map { events -> events.map { it.toModel() } }
            .firstOrNull() ?: emptyList()
    }

    override suspend fun addEvent(event: CalendarEvent) {
        calendarEventDao.insertEvent(event.toEntity())
    }

    override suspend fun getLeaves(): List<Leave> {
        return leaveDao.getAllLeaves()
            .map { leaves -> leaves.map { it.toModel() } }
            .firstOrNull() ?: emptyList()
    }

    override suspend fun addLeave(leave: Leave) {
        leaveDao.insertLeave(leave.toEntity())
    }

    override suspend fun getAstrologyDetails(date: Date): AstrologyDetails {
        return astrologyDao.getAstrologyForDate(date)?.toModel()
            ?: AstrologyDetails(
                date = date,
                raasi = "",
                nakshatra = "",
                sunrise = "",
                sunset = "",
                specialNotes = null
            )
    }

    override suspend fun addAstrologyDetails(details: AstrologyDetails) {
        astrologyDao.insertAstrology(AstrologyEntity(
            date = details.date,
            raasi = details.raasi,
            nakshatra = details.nakshatra,
            sunrise = details.sunrise,
            sunset = details.sunset,
            specialNotes = details.specialNotes
        ))
    }

    // Extension functions to convert between entities and models
    private fun CalendarEventEntity.toModel() = CalendarEvent(
        date = date,
        title = title,
        type = EventType.valueOf(type),
        description = description
    )

    private fun CalendarEvent.toEntity() = CalendarEventEntity(
        id = UUID.randomUUID().toString(),
        date = date,
        title = title,
        type = type.name,
        description = description,
        lastModified = System.currentTimeMillis()
    )

    private fun LeaveEntity.toModel() = Leave(
        id = id,
        date = date,
        title = title,
        type = LeaveType.valueOf(type),
        description = description
    )

    private fun Leave.toEntity() = LeaveEntity(
        id = id,
        date = date,
        title = title,
        type = type.name,
        description = description,
        lastModified = System.currentTimeMillis()
    )

    private fun AstrologyEntity.toModel() = AstrologyDetails(
        date = date,
        raasi = raasi,
        nakshatra = nakshatra,
        sunrise = sunrise,
        sunset = sunset,
        specialNotes = specialNotes
    )
}
