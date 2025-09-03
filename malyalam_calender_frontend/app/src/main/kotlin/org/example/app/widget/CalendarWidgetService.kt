package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.example.app.R
import org.example.app.data.db.AppDatabase
import org.example.app.data.models.LeaveType
import org.example.app.data.repository.CalendarRepositoryImpl
import java.util.*

class CalendarWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = CalendarWidgetFactory(applicationContext)
    
    internal fun getLeaveTypeIcon(type: LeaveType): Int {
        return when (type) {
            LeaveType.PERSONAL -> R.drawable.ic_personal_leave
            LeaveType.SICK -> R.drawable.ic_sick_leave
            LeaveType.CASUAL -> R.drawable.ic_casual_leave
        }
    }
}

class CalendarWidgetFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private val repository = CalendarRepositoryImpl(AppDatabase.getDatabase(context))
    
    override fun onCreate() {}
    
    override fun onDataSetChanged() {}
    
    override fun onDestroy() {}
    
    override fun getCount(): Int = 0
    
    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_calendar_item)
    }
    
    override fun getLoadingView(): RemoteViews? = null
    
    override fun getViewTypeCount(): Int = 1
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun hasStableIds(): Boolean = true
}
