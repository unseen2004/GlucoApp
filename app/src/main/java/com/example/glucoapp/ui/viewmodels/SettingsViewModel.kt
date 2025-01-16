package com.example.glucoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.data.db.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.PredefinedMeal
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val user: User) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _insulinTypes = MutableStateFlow<List<InsulinType>>(emptyList())
    val insulinTypes: StateFlow<List<InsulinType>> = _insulinTypes.asStateFlow()

    private val _predefinedMeals = MutableStateFlow<List<PredefinedMeal>>(emptyList())
    val predefinedMeals: StateFlow<List<PredefinedMeal>> = _predefinedMeals.asStateFlow()

    init {
        loadInsulinTypes()
        loadPredefinedMeals()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val currentUserId = 1 // TODO: Replace with actual logic to get the current user ID
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
            _uiState.value = SettingsUiState.Success(user)
        }
    }

    private fun loadInsulinTypes() {
        viewModelScope.launch {
            repository.getAllInsulinTypes().collect { types ->
                _insulinTypes.value = types
            }
        }
    }

    private fun loadPredefinedMeals() {
        viewModelScope.launch {
            repository.getAllPredefinedMeals().collect { meals ->
                _predefinedMeals.value = meals
            }
        }
    }

    fun insertInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.insertInsulinType(insulinType)
            loadInsulinTypes() // Reload the list after insertion
        }
    }

    fun deleteInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.deleteInsulinType(insulinType)
            loadInsulinTypes() // Reload the list after deletion
        }
    }

    fun insertPredefinedMeal(meal: PredefinedMeal) {
        viewModelScope.launch {
            repository.insertPredefinedMeal(meal)
            loadPredefinedMeals() // Reload the list after insertion
        }
    }

    fun deletePredefinedMeal(meal: PredefinedMeal) {
        viewModelScope.launch {
            repository.deletePredefinedMeal(meal)
            loadPredefinedMeals() // Reload the list after deletion
        }
    }
    fun updateLogin(userId: Int, newUsername: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId).firstOrNull()
            user?.let {
                val updatedUser = it.copy(username = newUsername)
                repository.updateUser(updatedUser)
                _uiState.value = SettingsUiState.Success(updatedUser)
            }
        }
    }
    fun updatePassword(userId: Int, newPassword: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId).firstOrNull()
            user?.let {
                val hashedNewPassword = hashPassword(newPassword) // Hash the new password
                val updatedUser = it.copy(passwordHash = hashedNewPassword)
                repository.updateUser(updatedUser)
                _uiState.value = SettingsUiState.Success(updatedUser)
            }
        }
    }

    fun updateDoctorPassword(userId: Int, newDoctorPassword: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId).firstOrNull()
            user?.let {
                val hashedNewDoctorPassword = hashPassword(newDoctorPassword) // Hash the new doctor password
                val updatedUser = it.copy(doctorPasswordHash = hashedNewDoctorPassword)
                repository.updateUser(updatedUser)
                _uiState.value = SettingsUiState.Success(updatedUser)
            }
        }
    }
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
