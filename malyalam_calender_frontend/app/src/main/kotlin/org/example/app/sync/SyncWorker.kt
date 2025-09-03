package org.example.app.sync

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

/**
 * Background worker for periodic data synchronization
 */
class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sync")
        
        return try {
            val syncManager = SyncManager(applicationContext)
            val status = syncManager.sync().first()
            
            when (status) {
                is SyncStatus.Success -> Result.success()
                is SyncStatus.Error -> Result.retry()
                else -> Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SyncWorker"
        private const val SYNC_WORK_NAME = "sync_work"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<SyncWorker>(
                repeatInterval = 15,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
            .setConstraints(constraints)
            .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    SYNC_WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request
                )
        }
    }
}
