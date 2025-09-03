package org.example.app.data.repository

import org.example.app.data.models.*

interface CalendarRepository {
    suspend fun getEvents(startTime: Long, endTime: Long): List<CalendarEvent>
    suspend fun getEventsForDate(date: Long): List<CalendarEvent>
    suspend fun addEvent(event: CalendarEvent)
    suspend fun updateEvent(event: CalendarEvent)
    suspend fun deleteEvent(eventId: String)
    suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEvent>

    suspend fun getLeaves(): List<Leave>
    suspend fun getLeavesForPeriod(startTime: Long, endTime: Long): List<Leave>
    suspend fun addLeave(leave: Leave)
    suspend fun updateLeave(leave: Leave)
    suspend fun deleteLeave(leaveId: String)
    suspend fun getLeavesModifiedAfter(timestamp: Long): List<Leave>

    suspend fun getAstrologyDetails(date: Long): AstrologyDetails?
    suspend fun updateAstrologyDetails(details: AstrologyDetails)
    suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyDetails>
}
