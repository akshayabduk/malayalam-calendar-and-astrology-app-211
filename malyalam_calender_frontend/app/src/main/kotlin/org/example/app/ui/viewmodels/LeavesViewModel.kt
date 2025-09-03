package org.example.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.app.data.models.Leave
import org.example.app.data.repository.CalendarRepository

/**
 * ViewModel for leaves management screen
 */
class LeavesViewModel(private val repository: CalendarRepository) : ViewModel() {
    
    val leaves = repository.getLeaves()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _searchQuery = MutableStateFlow("")
    
    val filteredLeaves = combine(leaves, _searchQuery) { leaves, query ->
        if (query.isEmpty()) {
            leaves
        } else {
            leaves.filter { leave ->
                leave.title.contains(query, ignoreCase = true) ||
                leave.description?.contains(query, ignoreCase = true) == true
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addLeave(leave: Leave) = viewModelScope.launch {
        repository.addLeave(leave)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    class Factory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LeavesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LeavesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
