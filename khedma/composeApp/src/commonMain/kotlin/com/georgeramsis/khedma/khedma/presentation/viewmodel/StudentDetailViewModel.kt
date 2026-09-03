package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.data.model.StudentDetails
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentDetailViewModel(val repository: StudentRepository) : ViewModel() {
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    private val _student = MutableStateFlow<StudentDetails?>(null)
    val student: StateFlow<StudentDetails?> = _student.asStateFlow()

    fun loadStudentDetails(studentId: String) {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            try {
                val result = repository.getStudentDetails(studentId)

                if (result != null)
                    _loadState.value = LoadState.Success
                else
                    _loadState.value = LoadState.NotFound
                _student.value = result
            } catch (e: Exception) {
                _loadState.value = LoadState.Error("Error loading student: ${e.message}")
            }
        }
    }
}

sealed class LoadState {
    object Idle : LoadState()
    object Loading : LoadState()
    object Success : LoadState()
    object NotFound : LoadState()
    data class Error(val message: String) : LoadState()
}

sealed class SaveState {
    object Idle : SaveState()
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}