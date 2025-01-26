package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.data.db.models.User
import com.example.glucoapp.data.db.models.Ingredient
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.Activity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.Inject

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

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    init {
        loadInsulinTypes()
        loadIngredients()
        loadActivities()
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

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            val currentUserId = 1 // TODO: Replace with actual logic to get the current user ID
            val user = repository.getUserById(currentUserId).firstOrNull()
            user?.let {
                val hashedPassword = hashPassword(newPassword)
                val updatedUser = it.copy(passwordHash = hashedPassword)
                repository.updateUser(updatedUser)
                _uiState.value = SettingsUiState.Success(updatedUser)
            }
        }
    }

    fun changeDoctorPassword(newPassword: String) {
        viewModelScope.launch {
            val currentUserId = 1 // TODO: Replace with actual logic to get the current user ID
            val user = repository.getUserById(currentUserId).firstOrNull()
            user?.let {
                val hashedPassword = hashPassword(newPassword)
                val updatedUser = it.copy(doctorPasswordHash = hashedPassword)
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

    fun addInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.insertInsulinType(insulinType)
            loadInsulinTypes()
        }
    }

    fun deleteInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.deleteInsulinType(insulinType)
            loadInsulinTypes()
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
            loadIngredients()
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredient)
            loadIngredients()
        }
    }

    fun addActivity(activity: Activity) {
        viewModelScope.launch {
            repository.insertActivity(activity)
            loadActivities()
        }
    }

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            repository.deleteActivity(activity)
            loadActivities()
        }
    }

    private fun loadInsulinTypes() {
        viewModelScope.launch {
            repository.getAllInsulinTypes().collect { insulinTypes ->
                _insulinTypes.value = insulinTypes
            }
        }
    }

    private fun loadIngredients() {
        viewModelScope.launch {
            repository.getAllIngredients().collect { ingredients ->
                _ingredients.value = ingredients
            }
        }
    }

    private fun loadActivities() {
        viewModelScope.launch {
            repository.getActivitiesByUserId(1).collect { activities ->
                _activities.value = activities
            }
        }
    }
}