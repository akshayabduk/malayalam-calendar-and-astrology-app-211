package org.example.app.sync

import android.content.Context
import com.google.common.truth.Truth.assertThat
import org.example.app.data.models.CalendarEvent
import org.example.app.data.models.EventType
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import java.util.*

class SyncConflictResolverTest {
    private lateinit var context: Context
    private lateinit var resolver: SyncConflictResolver

    @Before
    fun setup() {
        context = mock()
        resolver = SyncConflictResolver(context)
    }

    @Test
    fun `mergeEvents should keep longer title and combine descriptions`() {
        // Given
        val date = Date()
        val localEvent = CalendarEvent(
            date = date,
            title = "Short",
            type = EventType.LEAVE,
            description = "Local description"
        )
        val remoteEvent = CalendarEvent(
            date = date,
            title = "Longer Title",
            type = EventType.LEAVE,
            description = "Remote description"
        )
        var mergedEvent: CalendarEvent? = null

        // When
        resolver.resolveEventConflict(localEvent, remoteEvent) { event ->
            mergedEvent = event
        }

        // Then
        assertThat(mergedEvent).isNotNull()
        assertThat(mergedEvent!!.title).isEqualTo("Longer Title")
        assertThat(mergedEvent!!.description).isEqualTo("Remote description")
    }

    @Test
    fun `mergeLeaves should preserve local type on conflict`() {
        // Given
        val date = Date()
        val localLeave = Leave(
            id = "1",
            date = date,
            title = "Leave",
            type = LeaveType.SICK,
            description = "Local sick leave"
        )
        val remoteLeave = Leave(
            id = "1",
            date = date,
            title = "Leave",
            type = LeaveType.CASUAL,
            description = "Remote casual leave"
        )
        var mergedLeave: Leave? = null

        // When
        resolver.resolveLeaveConflict(localLeave, remoteLeave) { leave ->
            mergedLeave = leave
        }

        // Then
        assertThat(mergedLeave).isNotNull()
        assertThat(mergedLeave!!.type).isEqualTo(LeaveType.SICK)
        assertThat(mergedLeave!!.description).isEqualTo("Local sick leave")
    }
}
