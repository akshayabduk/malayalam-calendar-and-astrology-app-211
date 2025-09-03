package org.example.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.example.app.data.repository.CalendarRepository
import java.util.*

/**
 * ViewModel for astrology screen
 */
class AstrologyViewModel(private val repository: CalendarRepository) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(Date())

    val astrologyDetails = _selectedDate
        .flatMapLatest { date ->
            repository.getAstrologyDetails(date)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun setDate(date: Date) {
        _selectedDate.value = date
    }

    class Factory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AstrologyViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AstrologyViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
