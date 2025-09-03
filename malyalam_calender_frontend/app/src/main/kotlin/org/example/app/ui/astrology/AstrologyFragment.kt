package org.example.app.ui.astrology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.astrology.AstrologyService
import java.text.SimpleDateFormat
import java.util.*

class AstrologyFragment : Fragment() {
    private lateinit var astrologyService: AstrologyService
    private lateinit var dateText: TextView
    private lateinit var raasiText: TextView
    private lateinit var nakshatraText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var sunsetText: TextView

    private var selectedDate = Date()
    private val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_astrology, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        astrologyService = AstrologyService.getInstance(requireContext())

        dateText = view.findViewById(R.id.dateText)
        raasiText = view.findViewById(R.id.raasiText)
        nakshatraText = view.findViewById(R.id.nakshatraText)
        sunriseText = view.findViewById(R.id.sunriseText)
        sunsetText = view.findViewById(R.id.sunsetText)

        // Show today's date by default
        if (arguments?.getBoolean("show_today", false) == true) {
            selectedDate = Date()
            updateAstrologyDetails()
        }

        view.findViewById<View>(R.id.dateLayout).setOnClickListener {
            showDatePicker()
        }

        updateDateDisplay()
        updateAstrologyDetails()
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(selectedDate.time)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = Date(selection)
            updateDateDisplay()
            updateAstrologyDetails()
        }

        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun updateDateDisplay() {
        dateText.text = dateFormat.format(selectedDate)
    }

    private fun updateAstrologyDetails() {
        lifecycleScope.launch {
            try {
                val details = astrologyService.getAstrologyDetails(selectedDate)
                
                raasiText.text = details.raasi
                nakshatraText.text = details.nakshatra
                sunriseText.text = details.sunrise
                sunsetText.text = details.sunset
            } catch (e: Exception) {
                // Show error state
                raasiText.text = getString(R.string.no_astrology_data)
                nakshatraText.text = ""
                sunriseText.text = ""
                sunsetText.text = ""
            }
        }
    }
}
