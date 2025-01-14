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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.presentation.views.NotesScreen
import com.example.glucoapp.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Notes) }

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
                    onClick = { selectedScreen = Screen.Notes },
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
                    onClick = { selectedScreen = Screen.Meals },
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
                    onClick = { selectedScreen = Screen.Settings },
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
                Screen.Notes -> NotesScreen(navController)
                Screen.Meals -> MealsScreen(navController)
                Screen.Settings -> SettingsScreen(navController)
                else -> {}
            }
        }
    }
}