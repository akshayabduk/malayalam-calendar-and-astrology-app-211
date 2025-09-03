package org.example.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.example.app.R
import org.example.app.analytics.AnalyticsManager
import org.example.app.data.repository.CalendarRepository
import org.example.app.data.repository.MockCalendarRepository
import org.example.app.utils.ErrorHandler
import java.util.*

class AstrologyFragment : Fragment() {
    private lateinit var raasiText: TextView
    private lateinit var nakshatraText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var sunsetText: TextView
    private lateinit var analytics: AnalyticsManager
    private val repository: CalendarRepository = MockCalendarRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = AnalyticsManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_astrology, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        raasiText = view.findViewById(R.id.raasiText)
        nakshatraText = view.findViewById(R.id.nakshatraText)
        sunriseText = view.findViewById(R.id.sunriseText)
        sunsetText = view.findViewById(R.id.sunsetText)

        try {
            updateAstrologyDetails()
            analytics.logEvent(getString(R.string.analytics_event_view_astrology))
        } catch (e: Exception) {
            ErrorHandler.handleError(
                requireContext(),
                view,
                e
            ) { updateAstrologyDetails() }
        }
    }

    private fun updateAstrologyDetails() {
        val details = repository.getAstrologyDetails(Date())
        
        raasiText.text = details.raasi
        nakshatraText.text = details.nakshatra
        sunriseText.text = details.sunrise
        sunsetText.text = details.sunset
    }
}
