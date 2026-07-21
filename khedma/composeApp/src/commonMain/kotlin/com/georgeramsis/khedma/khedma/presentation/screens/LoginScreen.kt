package com.georgeramsis.khedma.khedma.presentation.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, viewModel: AuthViewModel = koinViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state) {

        if (state is AuthState.Success) {
            onLoginSuccess()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email, onValueChange = { email = it }, label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
            modifier = Modifier.onPreviewKeyEvent { event ->
                if (event.key == Key.Tab && event.type == KeyEventType.KeyDown) {
                    passwordFocusRequester.requestFocus()
                    true
                } else false
            }
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.signIn(email, password) }),
            modifier = Modifier.focusRequester(passwordFocusRequester).onPreviewKeyEvent { event ->
                if ((event.key == Key.NumPadEnter || event.key == Key.Enter) && event.type == KeyEventType.KeyDown) {
                    viewModel.signIn(email, password)
                    true
                } else false
            }
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            viewModel.signIn(email, password)
        }) {
            Text("Login")
        }
        Spacer(Modifier.height(8.dp))

        when (state) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Error -> {
                val error = (state as AuthState.Error).message
                Text(text = error)
            }

            else -> {}
        }
    }
}