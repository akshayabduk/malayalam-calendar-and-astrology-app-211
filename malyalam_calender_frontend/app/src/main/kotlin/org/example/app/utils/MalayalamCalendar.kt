package org.example.app.utils

import java.util.*

/**
 * Utility class for Malayalam calendar calculations
 */
object MalayalamCalendar {
    private val malayalamMonths = arrayOf(
        "Chingam", "Kanni", "Thulam", "Vrischikam", 
        "Dhanu", "Makaram", "Kumbham", "Meenam",
        "Medam", "Edavam", "Midhunam", "Karkidakam"
    )

    private val kollam = 1197 // Current Kollam era year (2022-2023)
    private val monthOffset = 4 // Chingam starts in August

    fun getMonthName(gregorianMonth: Int, gregorianYear: Int): String {
        val adjustedMonth = (gregorianMonth + monthOffset) % 12
        return malayalamMonths[adjustedMonth]
    }

    fun getMalayalamYear(gregorianYear: Int, gregorianMonth: Int): Int {
        return if (gregorianMonth >= 8) {
            kollam + (gregorianYear - 2022)
        } else {
            kollam + (gregorianYear - 2023)
        }
    }

    fun formatMalayalamDate(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val malayalamMonth = getMonthName(month, year)
        val malayalamYear = getMalayalamYear(year, month)

        return "$day $malayalamMonth, ME $malayalamYear"
    }
}
