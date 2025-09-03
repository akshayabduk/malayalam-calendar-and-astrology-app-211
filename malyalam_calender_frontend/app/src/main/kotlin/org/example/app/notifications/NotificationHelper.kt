package org.example.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.example.app.R

class NotificationHelper(private val context: Context) {
    
    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Event Reminders Channel
            val eventChannel = NotificationChannel(
                CHANNEL_EVENTS,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_desc)
            }

            // Backup Status Channel
            val backupChannel = NotificationChannel(
                CHANNEL_BACKUP,
                context.getString(R.string.backup_notification_channel),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.backup_notification_channel_description)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(eventChannel)
            notificationManager.createNotificationChannel(backupChannel)
        }
    }

    fun showEventReminder(title: String, content: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_EVENTS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_EVENT_REMINDER, notification)
    }

    fun showBackupProgress() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.backup_started))
            .setProgress(0, 0, true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_BACKUP_PROGRESS, notification)
    }

    fun showBackupComplete(success: Boolean) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(
                context.getString(
                    if (success) R.string.backup_success
                    else R.string.backup_failed
                )
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_BACKUP_COMPLETE, notification)
    }

    fun showRestoreProgress() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.restore_started))
            .setProgress(0, 0, true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_RESTORE_PROGRESS, notification)
    }

    fun showRestoreComplete(success: Boolean) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(
                context.getString(
                    if (success) R.string.restore_success
                    else R.string.restore_failed
                )
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_RESTORE_COMPLETE, notification)
    }

    fun showMigrationProgress() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.migration_title))
            .setContentText(context.getString(R.string.migration_message))
            .setProgress(0, 0, true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_MIGRATION_PROGRESS, notification)
    }

    fun showMigrationSuccess() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.migration_success))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_MIGRATION_COMPLETE, notification)
    }

    fun showMigrationError() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_BACKUP)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.migration_error))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_MIGRATION_COMPLETE, notification)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val CHANNEL_EVENTS = "events"
        private const val CHANNEL_BACKUP = "backup"

        private const val NOTIFICATION_EVENT_REMINDER = 1001
        private const val NOTIFICATION_BACKUP_PROGRESS = 2001
        private const val NOTIFICATION_BACKUP_COMPLETE = 2002
        private const val NOTIFICATION_RESTORE_PROGRESS = 2003
        private const val NOTIFICATION_RESTORE_COMPLETE = 2004
        private const val NOTIFICATION_MIGRATION_PROGRESS = 2005
        private const val NOTIFICATION_MIGRATION_COMPLETE = 2006
    }
}
