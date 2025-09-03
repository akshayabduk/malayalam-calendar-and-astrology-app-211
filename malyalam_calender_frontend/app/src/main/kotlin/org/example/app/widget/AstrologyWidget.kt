package org.example.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.example.app.R
import org.example.app.data.db.AppDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import java.util.*

/**
 * Widget provider for displaying daily astrology details
 */
class AstrologyWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            updateAppWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) = runBlocking {
        val database = AppDatabase.getDatabase(context)
        val views = RemoteViews(context.packageName, R.layout.widget_astrology)

        val today = Calendar.getInstance().time
        val astrologyDetails = database.astrologyDao().getAstrologyForDate(today).first()

        if (astrologyDetails != null) {
            views.setTextViewText(R.id.raasiText, astrologyDetails.raasi)
            views.setTextViewText(R.id.nakshatraText, astrologyDetails.nakshatra)
            views.setTextViewText(R.id.sunriseText, astrologyDetails.sunrise)
            views.setTextViewText(R.id.sunsetText, astrologyDetails.sunset)
            views.setViewVisibility(R.id.contentLayout, android.view.View.VISIBLE)
            views.setViewVisibility(R.id.errorLayout, android.view.View.GONE)
        } else {
            views.setViewVisibility(R.id.contentLayout, android.view.View.GONE)
            views.setViewVisibility(R.id.errorLayout, android.view.View.VISIBLE)
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
