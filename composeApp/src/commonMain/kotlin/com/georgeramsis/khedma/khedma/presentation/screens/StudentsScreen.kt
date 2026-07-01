package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.georgeramsis.khedma.khedma.data.model.Student
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentViewModel
import khedma.composeapp.generated.resources.Res
import khedma.composeapp.generated.resources.class_title
import khedma.composeapp.generated.resources.search_students
import khedma.composeapp.generated.resources.st_birthdate
import khedma.composeapp.generated.resources.st_phone
import khedma.composeapp.generated.resources.stage_title
import khedma.composeapp.generated.resources.students_log
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs

@Composable
fun StudentScreen(
    authViewModel: AuthViewModel,
    viewModel: StudentViewModel = koinViewModel(), onStudentClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val permission by authViewModel.permission.collectAsState()
    val studentList by viewModel.studentList.collectAsState()
    val stageAndClassName by viewModel.classAndStage.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val profile by authViewModel.profile.collectAsState()
    val isJunior = profile?.role == "junior"

    LaunchedEffect(permission) {
        permission?.let { permission ->
            viewModel.loadClassStudents(permission)
        }
    }

    val filteredStudentList = studentList.filter { student ->
        student.firstName.contains(searchQuery, ignoreCase = true) ||
                student.lastName.contains(searchQuery, ignoreCase = true)
    }.sortedBy { it.firstName }
    Scaffold(
        floatingActionButton = {
            if (!isJunior) {
                FloatingActionButton(
                    onClick = { /* TODO: Implement add student functionality */ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp)) {
            ScreenTitle()
            Spacer(modifier = Modifier.height(8.dp))
            when (state) {
                is StudentState.Loading -> CircularProgressIndicator()

                is StudentState.Success -> {
                    SearchBar(searchQuery) {
                        searchQuery = it
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    stageAndClassName?.let { names ->
                        StageTitle(names.stageName, names.className)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    StudentScreenContent(
                        filteredStudentList, modifier = Modifier.weight(1F),
                        onStudentClick = onStudentClick
                    )
                }

                is StudentState.Idle -> {
                    Text(text = "No students found.")
                }

                is StudentState.Error -> {
                    val error = (state as StudentState.Error).errorMessage
                    Text(text = error)
                }
            }
        }
    }
}

@Composable
fun ScreenTitle() {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(Res.string.students_log),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(7.dp)
            )
        }
    }
}

@Composable
fun StageTitle(stageName: String, className: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(stringResource(Res.string.stage_title) + ": ")
                Text(stageName)
                Spacer(modifier = Modifier.weight(1F))
                if (className.isNotEmpty()) {
                    Text(stringResource(Res.string.class_title) + ": ")
                    Text(className, modifier = Modifier.weight(1F))
                }
            }


        }
    }
}

@Composable
fun StudentScreenContent(students: List<Student>, modifier: Modifier = Modifier, onStudentClick: (String) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(students) { student ->
            StudentCard(student, { student.id?.let { onStudentClick(it) } })
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(Res.string.search_students)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        singleLine = true,
        shape = RoundedCornerShape(percent = 50)
    )
}

@Composable
fun StudentCard(student: Student, onStudentClick: (String) -> Unit) {
    val avatarColors = listOf(
        Color(0xFF1976D2), // Blue
        Color(0xFF388E3C), // Green
        Color(0xFFD32F2F), // Red
        Color(0xFF7B1FA2), // Purple
        Color(0xFFF57C00), // Orange
        Color(0xFF0097A7), // Teal
    )
    val color = avatarColors[abs(student.firstName.hashCode()) % avatarColors.size]
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), onClick = {
        student.id?.let {
            onStudentClick(it)
        }
    }) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp)
                    .background(color, shape = RoundedCornerShape(100)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.firstName[0].toString(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(3.dp)
            ) {

                Text(
                    "${student.firstName} ${student.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                )

                Row {
                    Text(
                        stringResource(Res.string.st_birthdate) + ": ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        student.dateOfBirth,
                        modifier = Modifier.weight(1F),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    Text(stringResource(Res.string.st_phone) + ": ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        student.phone ?: "",
                        modifier = Modifier.weight(1F),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
