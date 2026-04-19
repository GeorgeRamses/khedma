package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.Profile
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import com.georgeramsis.khedma.khedma.data.repository.AuthRepository
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Checking)
    val state: StateFlow<AuthState> = _state.asStateFlow()
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                repository.signIn(email, password)
                _profile.value = repository.getProfile()
                _permission.value = repository.getServantClass()
                _state.value = AuthState.Success
            } catch (e: Exception) {
                val message = when (e) {
                    is HttpRequestTimeoutException -> "Request timed out. Please check your internet connection and try again."
                    is IOException -> "Unable to connect to the server. Please check your internet connection and try again."
                    is RestException -> when {
                        e.message?.contains("Invalid login credentials") == true -> "Invalid email or password."
                        e.message?.contains("Email not confirmed") == true -> "Please verify your email first."
                        e.message?.contains("Email rate limit exceeded") == true -> "Too many attempts. Please wait and try again."
                        else -> "Something went wrong. Please try again."
                    }

                    else -> "An unexpected error occurred. Please try again."
                }
                _state.value = AuthState.Error(message)
                _profile.value = null
                _permission.value = null
            }
        }
    }

    private val _permission = MutableStateFlow<ServantPermission?>(null)
    val permission: StateFlow<ServantPermission?> = _permission.asStateFlow()

    init {
        // Check if user is already signed in
        viewModelScope.launch {
            try {
                _state.value = AuthState.Checking
                repository.awaitReady()
                val currentProfile = repository.getProfile()
                if (currentProfile != null) {
                    _profile.value = currentProfile

                    val result = repository.getServantClass()
                    println("Permission result: $result")
                    _permission.value = result
                    _state.value = AuthState.Success
                } else {
                    _state.value = AuthState.Idle
                    _permission.value = null
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error("${e.message}")
                _permission.value = null
                println("Permission error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _profile.value = null
            _permission.value = null
            _state.value = AuthState.Idle
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Checking : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}