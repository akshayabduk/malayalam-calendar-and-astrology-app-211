package org.example.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.app.data.db.*
import org.example.app.data.models.*
import java.util.*

class CalendarRepositoryImpl(private val db: AppDatabase) : CalendarRepository {
    private val calendarEventDao = db.calendarEventDao()
    private val leaveDao = db.leaveDao()
    private val astrologyDao = db.astrologyDao()

    override fun getEvents(month: Int, year: Int): Flow<List<CalendarEvent>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1, 0, 0, 0)
        val startDate = calendar.timeInMillis
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        val endDate = calendar.timeInMillis

        return calendarEventDao.getEventsForPeriod(startDate, endDate)
            .map { events -> events.map { it.toDomainModel() } }
    }

    override suspend fun addEvent(event: CalendarEvent) {
        calendarEventDao.insertEvent(event.toEntity())
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        calendarEventDao.updateEvent(event.toEntity())
    }

    override suspend fun deleteEvent(event: CalendarEvent) {
        calendarEventDao.deleteEvent(event.toEntity())
    }

    override fun getLeaves(): Flow<List<Leave>> {
        return leaveDao.getAllLeaves()
            .map { leaves -> leaves.map { it.toDomainModel() } }
    }

    override suspend fun addLeave(leave: Leave) {
        leaveDao.insertLeave(leave.toEntity())
    }

    override suspend fun updateLeave(leave: Leave) {
        leaveDao.updateLeave(leave.toEntity())
    }

    override suspend fun deleteLeave(leave: Leave) {
        leaveDao.deleteLeave(leave.toEntity())
    }

    override fun getAstrologyDetails(date: Date): Flow<AstrologyDetails?> {
        return astrologyDao.getAstrologyForDate(date.time)
            .map { entity -> entity?.toDomainModel() }
    }

    override suspend fun updateAstrologyDetails(details: AstrologyDetails) {
        astrologyDao.updateAstrology(details.toEntity())
    }

    private fun CalendarEventEntity.toDomainModel() = CalendarEvent(
        id = id,
        date = Date(date),
        title = title,
        type = EventType.valueOf(type),
        description = description
    )

    private fun CalendarEvent.toEntity() = CalendarEventEntity(
        id = id,
        date = date.time,
        title = title,
        type = type.name,
        description = description
    )

    private fun LeaveEntity.toDomainModel() = Leave(
        id = id,
        date = Date(date),
        title = title,
        type = LeaveType.valueOf(type),
        description = description
    )

    private fun Leave.toEntity() = LeaveEntity(
        id = id,
        date = date.time,
        title = title,
        type = type.name,
        description = description
    )

    private fun AstrologyEntity.toDomainModel() = AstrologyDetails(
        id = id,
        date = Date(date),
        raasi = raasi,
        nakshatra = nakshatra,
        sunrise = sunrise,
        sunset = sunset,
        specialNotes = specialNotes
    )

    private fun AstrologyDetails.toEntity() = AstrologyEntity(
        id = id,
        date = date.time,
        raasi = raasi,
        nakshatra = nakshatra,
        sunrise = sunrise,
        sunset = sunset,
        specialNotes = specialNotes
    )
}
