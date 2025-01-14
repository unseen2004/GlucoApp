package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            // Fetch the first user from the database
            val existingUser = repository.getUserById(1).firstOrNull()

            if (existingUser == null) {
                // Insert a default user if none exists
                val defaultUser = User(
                    userId = 1, // You might want to generate this dynamically later
                    username = "testuser",
                    passwordHash = "hash", // Replace with actual password hashing
                    doctorPasswordHash = "hash", // Replace with actual password hashing
                    accessLevel = 0,
                    glucoseUnit = 0
                )
                repository.insertUser(defaultUser)
                _user.value = defaultUser
            } else {
                // User found, set it as the current user
                _user.value = existingUser
            }
        }
    }

    fun setUser(userId: Int) {
        viewModelScope.launch {
            repository.getUserById(userId).collect { user ->
                _user.value = user
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
}
