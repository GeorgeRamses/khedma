package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.georgeramsis.khedma.khedma.data.model.AppLanguage
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.SettingsViewModel
import khedma.composeapp.generated.resources.Res
import khedma.composeapp.generated.resources.language
import khedma.composeapp.generated.resources.logout
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(authViewModel: AuthViewModel, settingsViewModel: SettingsViewModel, onSignOut: () -> Unit) {
    val lang by settingsViewModel.language.collectAsState()
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val state by authViewModel.state.collectAsState()
    LaunchedEffect(state) {
        if (state is AuthState.Idle)
            onSignOut()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(Res.string.language))
        Row {
            RadioButton(selected = lang.code == "en", onClick = { settingsViewModel.setLanguage(AppLanguage.English) })
            Text("English")

            RadioButton(selected = lang.code == "ar", onClick = { settingsViewModel.setLanguage(AppLanguage.Arabic) })
            Text("العربية")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isDark, onCheckedChange = { settingsViewModel.setDarkMode(it) })
        }


        Spacer(Modifier.weight(1f))
        Button(onClick = { authViewModel.signOut() }) {
            Text(stringResource(Res.string.logout))
        }
    }
}