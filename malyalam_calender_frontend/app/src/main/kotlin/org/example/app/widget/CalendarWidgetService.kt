package org.example.app.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.example.app.R
import org.example.app.data.models.Leave
import org.example.app.leaves.LeaveManager
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CalendarWidgetFactory(applicationContext)
    }
}

class CalendarWidgetFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val leaveManager = LeaveManager.getInstance(context)
    private var events = mutableListOf<Leave>()
    private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    override fun onCreate() {
        // Initialize
    }

    override fun onDataSetChanged() {
        // Get today's events
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 7) // Show next 7 days
        val endTime = calendar.timeInMillis

        events = leaveManager.leaves.value?.filter {
            it.date.time in startTime until endTime
        }?.sortedBy { it.date }?.toMutableList() ?: mutableListOf()
    }

    override fun onDestroy() {
        events.clear()
    }

    override fun getCount(): Int = events.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position < 0 || position >= events.size) {
            return RemoteViews(context.packageName, R.layout.widget_event_item)
        }

        val event = events[position]
        return RemoteViews(context.packageName, R.layout.widget_event_item).apply {
            setTextViewText(R.id.eventTitle, event.title)
            setTextViewText(R.id.eventDate, dateFormat.format(event.date))

            // Set type icon and color
            when (event.type) {
                LeaveType.PERSONAL -> {
                    setImageViewResource(R.id.eventIcon, R.drawable.ic_personal_leave)
                    setInt(R.id.eventIcon, "setColorFilter", context.getColor(R.color.leave_personal))
                }
                LeaveType.SICK -> {
                    setImageViewResource(R.id.eventIcon, R.drawable.ic_sick_leave)
                    setInt(R.id.eventIcon, "setColorFilter", context.getColor(R.color.leave_sick))
                }
                LeaveType.CASUAL -> {
                    setImageViewResource(R.id.eventIcon, R.drawable.ic_casual_leave)
                    setInt(R.id.eventIcon, "setColorFilter", context.getColor(R.color.leave_casual))
                }
            }
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}
