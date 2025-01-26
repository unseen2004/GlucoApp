package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.glucoapp.data.db.models.Note
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.NoteViewModel
import kotlinx.coroutines.launch
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.Ingredient
import com.example.glucoapp.data.db.models.InsulinType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var insulinAmount by remember { mutableStateOf("") }
    var ww by remember { mutableStateOf("") }
    var wbt by remember { mutableStateOf("") }

    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }
    var selectedActivityId by remember { mutableStateOf<Int?>(null) }
    var selectedInsulinType by remember { mutableStateOf<InsulinType?>(null) }
    var insulinTypeExpanded by remember { mutableStateOf(false) }

    var showMealSelectionDialog by remember { mutableStateOf(false) }
    var showPredefinedMealSelectionDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }

    var activityName by remember { mutableStateOf("") }
    var activityDuration by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    val meals by viewModel.meals.collectAsState()
    val predefinedMeals by viewModel.predefinedMeals.collectAsState()
    val insulinTypes by viewModel.insulinTypes.collectAsState()

    LaunchedEffect(Unit) {
        if (insulinTypes.isEmpty()) {
            viewModel.loadInsulinTypes()
        }
    }

    LaunchedEffect(key1 = selectedMeal) {
        selectedMeal?.let { meal ->
            ww = ((meal.carbs ?: 0.0) / 10).toString()
        }
    }

    LaunchedEffect(key1 = selectedIngredient) {
        selectedIngredient?.let { predefinedMeal ->
            ww = ((predefinedMeal.carbs ?: 0.0) / 10).toString()
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
                            timestamp = System.currentTimeMillis(),
                            glucoseLevel = glucoseLevel.toIntOrNull(),
                            InsulinAmount = insulinAmount.toDoubleOrNull(),
                            insulinTypeId = selectedInsulinType?.typeId,
                            noteText = noteText,
                            WW = ww.toDoubleOrNull(),
                            WBT = wbt.toDoubleOrNull(),
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

            OutlinedTextField(
                value = insulinAmount,
                onValueChange = { insulinAmount = it },
                label = { Text("Insulin Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ww,
                onValueChange = { ww = it },
                label = { Text("WW") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = wbt,
                onValueChange = { wbt = it },
                label = { Text("WBT") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showActivityDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Activity")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.loadMealsByUserId(1) // TODO: Replace with actual user ID
                    showMealSelectionDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Meal from Database")
            }

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

            if (showMealSelectionDialog) {
                AlertDialog(
                    onDismissRequest = { showMealSelectionDialog = false },
                    title = { Text("Select Meal") },
                    text = {
                        LazyColumn {
                            items(meals) { meal ->
                                Button(onClick = {
                                    selectedMeal = meal
                                    selectedIngredient = null
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
                                    selectedIngredient = meal
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