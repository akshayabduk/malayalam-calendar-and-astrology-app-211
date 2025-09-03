package org.example.app.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.CalendarDay
import org.example.app.utils.CalendarUtils
import java.util.*

/**
 * Adapter for displaying a full month calendar view
 */
class MonthViewAdapter(
    private val context: Context,
    private val onDayClick: (CalendarDay) -> Unit
) : RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder>() {

    private var calendar = Calendar.getInstance()
    private var days = listOf<CalendarDay>()
    private val dayAdapter = CalendarDayAdapter(onDayClick)

    init {
        updateMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
    }

    fun updateMonth(month: Int, year: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        days = CalendarUtils.getCalendarDays(month, year)
        dayAdapter.setData(days)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_month_view, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(calendar)
    }

    override fun getItemCount() = 1

    inner class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val monthText: TextView = view.findViewById(R.id.monthText)
        private val calendarGrid: RecyclerView = view.findViewById(R.id.calendarGrid)

        init {
            calendarGrid.apply {
                layoutManager = GridLayoutManager(context, 7)
                adapter = dayAdapter
            }
        }

        fun bind(calendar: Calendar) {
            monthText.text = CalendarUtils.getMonthYearString(calendar.time)
        }
    }
}
