package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.georgeramsis.khedma.khedma.presentation.viewmodel.LoadState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentDetailViewModel
import khedma.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailsScreen(
    studentId: String?,
    studentDetails: StudentDetailViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val currantStudent by studentDetails.student.collectAsState()
    val loadState by studentDetails.loadState.collectAsState()
    val isAddMode = studentId == null
    if (!isAddMode)
        LaunchedEffect(studentId) {
            studentId.let { studentDetails.loadStudentDetails(studentId = studentId) }
        }

    Scaffold(topBar = {
        TopAppBar(
            title = { /*Text("Student Details", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) */ },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val avatarColors = listOf(
                Color(0xFF1976D2), // Blue
                Color(0xFF388E3C), // Green
                Color(0xFFD32F2F), // Red
                Color(0xFF7B1FA2), // Purple
                Color(0xFFF57C00), // Orange
                Color(0xFF0097A7), // Teal
            )
            val color = avatarColors[abs(currantStudent?.firstName.hashCode()) % avatarColors.size]
            when (loadState) {
                is LoadState.Error -> {
                    val message = (loadState as LoadState.Error).message
                    Text(message)
                }

                LoadState.Idle -> {
                    Text(
                        "Student Not Found",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 10.dp)
                    )
                }

                LoadState.Loading -> {
                    Box(
                        modifier = Modifier.size(150.dp)
                            .background(color = color, shape = RoundedCornerShape(100)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 50.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                    currantStudent?.let { Text("Name", fontSize = 24.sp) }
                    CircularProgressIndicator()
                }

                LoadState.NotFound -> {
                    Text(
                        "Student Not Found",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 10.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    LoadDetails(studentDetails, color)
                }

                LoadState.Success -> {

                    //------------------load Student Details--------------------------------------------

                    LoadDetails(studentDetails, color)

                }
            }
        }
    }
}

@Composable
fun LoadDetails(studentDetails: StudentDetailViewModel, color: Color) {
    val currantStudent by studentDetails.student.collectAsState()
    val studentDetailsList: List<Triple<ImageVector, String, String>> = listOf(
        Triple(
            Icons.Default.Phone,
            "${stringResource(Res.string.st_phone)}: ",
            currantStudent?.phone ?: ""
        ),
        Triple(
            Icons.Default.MyLocation,
            "${stringResource(Res.string.st_address)}: ",
            currantStudent?.address ?: ""
        ),
        Triple(
            Icons.Default.CalendarMonth,
            "${stringResource(Res.string.st_birthdate)}: ",
            currantStudent?.dateOfBirth ?: ""
        ),
        Triple(
            Icons.Default.Email,
            "${stringResource(Res.string.st_email)}: ",
            currantStudent?.email ?: ""
        ),
        Triple(
            Icons.Default.Man4,
            "${stringResource(Res.string.class_title)}: ",
            currantStudent?.className ?: ""
        ),
        Triple(
            Icons.Default.School,
            "${stringResource(Res.string.stage_title)}: ",
            currantStudent?.stageName ?: ""
        ),
        Triple(
            Icons.Default.EditNote,
            "${stringResource(Res.string.st_notes)}: ",
            currantStudent?.notes ?: ""
        )
    )

    Box(
        modifier = Modifier.size(150.dp)
            .background(color = color, shape = RoundedCornerShape(100)),
        contentAlignment = Alignment.Center
    ) {
        val firstLitter = currantStudent?.firstName[0] ?: "?"
        Text(firstLitter.toString(), fontSize = 50.sp)
    }
    Spacer(Modifier.height(10.dp))
    currantStudent?.let { Text("${it.firstName} ${it.lastName}", fontSize = 24.sp) }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        items(studentDetailsList) { (icon, title, value) ->
            StudentDetailCard(icon, title, value)
        }
    }

}

@Composable
fun StudentDetailCard(icon: ImageVector? = null, title: String, value: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(7.dp), shape = RoundedCornerShape(10)
    ) {

        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            icon?.let { Icon(imageVector = it, contentDescription = title) }
            Spacer(Modifier.width(7.dp))
            Column {
                Text(title)
                Text(value)
            }

        }
    }
}