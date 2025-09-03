package org.example.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.data.db.*
import org.example.app.data.models.*
import org.example.app.utils.DateUtils.toTimestamp
import org.example.app.utils.DateUtils.toDate
import java.util.Date

class RoomCalendarRepository(private val database: AppDatabase) : CalendarRepository {

    override suspend fun getEventsForDate(date: Date): List<CalendarEvent> = withContext(Dispatchers.IO) {
        database.calendarEventDao().getEventsForDate(date.toTimestamp()).map { it.toModel() }
    }

    override suspend fun addEvent(event: CalendarEvent) = withContext(Dispatchers.IO) {
        database.calendarEventDao().insertEvent(event.toEntity())
    }

    override suspend fun updateEvent(event: CalendarEvent) = withContext(Dispatchers.IO) {
        database.calendarEventDao().updateEvent(event.toEntity())
    }

    override suspend fun deleteEvent(eventId: String) = withContext(Dispatchers.IO) {
        database.calendarEventDao().deleteEvent(eventId)
    }

    override suspend fun getLeavesForDate(date: Date): List<Leave> = withContext(Dispatchers.IO) {
        database.leaveDao().getLeavesForDate(date.toTimestamp()).map { it.toModel() }
    }

    override suspend fun addLeave(leave: Leave) = withContext(Dispatchers.IO) {
        database.leaveDao().insertLeave(leave.toEntity())
    }

    override suspend fun updateLeave(leave: Leave) = withContext(Dispatchers.IO) {
        database.leaveDao().updateLeave(leave.toEntity())
    }

    override suspend fun deleteLeave(leaveId: String) = withContext(Dispatchers.IO) {
        database.leaveDao().deleteLeave(leaveId)
    }

    override suspend fun getAstrologyForDate(date: Date): AstrologyDetails? = withContext(Dispatchers.IO) {
        database.astrologyDao().getAstrologyForDate(date.toTimestamp())?.toModel()
    }

    override suspend fun updateAstrology(details: AstrologyDetails) = withContext(Dispatchers.IO) {
        database.astrologyDao().insertAstrology(details.toEntity())
    }

    override suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEvent> = withContext(Dispatchers.IO) {
        database.calendarEventDao().getEventsModifiedAfter(timestamp).map { it.toModel() }
    }

    override suspend fun getLeavesModifiedAfter(timestamp: Long): List<Leave> = withContext(Dispatchers.IO) {
        database.leaveDao().getLeavesModifiedAfter(timestamp).map { it.toModel() }
    }

    override suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyDetails> = withContext(Dispatchers.IO) {
        database.astrologyDao().getAstrologyModifiedAfter(timestamp).map { it.toModel() }
    }
}
