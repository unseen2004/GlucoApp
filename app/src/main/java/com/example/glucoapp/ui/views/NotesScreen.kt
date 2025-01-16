package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.glucoapp.data.db.models.Note
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
                NoteItem(note = note, onDeleteClick = {
                    viewModel.deleteNote(note)
                })
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

@Composable
fun NoteItem(note: Note, onDeleteClick: () -> Unit) {
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
                text = note.noteText ?: "No Note Text",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "Glucose: ${note.glucoseLevel}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(2.dp))

            if (note.sugar != null) {
                Text(text = "Sugar: ${note.sugar} g")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (note.carboExch != null) {
                Text(text = "Carbo Exch: ${note.carboExch}")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (note.mealId != null) {
                Text(text = "Meal ID: ${note.mealId}")
                Spacer(modifier = Modifier.padding(2.dp))
            }
            if (note.activityId != null) {
                Text(text = "Activity ID: ${note.activityId}")
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