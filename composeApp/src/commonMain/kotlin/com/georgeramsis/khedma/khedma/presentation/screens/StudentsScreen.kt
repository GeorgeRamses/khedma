package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentScreen(viewModel: StudentViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val permission by viewModel.permission.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadServantPermission()
    }
    LaunchedEffect(permission) {
        permission?.let { perm ->
            perm.classId?.let { classId ->
                viewModel.getStudentByClass(classId)
            }

        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        StudentScreenTitle()
        when (state) {
            is StudentState.Loading -> CircularProgressIndicator()

            is StudentState.Success -> {
                val students = (state as StudentState.Success).students
                StudentScreenContent(students)
            }

            is StudentState.Error -> {
                val error = (state as StudentState.Error).errorMessage
                Text(text = error)
            }

            else -> {}
        }
    }
}

@Composable
fun StudentScreenTitle() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Stage", modifier = Modifier.weight(1F))
        Text("Class Name", modifier = Modifier.weight(1F))
    }
}

@Composable
fun StudentScreenContent(students: List<Student>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(students) { student ->
            StudentCard(student)
        }
    }
}

@Composable
fun StudentCard(student: Student) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("${student.firstName} ${student.lastName}", modifier = Modifier.weight(1F))
        Text(student.dateOfBirth)
        student.phone?.let { Text(it) }
        IconButton(onClick = { }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Button")
        }
    }
}