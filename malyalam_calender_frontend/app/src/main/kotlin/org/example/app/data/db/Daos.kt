package org.example.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Data Access Object for calendar events
 */
@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events")
    fun getAllEvents(): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date BETWEEN :startDate AND :endDate")
    fun getEventsForPeriod(startDate: Long, endDate: Long): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE lastModified > :timestamp")
    suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)
}

/**
 * Data Access Object for leaves
 */
@Dao
interface LeaveDao {
    @Query("SELECT * FROM leaves ORDER BY date DESC")
    fun getAllLeaves(): Flow<List<LeaveEntity>>

    @Query("SELECT * FROM leaves WHERE date BETWEEN :startDate AND :endDate")
    fun getLeavesForPeriod(startDate: Long, endDate: Long): Flow<List<LeaveEntity>>

    @Query("SELECT * FROM leaves WHERE lastModified > :timestamp")
    suspend fun getLeavesModifiedAfter(timestamp: Long): List<LeaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(leave: LeaveEntity)

    @Update
    suspend fun updateLeave(leave: LeaveEntity)

    @Delete
    suspend fun deleteLeave(leave: LeaveEntity)
}

/**
 * Data Access Object for astrology details
 */
@Dao
interface AstrologyDao {
    @Query("SELECT * FROM astrology_details WHERE date = :date LIMIT 1")
    fun getAstrologyForDate(date: Long): Flow<AstrologyEntity?>

    @Query("SELECT * FROM astrology_details WHERE lastModified > :timestamp")
    suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstrology(details: AstrologyEntity)

    @Update
    suspend fun updateAstrology(details: AstrologyEntity)

    @Delete
    suspend fun deleteAstrology(details: AstrologyEntity)
}
