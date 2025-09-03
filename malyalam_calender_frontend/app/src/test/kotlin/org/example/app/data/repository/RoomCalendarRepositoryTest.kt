package org.example.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.example.app.data.db.AppDatabase
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.EventType
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import java.util.*

class RoomCalendarRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: RoomCalendarRepository

    @Before
    fun setup() {
        val context = mock<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = RoomCalendarRepository(context)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun `addEvent should store event in database`() = runTest {
        // Given
        val event = CalendarEvent(
            date = Date(),
            title = "Test Event",
            type = EventType.HOLIDAY,
            description = "Test Description"
        )

        // When
        repository.addEvent(event)

        // Then
        val storedEvents = repository.getEvents(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        )
        assertThat(storedEvents).hasSize(1)
        assertThat(storedEvents[0].title).isEqualTo("Test Event")
    }

    @Test
    fun `addLeave should store leave in database`() = runTest {
        // Given
        val leave = Leave(
            id = "1",
            date = Date(),
            title = "Test Leave",
            type = LeaveType.PERSONAL,
            description = "Test Description"
        )

        // When
        repository.addLeave(leave)

        // Then
        val storedLeaves = repository.getLeaves()
        assertThat(storedLeaves).hasSize(1)
        assertThat(storedLeaves[0].title).isEqualTo("Test Leave")
    }

    @Test
    fun `getAstrologyDetails should return default when not found`() = runTest {
        // When
        val details = repository.getAstrologyDetails(Date())

        // Then
        assertThat(details).isNotNull()
        assertThat(details.raasi).isEmpty()
        assertThat(details.nakshatra).isEmpty()
    }
}
