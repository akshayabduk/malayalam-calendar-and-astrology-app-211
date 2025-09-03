package org.example.app.data.db

import androidx.room.TypeConverter
import org.example.app.data.models.EventType
import org.example.app.data.models.LeaveType
import java.util.Date

/**
 * Type converters for Room database
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromEventType(type: EventType): String {
        return type.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }

    @TypeConverter
    fun fromLeaveType(type: LeaveType): String {
        return type.name
    }

    @TypeConverter
    fun toLeaveType(value: String): LeaveType {
        return LeaveType.valueOf(value)
    }
}
