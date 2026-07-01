package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.ClassAndStageName
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _state = MutableStateFlow<StudentState>(StudentState.Idle)
    val state: StateFlow<StudentState> = _state.asStateFlow()

    private val _classAndStage = MutableStateFlow<ClassAndStageName?>(null)
    val classAndStage: StateFlow<ClassAndStageName?> = _classAndStage.asStateFlow()

    private val _studentList = MutableStateFlow<List<Student>>(emptyList())
    val studentList: StateFlow<List<Student>> = _studentList.asStateFlow()

    fun loadClassStudents(permission: ServantPermission) {
        viewModelScope.launch {
            _state.value = StudentState.Loading
            try {
                when {
                    permission.classId != null -> {
                        _studentList.value = repository.getStudentsByClass(permission.classId)
                        _classAndStage.value =
                            repository.getClassAndStageName(permission.classId)
                    }

                    permission.stageId != null -> {
                        _studentList.value = repository.getStudentsByStage(permission.stageId)
                        _classAndStage.value = repository.getStageName(permission.stageId)
                    }
                }
                if (_studentList.value.isNotEmpty()) {
                    _state.value = StudentState.Success

                } else {
                    _state.value = StudentState.Idle
                }
            } catch (e: Exception) {
                _state.value = StudentState.Error("Error loading students: ${e.message}")
            }
        }
    }
}

sealed class StudentState {
    object Idle : StudentState()
    object Loading : StudentState()
    object Success : StudentState()
    data class Error(val errorMessage: String) : StudentState()
}