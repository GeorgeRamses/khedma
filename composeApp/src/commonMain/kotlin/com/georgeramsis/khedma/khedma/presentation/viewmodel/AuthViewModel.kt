package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgeramsis.khedma.khedma.data.model.Profile
import com.georgeramsis.khedma.khedma.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Unknown error")
                _profile.value = null
            }
        }
    }

    init {
        // Check if user is already signed in
        viewModelScope.launch {
            _state.value = AuthState.Checking
            repository.awaitReady()
            val currentProfile = repository.getProfile()
            if (currentProfile != null) {
                _profile.value = currentProfile
                _state.value = AuthState.Success
            } else {
                _state.value = AuthState.Idle
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _profile.value = null
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