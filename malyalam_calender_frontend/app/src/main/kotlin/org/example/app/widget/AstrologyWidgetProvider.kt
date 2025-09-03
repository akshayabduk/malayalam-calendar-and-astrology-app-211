package org.example.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.app.MainActivity
import org.example.app.MalayalamCalendarApp
import org.example.app.R
import org.example.app.utils.MalayalamCalendar
import java.util.*

class AstrologyWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = (context.applicationContext as MalayalamCalendarApp).repository
            val today = Calendar.getInstance()
            
            val astrologyDetails = repository.getAstrologyDetails(today.time)
            val malayalamDate = MalayalamCalendar.formatMalayalamDate(today.time)

            // Update all widgets
            appWidgetIds.forEach { widgetId ->
                updateWidget(
                    context,
                    appWidgetManager,
                    widgetId,
                    malayalamDate,
                    astrologyDetails.raasi,
                    astrologyDetails.nakshatra
                )
            }
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        date: String,
        raasi: String,
        nakshatra: String
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_astrology)
        
        // Update text
        views.setTextViewText(R.id.dateText, date)
        views.setTextViewText(R.id.raasiText, raasi)
        views.setTextViewText(R.id.nakshatraText, nakshatra)

        // Add click intent
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("openTab", 2) // Open astrology tab
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(android.R.id.background, pendingIntent)

        // Update widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
