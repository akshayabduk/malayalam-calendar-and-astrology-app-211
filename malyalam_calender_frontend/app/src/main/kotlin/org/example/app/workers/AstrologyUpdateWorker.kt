package org.example.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.app.data.db.AppDatabase
import org.example.app.data.repository.RoomCalendarRepository
import org.example.app.astrology.AstrologyService
import java.util.*

class AstrologyUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val db = AppDatabase.getDatabase(context)
    private val repository = RoomCalendarRepository(db)
    private val astrologyService = AstrologyService(context)

    override suspend fun doWork(): Result {
        return try {
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            val astrologyDetails = astrologyService.getAstrologyDetails(today)
            repository.updateAstrologyDetails(astrologyDetails)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
