package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.glucoapp.ui.viewmodels.MainViewModel
import com.example.glucoapp.ui.viewmodels.NotesViewModel
import com.example.glucoapp.data.db.entities.Notes
import com.example.glucoapp.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val mainViewModel: MainViewModel = ViewModelProvider(context as androidx.fragment.app.FragmentActivity)[MainViewModel::class.java]

    var glucoseLevel by remember { mutableStateOf("") }
    var insulinDoseFast by remember { mutableStateOf("") }
    var insulinDoseFastCorr by remember { mutableStateOf("") }
    var insulinDoseLong by remember { mutableStateOf("") }
    var insulinType by remember { mutableIntStateOf(0) } // Default to type 1
    var noteText by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    var carboExch by remember { mutableStateOf("") }
    // Add meal and activity related states if necessary

    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Notes") }) }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            OutlinedTextField(
                value = glucoseLevel,
                onValueChange = { glucoseLevel = it },
                label = { Text("Glucose Level") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = insulinDoseFast,
                onValueChange = { insulinDoseFast = it },
                label = { Text("Insulin Dose (Fast)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = insulinDoseFastCorr,
                onValueChange = { insulinDoseFastCorr = it },
                label = { Text("Insulin Dose (Fast Correction)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = insulinDoseLong,
                onValueChange = { insulinDoseLong = it },
                label = { Text("Insulin Dose (Long)") },
                modifier = Modifier.fillMaxWidth()
            )
            // Add a dropdown or segmented control for insulinType if needed

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Notes Text") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sugar,
                onValueChange = { sugar = it },
                label = { Text("Sugar") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = carboExch,
                onValueChange = { carboExch = it },
                label = { Text("Carbohydrate Exchanges") },
                modifier = Modifier.fillMaxWidth()
            )
            // Add fields for meal and activity if necessary

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val newNotes = Notes(
                        userId = notesViewModel.getUserId(mainViewModel), // Replace with actual user ID
                        timestamp = System.currentTimeMillis(), // Use current time
                        glucoseLevel = glucoseLevel.toDoubleOrNull() ?: 0.0,
                        insulinDoseFast = insulinDoseFast.toDoubleOrNull() ?: 0.0,
                        insulinDoseFastCorr = insulinDoseFastCorr.toDoubleOrNull() ?: 0.0,
                        insulinDoseLong = insulinDoseLong.toDoubleOrNull() ?: 0.0,
                        insulinType = insulinType,
                        noteText = noteText,
                        sugar = sugar.toDoubleOrNull() ?: 0.0,
                        carboExch = carboExch.toDoubleOrNull() ?: 0.0,
                        mealId = null, // Replace with actual meal ID if applicable
                        activityId = null // Replace with actual activity ID if applicable
                    )
                    coroutineScope.launch {
                        notesViewModel.insertNote(newNotes)
                        navController.navigate(Screen.Notes.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Notes")
            }
        }
    }
}