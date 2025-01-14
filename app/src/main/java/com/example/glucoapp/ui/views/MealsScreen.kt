package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.MealsViewModel

@Composable
fun MealsScreen(
    navController: NavController,
    viewModel: MealsViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Meals Screen") // Placeholder content

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddMeal.route) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add Meal")
        }
    }
}