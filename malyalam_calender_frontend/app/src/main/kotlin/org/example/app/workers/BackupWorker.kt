package org.example.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.example.app.backup.BackupManager
import org.example.app.notifications.LocalNotificationManager

class BackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val backupManager = BackupManager(context)
    private val notificationManager = LocalNotificationManager(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (backupManager.createBackup()) {
                notificationManager.showBackupSuccessNotification()
                Result.success()
            } else {
                notificationManager.showBackupFailureNotification()
                Result.failure()
            }
        } catch (e: Exception) {
            notificationManager.showBackupFailureNotification()
            Result.failure()
        }
    }
}
