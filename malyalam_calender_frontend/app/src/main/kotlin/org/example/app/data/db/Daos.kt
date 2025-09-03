package org.example.app.data.db

import androidx.room.*

@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events WHERE date >= :startTime AND date <= :endTime")
    suspend fun getEventsForDate(startTime: Long, endTime: Long): List<CalendarEventEntity>

    @Query("SELECT * FROM calendar_events WHERE lastModified > :timestamp")
    suspend fun getEventsModifiedAfter(timestamp: Long): List<CalendarEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Query("DELETE FROM calendar_events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: String)
}

@Dao
interface LeaveDao {
    @Query("SELECT * FROM leaves WHERE date >= :startTime AND date <= :endTime")
    suspend fun getLeavesForDate(startTime: Long, endTime: Long): List<LeaveEntity>

    @Query("SELECT * FROM leaves WHERE lastModified > :timestamp")
    suspend fun getLeavesModifiedAfter(timestamp: Long): List<LeaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(leave: LeaveEntity)

    @Update
    suspend fun updateLeave(leave: LeaveEntity)

    @Query("DELETE FROM leaves WHERE id = :leaveId")
    suspend fun deleteLeave(leaveId: String)
}

@Dao
interface AstrologyDao {
    @Query("SELECT * FROM astrology WHERE date >= :startTime AND date <= :endTime LIMIT 1")
    suspend fun getAstrologyForDate(startTime: Long, endTime: Long): AstrologyEntity?

    @Query("SELECT * FROM astrology WHERE lastModified > :timestamp")
    suspend fun getAstrologyModifiedAfter(timestamp: Long): List<AstrologyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstrology(astrology: AstrologyEntity)

    @Update
    suspend fun updateAstrology(astrology: AstrologyEntity)

    @Delete
    suspend fun deleteAstrology(astrology: AstrologyEntity)
}
