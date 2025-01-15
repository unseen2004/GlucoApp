package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.NoteViewModel

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState()

    // Load notes when the screen is shown
    LaunchedEffect(Unit) {
        viewModel.loadUserById(1) // TODO: Replace with actual user ID
        viewModel.loadNotesByUserId(1) // TODO: Replace with actual user ID
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notes) { note ->
                // Display note details
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = """
                        Note: ${note.noteText}
                        Glucose: ${note.glucoseLevel}
                        Date: ${note.timestamp}
                        Insulin Type ID: ${note.insulinTypeId}
                        Sugar: ${note.sugar}
                        Carbo Exch: ${note.carboExch}
                        Meal ID: ${note.mealId}
                        Activity ID: ${note.activityId}
                    """.trimIndent()
                )
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddNote.route) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add Note")
        }
    }
}