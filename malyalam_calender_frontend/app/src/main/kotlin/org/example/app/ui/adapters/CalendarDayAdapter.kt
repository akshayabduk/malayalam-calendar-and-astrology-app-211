package org.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.CalendarEvent
import java.util.*

/**
 * Adapter for displaying calendar days in a grid
 */
class CalendarDayAdapter(
    private var days: List<CalendarDay>,
    private val onDayClick: (Date) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder>() {

    data class CalendarDay(
        val date: Date,
        val isCurrentMonth: Boolean,
        val events: List<CalendarEvent> = emptyList()
    )

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.dayText)
        val eventIndicator: View = view.findViewById(R.id.eventIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        val calendar = Calendar.getInstance().apply { time = day.date }
        
        holder.dayText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        holder.dayText.alpha = if (day.isCurrentMonth) 1.0f else 0.3f
        
        holder.eventIndicator.visibility = 
            if (day.events.isNotEmpty()) View.VISIBLE else View.GONE
        
        holder.itemView.setOnClickListener { onDayClick(day.date) }
    }

    override fun getItemCount() = days.size

    fun updateDays(newDays: List<CalendarDay>) {
        days = newDays
        notifyDataSetChanged()
    }
}
