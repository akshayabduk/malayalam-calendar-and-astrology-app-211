package org.example.app.leaves

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Manages leave data operations
 */
class LeaveManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _leaves = MutableLiveData<List<Leave>>()
    
    val leaves: LiveData<List<Leave>> = _leaves

    init {
        loadLeaves()
    }

    /**
     * Create a new leave entry
     */
    suspend fun createLeave(
        title: String,
        date: Date,
        type: LeaveType,
        description: String? = null
    ): Leave {
        val leave = Leave(
            id = UUID.randomUUID().toString(),
            title = title,
            date = date,
            type = type,
            description = description
        )

        val currentLeaves = (_leaves.value ?: emptyList()).toMutableList()
        currentLeaves.add(leave)
        _leaves.value = currentLeaves.sortedBy { it.date }
        
        saveLeaves()
        return leave
    }

    /**
     * Update an existing leave entry
     */
    suspend fun updateLeave(
        id: String,
        title: String,
        date: Date,
        type: LeaveType,
        description: String? = null
    ): Leave {
        val updatedLeave = Leave(
            id = id,
            title = title,
            date = date,
            type = type,
            description = description
        )

        val currentLeaves = (_leaves.value ?: emptyList()).toMutableList()
        val index = currentLeaves.indexOfFirst { it.id == id }
        if (index != -1) {
            currentLeaves[index] = updatedLeave
            _leaves.value = currentLeaves.sortedBy { it.date }
            saveLeaves()
        }

        return updatedLeave
    }

    /**
     * Delete a leave entry
     */
    suspend fun deleteLeave(id: String) {
        val currentLeaves = (_leaves.value ?: emptyList()).toMutableList()
        currentLeaves.removeAll { it.id == id }
        _leaves.value = currentLeaves
        saveLeaves()
    }

    /**
     * Get a specific leave by ID
     */
    suspend fun getLeave(id: String): Leave? {
        return _leaves.value?.find { it.id == id }
    }

    /**
     * Get all leaves for a specific date
     */
    suspend fun getLeavesForDate(date: Date): List<Leave> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endTime = calendar.timeInMillis

        return _leaves.value?.filter {
            it.date.time in startTime until endTime
        } ?: emptyList()
    }

    private fun loadLeaves() {
        val leavesJson = prefs.getString(KEY_LEAVES, "[]")
        val leavesList = ArrayList<Leave>()
        
        try {
            val jsonArray = JSONArray(leavesJson)
            for (i in 0 until jsonArray.length()) {
                val leaveObj = jsonArray.getJSONObject(i)
                leavesList.add(
                    Leave(
                        id = leaveObj.getString("id"),
                        title = leaveObj.getString("title"),
                        date = Date(leaveObj.getLong("date")),
                        type = LeaveType.valueOf(leaveObj.getString("type")),
                        description = if (leaveObj.has("description")) 
                            leaveObj.getString("description") else null
                    )
                )
            }
        } catch (e: Exception) {
            // Handle error
        }

        _leaves.value = leavesList.sortedBy { it.date }
    }

    private fun saveLeaves() {
        val jsonArray = JSONArray()
        _leaves.value?.forEach { leave ->
            val leaveObj = JSONObject().apply {
                put("id", leave.id)
                put("title", leave.title)
                put("date", leave.date.time)
                put("type", leave.type.name)
                leave.description?.let { put("description", it) }
            }
            jsonArray.put(leaveObj)
        }

        prefs.edit()
            .putString(KEY_LEAVES, jsonArray.toString())
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "leave_manager"
        private const val KEY_LEAVES = "leaves"

        @Volatile
        private var instance: LeaveManager? = null

        fun getInstance(context: Context): LeaveManager {
            return instance ?: synchronized(this) {
                instance ?: LeaveManager(context).also { instance = it }
            }
        }
    }
}
