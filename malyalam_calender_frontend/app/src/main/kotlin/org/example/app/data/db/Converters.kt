package org.example.app.data.db

import androidx.room.TypeConverter
import org.example.app.data.models.EventType
import org.example.app.data.models.LeaveType
import java.util.Date

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
    fun fromEventType(value: EventType): String {
        return value.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }

    @TypeConverter
    fun fromLeaveType(value: LeaveType): String {
        return value.name
    }

    @TypeConverter
    fun toLeaveType(value: String): LeaveType {
        return LeaveType.valueOf(value)
    }
}
