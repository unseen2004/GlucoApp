package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.db.models.Note
import com.example.glucoapp.navigation.Screen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.PredefinedMeal
import com.example.glucoapp.ui.viewmodels.NoteViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.MaterialTheme
import com.example.glucoapp.data.db.models.InsulinType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    // State variables for the Note fields
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var insulinTaken by remember { mutableStateOf<Boolean?>(null) } // Yes/No for insulin taken
    var sugar by remember { mutableStateOf("") }
    var carboExch by remember { mutableStateOf("") }

    // State variables for related entities
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var selectedPredefinedMeal by remember { mutableStateOf<PredefinedMeal?>(null) }
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }
    var selectedActivityId by remember { mutableStateOf<Int?>(null) }
    // State variables for dialog visibility
    var showMealSelectionDialog by remember { mutableStateOf(false) }
    var showPredefinedMealSelectionDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }

    // State variables for the Activity fields
    var activityName by remember { mutableStateOf("") }
    var activityDuration by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    // Collect StateFlows from ViewModel
    val meals by viewModel.meals.collectAsState()
    val predefinedMeals by viewModel.predefinedMeals.collectAsState()

    var selectedInsulinType by remember { mutableStateOf<InsulinType?>(null) }
    var insulinTypeExpanded by remember { mutableStateOf(false) }

    // Collect insulin types from the ViewModel
    val insulinTypes by viewModel.insulinTypes.collectAsState()

    // Load insulin types when the screen is first shown
    LaunchedEffect(Unit) {
        if (insulinTypes.isEmpty()) {
            viewModel.loadInsulinTypes()
        }
    }

    // Auto-calculate carboExch when a meal is selected
    LaunchedEffect(key1 = selectedMeal) {
        selectedMeal?.let { meal ->
            carboExch = ((meal.carbs ?: 0.0) / 10).toString()
        }
    }

    LaunchedEffect(key1 = selectedPredefinedMeal) {
        selectedPredefinedMeal?.let { predefinedMeal ->
            carboExch = ((predefinedMeal.carbs ?: 0.0) / 10).toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Main.route) }) {
                        Icon(Icons.Filled.Close, "Cancel")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val newNote = Note(
                            userId = 1, // TODO: Replace with actual user ID
                            timestamp = System.currentTimeMillis(), // Current timestamp
                            glucoseLevel = glucoseLevel.toDoubleOrNull() ?: 0.0,
                            insulinTypeId = selectedInsulinType?.typeId,
                            noteText = noteText,
                            sugar = sugar.toDoubleOrNull() ?: 0.0,
                            carboExch = carboExch.toDoubleOrNull() ?: 0.0,
                            mealId = selectedMeal?.mealId,
                            activityId = selectedActivityId
                        )
                        viewModel.insertNote(newNote)
                        navController.navigate(Screen.Main.route)
                    }) {
                        Icon(Icons.Filled.Check, "Save")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Note Text") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = glucoseLevel,
                onValueChange = { glucoseLevel = it },
                label = { Text("Glucose Level") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

// Insulin Type Selection
            ExposedDropdownMenuBox(
                expanded = insulinTypeExpanded,
                onExpandedChange = { insulinTypeExpanded = !insulinTypeExpanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedInsulinType?.typeName ?: "",
                    onValueChange = {},
                    label = { Text("Insulin Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = insulinTypeExpanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = insulinTypeExpanded,
                    onDismissRequest = { insulinTypeExpanded = false },
                ) {
                    insulinTypes.forEach { insulinType ->
                        DropdownMenuItem(
                            onClick = {
                                selectedInsulinType = insulinType
                                insulinTypeExpanded = false
                            },
                            text = { Text(insulinType.typeName) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))


            // Sugar Field
            OutlinedTextField(
                value = sugar,
                onValueChange = { sugar = it },
                label = { Text("Sugar") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Carbohydrate Exchange Field (Read-only, auto-calculated)
            OutlinedTextField(
                value = carboExch,
                onValueChange = { carboExch = it },
                label = { Text("Carbohydrate Exchange") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Button to Add Activity
            Button(
                onClick = { showActivityDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Activity")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Button to Select Meal from Database
            Button(
                onClick = {
                    viewModel.loadMealsByUserId(1) // TODO: Replace with actual user ID
                    showMealSelectionDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Meal from Database")
            }

            // Button to Select Predefined Meal
            Button(
                onClick = {
                    viewModel.loadAllPredefinedMeals()
                    showPredefinedMealSelectionDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Predefined Meal")
            }

            // Activity Dialog
            if (showActivityDialog) {
                AlertDialog(
                    onDismissRequest = { showActivityDialog = false },
                    title = { Text("Add Activity") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = activityName,
                                onValueChange = { activityName = it },
                                label = { Text("Activity Name") }
                            )
                            OutlinedTextField(
                                value = activityDuration,
                                onValueChange = { activityDuration = it },
                                label = { Text("Duration (in minutes)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = activityNotes,
                                onValueChange = { activityNotes = it },
                                label = { Text("Notes") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.viewModelScope.launch {
                                val newActivity = Activity(
                                    userId = 1, // TODO: Replace with actual user ID
                                    timestamp = System.currentTimeMillis(),
                                    activityType = activityName,
                                    duration = activityDuration.toIntOrNull() ?: 0,
                                    notes = activityNotes
                                )
                                // Access repository through viewModel
                                viewModel.insertActivity(newActivity) { newId ->
                                    selectedActivityId = newId
                                }
                                showActivityDialog = false
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showActivityDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Dialogs for Meal and Predefined Meal Selection
            if (showMealSelectionDialog) {
                AlertDialog(
                    onDismissRequest = { showMealSelectionDialog = false },
                    title = { Text("Select Meal") },
                    text = {
                        LazyColumn {
                            items(meals) { meal ->
                                Button(onClick = {
                                    selectedMeal = meal
                                    selectedPredefinedMeal = null
                                    showMealSelectionDialog = false
                                }) {
                                    Text(meal.foodName ?: "Unknown Meal")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showMealSelectionDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showPredefinedMealSelectionDialog) {
                AlertDialog(
                    onDismissRequest = { showPredefinedMealSelectionDialog = false },
                    title = { Text("Select Predefined Meal") },
                    text = {
                        LazyColumn {
                            items(predefinedMeals) { meal ->
                                Button(onClick = {
                                    selectedPredefinedMeal = meal
                                    selectedMeal = null
                                    showPredefinedMealSelectionDialog = false
                                }) {
                                    Text(meal.foodName)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showPredefinedMealSelectionDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}