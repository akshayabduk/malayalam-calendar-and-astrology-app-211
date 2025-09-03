package org.example.app.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.app.data.models.*
import java.util.*

/**
 * PUBLIC_INTERFACE
 * Repository interface for calendar data management
 */
interface CalendarRepository {
    /**
     * Get events for a specific month and year
     */
    fun getEvents(month: Int, year: Int): Flow<List<CalendarEvent>>

    /**
     * Add a new event to the calendar
     */
    suspend fun addEvent(event: CalendarEvent)

    /**
     * Update an existing event
     */
    suspend fun updateEvent(event: CalendarEvent)

    /**
     * Delete an event
     */
    suspend fun deleteEvent(event: CalendarEvent)

    /**
     * Get all leaves
     */
    fun getLeaves(): Flow<List<Leave>>

    /**
     * Add a new leave
     */
    suspend fun addLeave(leave: Leave)

    /**
     * Update an existing leave
     */
    suspend fun updateLeave(leave: Leave)

    /**
     * Delete a leave
     */
    suspend fun deleteLeave(leave: Leave)

    /**
     * Get astrology details for a specific date
     */
    fun getAstrologyDetails(date: Date): Flow<AstrologyDetails?>

    /**
     * Update astrology details
     */
    suspend fun updateAstrologyDetails(details: AstrologyDetails)
}
