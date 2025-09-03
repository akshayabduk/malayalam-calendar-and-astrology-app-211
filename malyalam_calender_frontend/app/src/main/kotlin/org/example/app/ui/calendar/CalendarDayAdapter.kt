package org.example.app.ui.calendar

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.CalendarDay
import java.util.*

/**
 * Adapter for displaying calendar days in a grid
 */
class CalendarDayAdapter(
    private val onDayClick: (CalendarDay) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder>() {

    private var days = listOf<CalendarDay>()
    private var today = Calendar.getInstance()

    fun setData(days: List<CalendarDay>) {
        this.days = days
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view, onDayClick)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size

    inner class DayViewHolder(
        itemView: View,
        private val onDayClick: (CalendarDay) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.dayText)
        private val eventIndicator: View = itemView.findViewById(R.id.eventIndicator)

        fun bind(day: CalendarDay) {
            dayText.text = day.dayOfMonth.toString()

            // Style for today
            if (isToday(day.date)) {
                dayText.setTypeface(null, Typeface.BOLD)
                dayText.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.brand_primary)
                )
            } else {
                dayText.setTypeface(null, Typeface.NORMAL)
                dayText.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (day.isCurrentMonth) android.R.color.black
                        else android.R.color.darker_gray
                    )
                )
            }

            // Show event indicator
            eventIndicator.visibility = if (day.hasEvent) View.VISIBLE else View.GONE
            if (day.hasEvent) {
                eventIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        when {
                            day.isHoliday -> R.color.brand_accent
                            day.isLeave -> R.color.brand_primary
                            else -> R.color.brand_secondary
                        }
                    )
                )
            }

            itemView.setOnClickListener { onDayClick(day) }
        }

        private fun isToday(date: Date): Boolean {
            val cal = Calendar.getInstance().apply { time = date }
            return cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                   cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
        }
    }
}
