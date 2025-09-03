package org.example.app.ui.preferences

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import java.text.DateFormat
import java.util.*

class TimePickerPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {
    private var timeInMinutes: Int = 0 // Store as minutes since midnight

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getString(index) ?: "480" // Default to 8:00 AM
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        val default = (defaultValue as? String)?.toIntOrNull() ?: 480
        timeInMinutes = getPersistedInt(default)
        updateSummary()
    }

    private fun updateSummary() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timeInMinutes / 60)
            set(Calendar.MINUTE, timeInMinutes % 60)
        }
        summary = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
    }

    fun getTime(): Int = timeInMinutes

    fun setTime(minutes: Int) {
        timeInMinutes = minutes
        persistInt(minutes)
        updateSummary()
    }
}
