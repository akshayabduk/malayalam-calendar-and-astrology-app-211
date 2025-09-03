package org.example.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events WHERE startTime >= :startTime AND endTime <= :endTime")
    suspend fun getEventsForPeriod(startTime: Long, endTime: Long): List<CalendarEventEntity>

    @Query("SELECT * FROM calendar_events WHERE startTime >= :date AND endTime <= :date")
    suspend fun getEventsForDate(date: Long): List<CalendarEventEntity>

    @Query("SELECT * FROM calendar_events WHERE lastModified > :timestamp")
    suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)

    @Query("DELETE FROM calendar_events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: String)
}

@Dao
interface LeaveDao {
    @Query("SELECT * FROM leaves")
    suspend fun getAllLeaves(): List<LeaveEntity>

    @Query("SELECT * FROM leaves WHERE startDate >= :startTime AND endDate <= :endTime")
    suspend fun getLeavesForPeriod(startTime: Long, endTime: Long): List<LeaveEntity>

    @Query("SELECT * FROM leaves WHERE lastModified > :timestamp")
    suspend fun getLeavesModifiedAfter(timestamp: Long): List<LeaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(leave: LeaveEntity)

    @Update
    suspend fun updateLeave(leave: LeaveEntity)

    @Delete
    suspend fun deleteLeave(leave: LeaveEntity)

    @Query("DELETE FROM leaves WHERE id = :leaveId")
    suspend fun deleteLeaveById(leaveId: String)
}

@Dao
interface AstrologyDao {
    @Query("SELECT * FROM astrology_details WHERE date = :date")
    suspend fun getAstrologyForDate(date: Long): AstrologyEntity?

    @Query("SELECT * FROM astrology_details WHERE lastModified > :timestamp")
    suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstrology(astrology: AstrologyEntity)

    @Update
    suspend fun updateAstrology(astrology: AstrologyEntity)

    @Query("DELETE FROM astrology_details WHERE id = :id")
    suspend fun deleteAstrologyById(id: String)
}
