package com.example.glucoapp.ui.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.example.glucoapp.ui.viewmodels.NoteViewModel
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.PredefinedMeal
import kotlinx.coroutines.launch
import androidx.compose.material3.ExposedDropdownMenuDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    // State variables for the Note fields
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var selectedInsulinTypeId by remember { mutableStateOf<Int?>(null) }
    var sugar by remember { mutableStateOf("") }
    var carboExch by remember { mutableStateOf("") }

    // State variables for related entities
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var selectedPredefinedMeal by remember { mutableStateOf<PredefinedMeal?>(null) }
    var selectedActivity by remember { mutableStateOf<Activity?>(null) }

    // State variables for dialog visibility
    var showMealSelectionDialog by remember { mutableStateOf(false) }
    var showPredefinedMealSelectionDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }

    // State variables for the Activity fields
    var activityName by remember { mutableStateOf("") }
    var activityDuration by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    // State for Insulin Types
    val insulinTypes by viewModel.insulinTypes.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // Date and Time
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTime = remember { mutableStateOf("") }
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _: TimePicker, mHour: Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute,
        false
    )

    // Collect StateFlows from ViewModel
    val meals by viewModel.meals.collectAsState()
    val predefinedMeals by viewModel.predefinedMeals.collectAsState()

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
                        val timestamp = run {
                            val dateParts = mDate.value.split("/")
                            val timeParts = mTime.value.split(":")
                            if (dateParts.size == 3 && timeParts.size == 2) {
                                val day = dateParts[0].toIntOrNull() ?: 0
                                val month = dateParts[1].toIntOrNull() ?: 0
                                val year = dateParts[2].toIntOrNull() ?: 0
                                val hour = timeParts[0].toIntOrNull() ?: 0
                                val minute = timeParts[1].toIntOrNull() ?: 0

                                Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month - 1)
                                    set(Calendar.DAY_OF_MONTH, day)
                                    set(Calendar.HOUR_OF_DAY, hour)
                                    set(Calendar.MINUTE, minute)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }.timeInMillis
                            } else {
                                System.currentTimeMillis()
                            }
                        }
                        val newNote = Note(
                            userId = 1, // TODO: Replace with actual user ID
                            timestamp = timestamp,
                            glucoseLevel = glucoseLevel.toDoubleOrNull() ?: 0.0,
                            insulinTypeId = selectedInsulinTypeId,
                            noteText = noteText,
                            sugar = sugar.toDoubleOrNull() ?: 0.0,
                            carboExch = carboExch.toDoubleOrNull() ?: 0.0,
                            mealId = selectedMeal?.mealId,
                            activityId = selectedActivity?.activityId // Now potentially set
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

            // Date Picker
            OutlinedTextField(
                value = mDate.value,
                onValueChange = { mDate.value = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { mDatePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Open Date Picker")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Time Picker
            OutlinedTextField(
                value = mTime.value,
                onValueChange = { mTime.value = it },
                label = { Text("Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { mTimePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Open Time Picker")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Insulin Type Selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedInsulinTypeId?.let { id ->
                        insulinTypes.firstOrNull { it.typeId == id }?.typeName
                    } ?: "",
                    onValueChange = {},
                    label = { Text("Insulin Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    insulinTypes.forEach { insulinType ->
                        DropdownMenuItem(
                            onClick = {
                                selectedInsulinTypeId = insulinType.typeId
                                expanded = false
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
                onValueChange = { /* Read-only field */ },
                label = { Text("Carbohydrate Exchange") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
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
                                viewModel.insertActivity(newActivity)
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