package org.example.app.ui.astrology

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.example.app.R
import org.example.app.astrology.AstrologyService
import java.util.*

class AstrologyActivity : AppCompatActivity() {
    private lateinit var raasiText: TextView
    private lateinit var nakshatraText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var sunsetText: TextView
    
    private val astrologyService = AstrologyService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astrology)

        initViews()
        loadAstrologyDetails()
    }

    private fun initViews() {
        raasiText = findViewById(R.id.raasiText)
        nakshatraText = findViewById(R.id.nakshatraText)
        sunriseText = findViewById(R.id.sunriseText)
        sunsetText = findViewById(R.id.sunsetText)
    }

    private fun loadAstrologyDetails() {
        val details = astrologyService.getAstrologyDetails(Date())
        
        raasiText.text = details.raasi
        nakshatraText.text = details.nakshatra
        sunriseText.text = details.sunrise
        sunsetText.text = details.sunset
    }
}
