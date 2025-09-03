package org.example.app.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.example.app.R
import org.example.app.data.models.CalendarEvent
import java.util.*

class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    private val calendar = Calendar.getInstance()
    private var onDateClickListener: ((Date) -> Unit)? = null
    private var events: List<CalendarEvent> = emptyList()

    init {
        columnCount = 7
        setupCalendar()
    }

    fun setOnDateClickListener(listener: (Date) -> Unit) {
        onDateClickListener = listener
    }

    fun setEvents(events: List<CalendarEvent>) {
        this.events = events
        updateCalendar()
    }

    fun setMonth(month: Int, year: Int) {
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        updateCalendar()
    }

    private fun setupCalendar() {
        // Add day headers
        val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        daysOfWeek.forEach { day ->
            addView(createDayHeader(day))
        }
        updateCalendar()
    }

    private fun updateCalendar() {
        // Remove all day views
        for (i in childCount - 1 downTo 7) {
            removeViewAt(i)
        }

        // Get first day of month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Add padding for first week
        for (i in 0 until firstDayOfWeek) {
            addView(createEmptyDay())
        }

        // Add days
        for (day in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val hasEvent = events.any { event ->
                val eventCal = Calendar.getInstance().apply { time = event.date }
                eventCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                        eventCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                        eventCal.get(Calendar.DAY_OF_MONTH) == day
            }
            addView(createDay(day, hasEvent))
        }
    }

    private fun createDayHeader(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            textAlignment = TEXT_ALIGNMENT_CENTER
            setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
            setPadding(8, 8, 8, 8)
            layoutParams = LayoutParams().apply {
                width = 0
                height = LayoutParams.WRAP_CONTENT
                columnWeight = 1f
            }
        }
    }

    private fun createDay(dayOfMonth: Int, hasEvent: Boolean): TextView {
        return TextView(context).apply {
            text = dayOfMonth.toString()
            textAlignment = TEXT_ALIGNMENT_CENTER
            setPadding(8, 16, 8, 16)
            
            if (hasEvent) {
                setTextColor(ContextCompat.getColor(context, R.color.brand_primary))
                setBackgroundResource(R.drawable.bg_calendar_day_event)
            }

            setOnClickListener {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                onDateClickListener?.invoke(calendar.time)
            }

            layoutParams = LayoutParams().apply {
                width = 0
                height = LayoutParams.WRAP_CONTENT
                columnWeight = 1f
            }
        }
    }

    private fun createEmptyDay(): TextView {
        return TextView(context).apply {
            text = ""
            layoutParams = LayoutParams().apply {
                width = 0
                height = LayoutParams.WRAP_CONTENT
                columnWeight = 1f
            }
        }
    }
}
