package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navToStudents: () -> Unit,
    authViewModel: AuthViewModel = koinViewModel(),
    viewModel: StudentViewModel = koinViewModel()
) {
    val permission by viewModel.permission.collectAsState()
    val profile by authViewModel.profile.collectAsState()
    var classId: String by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.loadServantPermission()
    }
    LaunchedEffect(permission) {
        permission?.let {
            when {
                it.classId != null -> classId = it.classId
                it.stageId != null -> classId = it.stageId
                it.serviceType != null -> classId = it.serviceType
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, ${profile?.fullName ?: "..."}")
        Spacer(Modifier.height(30.dp))
        Button(onClick = {
            navToStudents()
        }, enabled = classId.isNotEmpty()) {
            Text("View students")
        }
    }
}