package com.example.glucoapp.presentation.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.example.glucoapp.navigation.Screen
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.presentation.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var insulinTypeId by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    var carboExch by remember { mutableStateOf("") }
    var selectedMealId by remember { mutableStateOf<Int?>(null) }
    var selectedPredefinedMealId by remember { mutableStateOf<Int?>(null) }
    var showMealSelectionDialog by remember { mutableStateOf(false) }
    var showPredefinedMealSelectionDialog by remember { mutableStateOf(false) }
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Notes.route) }) {
                        Icon(Icons.Filled.Close, "Cancel")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val newNote = Note(
                            userId = 1, // Replace with actual user ID
                            timestamp = System.currentTimeMillis(),
                            glucoseLevel = glucoseLevel.toDoubleOrNull() ?: 0.0,
                            insulinTypeId = insulinTypeId.toIntOrNull(),
                            noteText = noteText,
                            sugar = sugar.toDoubleOrNull() ?: 0.0,
                            carboExch = carboExch.toDoubleOrNull() ?: 0.0,
                            mealId = selectedMealId,
                            activityId = null // Set if linked to an activity
                        )
                        viewModel.insertNote(newNote)
                        navController.navigate(Screen.Notes.route)
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mDate.value,
                onValueChange = { mDate.value = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                mDatePickerDialog.show()
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Open Date Picker")
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = insulinTypeId,
                onValueChange = { insulinTypeId = it },
                label = { Text("Insulin Type ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = sugar,
                onValueChange = { sugar = it },
                label = { Text("Sugar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = carboExch,
                onValueChange = { carboExch = it },
                label = { Text("Carbohydrate Exchange") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showMealSelectionDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Meal from Database")
            }

            Button(
                onClick = { showPredefinedMealSelectionDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Predefined Meal")
            }

            // Dialogs for meal selection
            if (showMealSelectionDialog) {
                // TODO: Implement Meal Selection Dialog
                AlertDialog(
                    onDismissRequest = { showMealSelectionDialog = false },
                    title = { Text("Select Meal") },
                    text = { Text("Meal selection logic here") }, // Placeholder
                    confirmButton = {
                        Button(onClick = {
                            // Handle meal selection
                            showMealSelectionDialog = false
                        }) {
                            Text("Select")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showMealSelectionDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showPredefinedMealSelectionDialog) {
                // TODO: Implement Predefined Meal Selection Dialog
                AlertDialog(
                    onDismissRequest = { showPredefinedMealSelectionDialog = false },
                    title = { Text("Select Predefined Meal") },
                    text = { Text("Predefined meal selection logic here") }, // Placeholder
                    confirmButton = {
                        Button(onClick = {
                            // Handle predefined meal selection
                            showPredefinedMealSelectionDialog = false
                        }) {
                            Text("Select")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showPredefinedMealSelectionDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}