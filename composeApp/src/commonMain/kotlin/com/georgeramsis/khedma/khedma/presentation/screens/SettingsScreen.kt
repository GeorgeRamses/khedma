package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen( authViewModel: AuthViewModel ,onSignOut: () -> Unit) {
    val state by authViewModel.state.collectAsState()
    LaunchedEffect(state) {
        if (state is AuthState.Idle)
            onSignOut()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings")
        Button(onClick = { authViewModel.signOut() }) {
            Text("Logout")
        }
    }
}