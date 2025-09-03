package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.data.db.AppDatabase
import org.example.app.data.repository.RoomCalendarRepository

class AstrologyWidgetProvider : AppWidgetProvider() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val db = AppDatabase.getDatabase(context)
        val repository = RoomCalendarRepository(db)

        appWidgetIds.forEach { appWidgetId ->
            updateAstrologyWidget(context, appWidgetManager, appWidgetId, repository)
        }
    }

    private fun updateAstrologyWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        repository: RoomCalendarRepository
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_astrology)
        coroutineScope.launch {
            val today = Date()
            val details = repository.getAstrologyDetails(today)
            // Update widget with astrology details
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
