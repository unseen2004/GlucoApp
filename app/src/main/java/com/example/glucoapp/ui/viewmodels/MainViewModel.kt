package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _selectedScreen = MutableStateFlow<Screen>(Screen.Notes)
    val selectedScreen: StateFlow<Screen> = _selectedScreen

    fun checkCurrentUser() {
        // In a real app, you'd check for a stored user or token here
        viewModelScope.launch {
            // For now, assuming no user is logged in initially
        }
    }
    fun onScreenSelected(screen: Screen) {
        _selectedScreen.value = screen
    }

    fun logout() {
        // In a real app, you'd clear any stored user data or tokens here
        _currentUser.value = null
    }
}