package org.example.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.app.data.db.*
import org.example.app.data.models.*
import java.util.*

class CalendarRepositoryImpl(
    private val calendarEventDao: CalendarEventDao,
    private val leaveDao: LeaveDao,
    private val astrologyDao: AstrologyDao
) : CalendarRepository {

    override fun getEvents(month: Int, year: Int): Flow<List<CalendarEvent>> {
        val calendar = Calendar.getInstance()
        
        // Start of month
        calendar.set(year, month, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        // End of month
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time

        return calendarEventDao.getEventsForPeriod(startDate, endDate)
            .map { entities ->
                entities.map { entity ->
                    CalendarEvent(
                        date = entity.date,
                        title = entity.title,
                        type = entity.type,
                        description = entity.description
                    )
                }
            }
    }

    override suspend fun addEvent(event: CalendarEvent) {
        calendarEventDao.insertEvent(
            CalendarEventEntity(
                title = event.title,
                description = event.description,
                date = event.date,
                type = event.type
            )
        )
    }

    override fun getLeaves(): Flow<List<Leave>> {
        return leaveDao.getAllLeaves()
            .map { entities ->
                entities.map { entity ->
                    Leave(
                        id = entity.id.toString(),
                        date = entity.date,
                        title = entity.title,
                        type = entity.type,
                        description = entity.description
                    )
                }
            }
    }

    override suspend fun addLeave(leave: Leave) {
        leaveDao.insertLeave(
            LeaveEntity(
                title = leave.title,
                description = leave.description,
                date = leave.date,
                type = leave.type
            )
        )
    }

    override fun getAstrologyDetails(date: Date): Flow<AstrologyDetails?> {
        return astrologyDao.getAstrologyForDate(date)
            .map { entity ->
                entity?.let {
                    AstrologyDetails(
                        date = it.date,
                        raasi = it.raasi,
                        nakshatra = it.nakshatra,
                        sunrise = it.sunrise,
                        sunset = it.sunset,
                        specialNotes = it.specialNotes
                    )
                }
            }
    }
}
