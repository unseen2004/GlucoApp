package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.Ingredient
import com.example.glucoapp.ui.viewmodels.NoteViewModel
import kotlinx.coroutines.launch
import com.example.glucoapp.data.db.models.InsulinType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var insulinAmount by remember { mutableStateOf("") }
    var WW by remember { mutableStateOf("") }
    var WBT by remember { mutableStateOf("") }
    var selectedInsulinType by remember { mutableStateOf<InsulinType?>(null) }
    var insulinTypeExpanded by remember { mutableStateOf(false) }
    var showMealSelectionDialog by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }
    var activityExpanded by remember { mutableStateOf(false) }

    val insulinTypes by viewModel.insulinTypes.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val activities by viewModel.activities.collectAsState()

    LaunchedEffect(Unit) {
        if (insulinTypes.isEmpty()) {
            viewModel.loadInsulinTypes()
        }
        if (activities.isEmpty()) {
            viewModel.loadActivities()
        }
    }

    LaunchedEffect(key1 = selectedMeal) {
        selectedMeal?.let { meal ->
            WW = ((meal.carbs ?: 0.0) / 10).toString()
            WBT = ((meal.carbs ?: 0.0) / 12).toString()
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
                            insulinTypeId = selectedInsulinType?.typeId,
                            noteText = noteText,
                            mealId = selectedMeal?.mealId,
                            InsulinAmount = insulinAmount.toDoubleOrNull(),
                            WW = WW.toDoubleOrNull(),
                            WBT = WBT.toDoubleOrNull(),
                            activityId = selectedActivity?.activityId
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
                value = WW,
                onValueChange = { WW = it },
                label = { Text("WW") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = WBT,
                onValueChange = { WBT = it },
                label = { Text("WBT") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.loadMealsByUserId(1) // TODO: Replace with actual user ID
                    showMealSelectionDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Meal")
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = activityExpanded,
                onExpandedChange = { activityExpanded = !activityExpanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedActivity?.activityType ?: "",
                    onValueChange = {},
                    label = { Text("Activity") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = activityExpanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = activityExpanded,
                    onDismissRequest = { activityExpanded = false },
                ) {
                    activities.forEach { activity ->
                        DropdownMenuItem(
                            onClick = {
                                selectedActivity = activity
                                activityExpanded = false
                            },
                            text = { Text(activity.activityType) }
                        )
                    }
                }
            }
        }
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
}