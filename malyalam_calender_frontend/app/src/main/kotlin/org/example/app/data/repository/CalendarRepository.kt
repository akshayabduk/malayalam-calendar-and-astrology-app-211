package org.example.app.data.repository

import org.example.app.data.models.AstrologyDetails
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.Leave
import java.util.Date

interface CalendarRepository {
    suspend fun getEventsForDate(date: Date): List<CalendarEvent>
    suspend fun addEvent(event: CalendarEvent)
    suspend fun updateEvent(event: CalendarEvent)
    suspend fun deleteEvent(eventId: String)

    suspend fun getLeavesForDate(date: Date): List<Leave>
    suspend fun addLeave(leave: Leave)
    suspend fun updateLeave(leave: Leave)
    suspend fun deleteLeave(leaveId: String)

    suspend fun getAstrologyForDate(date: Date): AstrologyDetails?
    suspend fun updateAstrology(details: AstrologyDetails)

    suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEvent>
    suspend fun getLeavesModifiedAfter(timestamp: Long): List<Leave>
    suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyDetails>
}
