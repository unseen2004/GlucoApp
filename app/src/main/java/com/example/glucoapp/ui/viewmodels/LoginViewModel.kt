package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.models.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(user: User) {
        // Basic implementation - accepts any login
        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(user.username).firstOrNull()
            if (existingUser != null) {
                _loginState.value = LoginState.Success
            } else {
                // Handle login failure if needed
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        }
    }

    fun register(user: User) {
        // Basic implementation - always succeeds
        viewModelScope.launch {
            repository.insertUser(user)
            _loginState.value = LoginState.Success
        }
    }
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}