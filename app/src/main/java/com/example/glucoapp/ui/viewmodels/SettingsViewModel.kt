package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.glucoapp.ui.views.SettingsUiState

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                // Assuming you have a way to get the current user ID, e.g., from shared preferences
                val currentUserId = 1 // Replace with actual logic to get the current user ID

                repository.getUserById(currentUserId).collect { user ->
                    if (user != null) {
                        _uiState.value = SettingsUiState.Success(user)
                    } else {
                        _uiState.value = SettingsUiState.Error("User not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SettingsUiState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }


    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            // Optionally reload user data or just update the UI state
            _uiState.value = SettingsUiState.Success(user)
        }
    }

    // Add functions to get/set specific settings (e.g., units, theme, language)
    // ...
}