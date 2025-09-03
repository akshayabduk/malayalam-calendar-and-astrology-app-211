package org.example.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.example.app.R
import org.example.app.data.models.CalendarEvent

class NotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "calendar_events"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Calendar Events",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for calendar events"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showEventNotification(event: CalendarEvent) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_calendar_notification)
            .setContentTitle(event.title)
            .setContentText(event.description ?: "")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(event.id.hashCode(), notification)
    }
}
