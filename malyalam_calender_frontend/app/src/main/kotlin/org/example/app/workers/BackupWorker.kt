package org.example.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.app.backup.BackupManager
import org.example.app.data.db.AppDatabase
import org.example.app.data.repository.RoomCalendarRepository

class BackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val isAutoBackup = inputData.getBoolean("isAutoBackup", false)
        
        return try {
            val db = AppDatabase.getDatabase(applicationContext)
            val repository = RoomCalendarRepository(db)
            val backupManager = BackupManager(applicationContext, repository)
            
            backupManager.createBackup()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
