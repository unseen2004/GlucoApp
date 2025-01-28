package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.MealsViewModel
import com.example.glucoapp.data.db.models.Meal

@Composable
fun MealsScreen(
    navController: NavController,
    viewModel: MealsViewModel = hiltViewModel(),
    isDoctor: Boolean = false
) {
    val userId by viewModel.userId.collectAsState(initial = null)
    val meals by viewModel.meals.collectAsState()

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadMealsByUserId(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(meals) { meal ->
                MealItem(meal = meal, onDeleteClick = {
                    if (!isDoctor) {
                        viewModel.deleteMeal(meal)
                    }
                })
            }
        }
        if (!isDoctor) {
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
}

@Composable
fun MealItem(meal: Meal, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = meal.foodName ?: "Unknown Meal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.padding(4.dp))

            if (meal.protein != null) {
                Text(text = "Protein: ${meal.protein} g")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (meal.carbs != null) {
                Text(text = "Carbs: ${meal.carbs} g")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (meal.fat != null) {
                Text(text = "Fat: ${meal.fat} g")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (meal.kcal != null) {
                Text(text = "Kcal: ${meal.kcal}")
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}