package org.example.app.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import org.example.app.data.repository.CalendarRepository
import org.example.app.data.repository.RoomCalendarRepository

class WidgetUpdateService : Service() {
    private lateinit var repository: CalendarRepository
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    override fun onCreate() {
        super.onCreate()
        repository = RoomCalendarRepository(this)
        startUpdateCycle()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startUpdateCycle() {
        serviceScope.launch {
            while (isActive) {
                updateWidgets()
                delay(UPDATE_INTERVAL)
            }
        }
    }

    private suspend fun updateWidgets() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        
        // Update Calendar Widgets
        val calendarWidgets = appWidgetManager.getAppWidgetIds(
            ComponentName(this, CalendarWidget::class.java)
        )
        calendarWidgets.forEach { widgetId ->
            CalendarWidget.updateWidget(this, appWidgetManager, widgetId)
        }

        // Update Astrology Widgets
        val astrologyWidgets = appWidgetManager.getAppWidgetIds(
            ComponentName(this, AstrologyWidget::class.java)
        )
        astrologyWidgets.forEach { widgetId ->
            AstrologyWidget.updateWidget(this, appWidgetManager, widgetId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    companion object {
        private const val UPDATE_INTERVAL = 1800000L // 30 minutes

        fun startService(context: Context) {
            context.startService(Intent(context, WidgetUpdateService::class.java))
        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, WidgetUpdateService::class.java))
        }
    }
}
