package com.example.glucoapp.ui.views

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
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var noteText by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    var insulinTypeId by remember { mutableStateOf("") } // Consider using a dropdown or selection UI
    var sugar by remember { mutableStateOf("") }
    var carboExch by remember { mutableStateOf("") }
    // ... add more fields for other note properties

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
                            timestamp = System.currentTimeMillis(), // Or get timestamp from user input
                            glucoseLevel = glucoseLevel.toDoubleOrNull() ?: 0.0,
                            insulinTypeId = insulinTypeId.toIntOrNull(), // Convert to Int if needed
                            noteText = noteText,
                            sugar = sugar.toDoubleOrNull() ?: 0.0,
                            carboExch = carboExch.toDoubleOrNull() ?: 0.0,
                            mealId = null, // Set if linked to a meal
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

            // Add fields for insulinTypeId, sugar, carboExch, etc.
            // Consider using dropdowns or other selection UI elements for insulinTypeId and meal/activity selection

            // Example of adding a meal from the database (replace with your actual implementation)
            Button(
                onClick = { /* TODO: Implement logic to select meal from database */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Meal from Database")
            }

            // Example of adding a predefined meal (replace with your actual implementation)
            Button(
                onClick = { /* TODO: Implement logic to select predefined meal */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Predefined Meal")
            }
        }
    }
}