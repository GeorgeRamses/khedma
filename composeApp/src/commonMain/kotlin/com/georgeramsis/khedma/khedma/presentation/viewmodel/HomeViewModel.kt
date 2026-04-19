package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.AbsenceWithName
import com.georgeramsis.khedma.khedma.data.model.Activity
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException


class HomeViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _studentNeedVisit = MutableStateFlow<List<AbsenceWithName>>(emptyList())
    val studentNeedVisit: StateFlow<List<AbsenceWithName>> = _studentNeedVisit.asStateFlow()

    private val _upcomingBirthdays = MutableStateFlow<List<Student>>(emptyList())
    val upcomingBirthdays: StateFlow<List<Student>> = _upcomingBirthdays.asStateFlow()

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()


    fun loadHomeData(permission: ServantPermission) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {

                coroutineScope {
                    when {
                        permission.classId != null -> {
                            val snv = async { repository.getClassStudentsNeedingVisit(permission) }
                            val act = async { repository.getUpcomingActivities() }
                            val stu = repository.getStudentsByClass(permission.classId)
                            val absence = snv.await()
                            val studentMap = stu.associateBy { it.id }
                            val result = absence.mapNotNull { absence ->
                                val student = studentMap[absence.studentId]
                                student?.let {
                                    AbsenceWithName("${student.firstName} ${student.lastName}", absence.date)
                                }
                            }
                            _studentNeedVisit.value = result
                            _upcomingBirthdays.value = repository.getUpcomingBirthdays(stu)
                            _activities.value = act.await()
                        }

                        permission.stageId != null -> {
                            val snv = async { repository.getStageStudentsNeedingVisit(permission) }
                            val act = async { repository.getUpcomingActivities() }
                            val stu = repository.getStudentsByStage(permission.stageId)
                            val absence = snv.await()
                            val studentMap = stu.associateBy { it.id }
                            val result = absence.mapNotNull { absence ->
                                val student = studentMap[absence.studentId]
                                student?.let {
                                    AbsenceWithName("${student.firstName} ${student.lastName}", absence.date)
                                }
                            }
                            _studentNeedVisit.value = result
                            _upcomingBirthdays.value = repository.getUpcomingBirthdays(stu)
                            _activities.value = act.await()
                        }

                        else -> {
                            val act = async { repository.getUpcomingActivities() }
                            _activities.value = act.await()
                        }


                    }


                }
                _state.value = HomeState.Success
            } catch (e: Exception) {
                val message = when (e) {
                    is HttpRequestTimeoutException -> "Request timed out. Please check your internet connection and try again."
                    is IOException -> "Unable to connect to the server. Please check your internet connection and try again."
                    is RestException -> "Server error: ${e.statusCode}. Please try again later."
                    else -> "An unexpected error occurred. Please try again."
                }
                _state.value = HomeState.Error(message)
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