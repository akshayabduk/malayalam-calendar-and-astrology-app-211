package org.example.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType

@Dao
interface LeaveDao {
    @Query("SELECT * FROM leaves")
    fun getAllLeaves(): Flow<List<Leave>>

    @Query("SELECT * FROM leaves WHERE type = :type")
    fun getLeavesByType(type: LeaveType): Flow<List<Leave>>

    @Query("SELECT * FROM leaves WHERE title LIKE :query OR description LIKE :query")
    fun searchLeaves(query: String): Flow<List<Leave>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(leave: Leave)

    @Update
    suspend fun updateLeave(leave: Leave)

    @Delete
    suspend fun deleteLeave(leave: Leave)

    @Query("SELECT * FROM leaves WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getLeavesForPeriod(startDate: Date, endDate: Date): List<Leave>
}
