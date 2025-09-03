package org.example.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.app.data.models.CalendarEvent
import org.example.app.data.repository.CalendarRepository
import java.util.*

/**
 * ViewModel for calendar screen
 */
class CalendarViewModel(private val repository: CalendarRepository) : ViewModel() {
    
    private val _currentMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH))
    private val _currentYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    
    val events = combine(_currentMonth, _currentYear) { month, year ->
        repository.getEvents(month, year)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setMonth(month: Int, year: Int) {
        _currentMonth.value = month
        _currentYear.value = year
    }

    fun addEvent(event: CalendarEvent) = viewModelScope.launch {
        repository.addEvent(event)
    }

    class Factory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
