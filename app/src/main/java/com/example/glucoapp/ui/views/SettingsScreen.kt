package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser() // Load the current user when the screen is shown
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings Screen")

        when (val state = uiState) {
            is SettingsUiState.Loading -> {
                // Show loading indicator
                Text("Loading...")
            }
            is SettingsUiState.Success -> {
                // Display and edit settings
                SettingsContent(
                    user = state.user,
                    onSaveSettings = { updatedUser ->
                        viewModel.updateUser(updatedUser)
                    }
                )
            }
            is SettingsUiState.Error -> {
                // Show error message
                Text("Error: ${state.message}")
            }
        }
    }
}

@Composable
fun SettingsContent(user: User, onSaveSettings: (User) -> Unit) {
    var username by remember { mutableStateOf(user.username) }
    // Add more settings fields as needed

    Column {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxSize()
        )
        // Add more settings fields here

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                val updatedUser = user.copy(username = username) // Update other fields as needed
                onSaveSettings(updatedUser)
            }
        ) {
            Text("Save Settings")
        }
    }
}

// UI State for the Settings Screen
sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val user: User) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}