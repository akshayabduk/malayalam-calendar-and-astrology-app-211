package org.example.app.utils

import java.util.*
import kotlin.math.*

/**
 * Utility class for astronomical calculations
 */
object AstronomicalCalculator {
    private const val OBLIQUITY = 23.439281

    fun calculateSunPosition(date: Date, latitude: Double, longitude: Double): SunPosition {
        val calendar = Calendar.getInstance().apply { time = date }
        
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val fracYear = 2 * PI * (dayOfYear - 1) / 365.0
        
        // Equation of time
        val eqTime = 229.18 * (0.000075 + 0.001868 * cos(fracYear) - 0.032077 * sin(fracYear) -
                0.014615 * cos(2 * fracYear) - 0.040849 * sin(2 * fracYear))

        // Solar declination
        val decl = 0.006918 - 0.399912 * cos(fracYear) + 0.070257 * sin(fracYear) -
                0.006758 * cos(2 * fracYear) + 0.000907 * sin(2 * fracYear)

        // Time offsets
        val timeOffset = eqTime + 4 * longitude
        val tst = calendar.get(Calendar.HOUR_OF_DAY) * 60.0 + calendar.get(Calendar.MINUTE) + timeOffset
        val solarTime = tst / 60.0

        // Hour angle
        val ha = (solarTime - 12) * 15

        // Convert latitude to radians
        val latRad = Math.toRadians(latitude)

        // Calculate sunrise and sunset
        val cosZenith = sin(latRad) * sin(decl) + cos(latRad) * cos(decl)
        val hourAngle = acos(-tan(latRad) * tan(decl))
        
        // Convert to hours
        val sunriseTime = 12 - hourAngle * 180 / (15 * PI) - timeOffset / 60
        val sunsetTime = 12 + hourAngle * 180 / (15 * PI) - timeOffset / 60

        return SunPosition(
            sunrise = formatTime(sunriseTime),
            sunset = formatTime(sunsetTime)
        )
    }

    private fun formatTime(decimalHours: Double): String {
        val hours = decimalHours.toInt()
        val minutes = ((decimalHours - hours) * 60).toInt()
        return String.format("%02d:%02d", hours, minutes)
    }

    data class SunPosition(
        val sunrise: String,
        val sunset: String
    )
}
