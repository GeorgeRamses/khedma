package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _state = MutableStateFlow<StudentState>(StudentState.Idle)
    val state: StateFlow<StudentState> = _state.asStateFlow()

    fun getStudentByClass(classId: String) {
        viewModelScope.launch {
            _state.value = StudentState.Loading
            try {
                val students = repository.getStudentsByClass(classId)
                _state.value = StudentState.Success(students)
            } catch (e: Exception) {
                _state.value = StudentState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class StudentState {
    object Idle : StudentState()
    object Loading : StudentState()
    data class Success(val students: List<Student>) : StudentState()
    data class Error(val errorMessage: String) : StudentState()
}