package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.Activity
import com.georgeramsis.khedma.khedma.data.model.Attendance
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import com.georgeramsis.khedma.khedma.data.repository.StudentVisitNeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _studentNeedVisit = MutableStateFlow<List<StudentVisitNeed>>(emptyList())
    val studentNeedVisit: StateFlow<List<StudentVisitNeed>> = _studentNeedVisit.asStateFlow()

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    fun loadStudentsNeedingVisit(classId: String) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                _studentNeedVisit.value = repository.getStudentsNeedingVisit(classId)
                _state.value = HomeState.Success
            } catch (e: Exception) {
                _state.value = HomeState.Error(e.message ?: "Unknown error")
            }

        }
    }

    fun loadUpcomingActivities() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                _activities.value = repository.getUpcomingActivities()
                _state.value = HomeState.Success
            } catch (e: Exception) {
                _state.value = HomeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    object Success : HomeState()
    data class Error(val errorMessage: String) : HomeState()
}