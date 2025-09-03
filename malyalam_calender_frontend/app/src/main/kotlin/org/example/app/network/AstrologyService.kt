package org.example.app.network

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface AstrologyService {
    @GET("astrology/daily")
    suspend fun getDailyAstrology(
        @Query("date") date: String,
        @Query("timezone") timezone: String = TimeZone.getDefault().id
    ): AstrologyResponse
}

data class AstrologyResponse(
    val date: String,
    val raasi: String,
    val nakshatra: String,
    val sunrise: String,
    val sunset: String,
    val specialNotes: String?
)
