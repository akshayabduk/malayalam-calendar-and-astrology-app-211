package org.example.app.data.repository

import org.example.app.data.db.*
import org.example.app.data.models.*
import java.util.UUID

class CalendarRepositoryImpl(
    private val calendarEventDao: CalendarEventDao,
    private val leaveDao: LeaveDao,
    private val astrologyDao: AstrologyDao
) : CalendarRepository {

    override suspend fun getEvents(startTime: Long, endTime: Long): List<CalendarEvent> {
        return calendarEventDao.getEventsForPeriod(startTime, endTime).map { it.toDomainModel() }
    }

    override suspend fun getEventsForDate(date: Long): List<CalendarEvent> {
        val endOfDay = date + 24 * 60 * 60 * 1000 // Add 24 hours in milliseconds
        return calendarEventDao.getEventsForPeriod(date, endOfDay).map { it.toDomainModel() }
    }

    override suspend fun addEvent(event: CalendarEvent) {
        calendarEventDao.insertEvent(event.toEntity())
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        calendarEventDao.updateEvent(event.toEntity())
    }

    override suspend fun deleteEvent(eventId: String) {
        calendarEventDao.deleteEventById(eventId)
    }

    override suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEvent> {
        return calendarEventDao.getEventsModifiedAfter(timestamp).map { it.toDomainModel() }
    }

    override suspend fun getLeaves(): List<Leave> {
        return leaveDao.getAllLeaves().map { it.toDomainModel() }
    }

    override suspend fun getLeavesForPeriod(startTime: Long, endTime: Long): List<Leave> {
        return leaveDao.getLeavesForPeriod(startTime, endTime).map { it.toDomainModel() }
    }

    override suspend fun addLeave(leave: Leave) {
        leaveDao.insertLeave(leave.toEntity())
    }

    override suspend fun updateLeave(leave: Leave) {
        leaveDao.updateLeave(leave.toEntity())
    }

    override suspend fun deleteLeave(leaveId: String) {
        leaveDao.deleteLeaveById(leaveId)
    }

    override suspend fun getLeavesModifiedAfter(timestamp: Long): List<Leave> {
        return leaveDao.getLeavesModifiedAfter(timestamp).map { it.toDomainModel() }
    }

    override suspend fun getAstrologyDetails(date: Long): AstrologyDetails? {
        return astrologyDao.getAstrologyForDate(date)?.toDomainModel()
    }

    override suspend fun updateAstrologyDetails(details: AstrologyDetails) {
        astrologyDao.insertAstrology(details.toEntity())
    }

    override suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyDetails> {
        return astrologyDao.getAstrologyModifiedAfter(timestamp).map { it.toDomainModel() }
    }
}
