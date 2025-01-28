package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.UserPreferences
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    userPreferences: UserPreferences
) {
    val selectedScreen by viewModel.selectedScreen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Glucose Tracker") })
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { viewModel.onScreenSelected(Screen.Notes) },
                    modifier = Modifier.weight(1f),
                    content = {
                        Text(
                            "Notes",
                            color = if (selectedScreen == Screen.Notes) Color.Red else Color.White
                        )
                    }
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = { viewModel.onScreenSelected(Screen.Meals) },
                    modifier = Modifier.weight(1f),
                    content = {
                        Text(
                            "Meals",
                            color = if (selectedScreen == Screen.Meals) Color.Red else Color.White
                        )
                    }
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = { viewModel.onScreenSelected(Screen.Settings) },
                    modifier = Modifier.weight(1f),
                    content = {
                        Text(
                            "Settings",
                            color = if (selectedScreen == Screen.Settings) Color.Red else Color.White
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedScreen) {
                Screen.Notes -> NotesScreen(navController, userPreferences = userPreferences)
                Screen.Meals -> MealsScreen(navController, isDoctor = userPreferences.isDoctorLoggedIn.collectAsState(initial = false).value)
                Screen.Settings -> SettingsScreen(navController, isDoctor = userPreferences.isDoctorLoggedIn.collectAsState(initial = false).value)
                else -> {}
            }
        }
    }
}