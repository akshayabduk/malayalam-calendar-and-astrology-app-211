package org.example.app.astrology

import org.example.app.data.models.AstrologyDetails
import org.example.app.utils.AstronomicalCalculator
import java.util.*

/**
 * Service for astrology-related calculations
 */
class AstrologyService {
    companion object {
        private val DEFAULT_LATITUDE = 10.8505
        private val DEFAULT_LONGITUDE = 76.2711  // Coordinates for Kerala

        private val NAKSHATRAS = arrayOf(
            "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra",
            "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni",
            "Uttara Phalguni", "Hasta", "Chitra", "Swati", "Vishakha",
            "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha", "Uttara Ashadha",
            "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada",
            "Uttara Bhadrapada", "Revati"
        )

        private val RAASIS = arrayOf(
            "Mesha", "Vrishabha", "Mithuna", "Karka", "Simha", "Kanya",
            "Tula", "Vrischika", "Dhanus", "Makara", "Kumbha", "Meena"
        )
    }

    fun getAstrologyDetails(date: Date): AstrologyDetails {
        // Calculate sun position
        val sunPosition = AstronomicalCalculator.calculateSunPosition(
            date,
            DEFAULT_LATITUDE,
            DEFAULT_LONGITUDE
        )

        // Calculate nakshatra (simplified)
        val calendar = Calendar.getInstance().apply { time = date }
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val nakshatraIndex = (dayOfYear % NAKSHATRAS.size)
        
        // Calculate raasi (simplified)
        val raashiIndex = (dayOfYear % RAASIS.size)

        return AstrologyDetails(
            date = date,
            raasi = RAASIS[raashiIndex],
            nakshatra = NAKSHATRAS[nakshatraIndex],
            sunrise = sunPosition.sunrise,
            sunset = sunPosition.sunset
        )
    }
}
