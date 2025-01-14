package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.navigation.Screen.Notes
import com.example.glucoapp.ui.viewmodels.NotesViewModel

@Composable
fun NotesScreen(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    val notes by notesViewModel.notes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddNote.route) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Notes")
            }
        }
    ) { innerPadding ->
        NotesContent(
            notes = notes,
            onDeleteNote = { note -> notesViewModel.deleteNote(note) },
            onUpdateNote = { note -> notesViewModel.updateNote(note) },
            modifier = Modifier.padding(innerPadding) // Correct padding modifier
        )
    }
}

@Composable
fun NotesContent(
    notes: List<Notes>,
    onDeleteNote: (Notes) -> Unit,
    onUpdateNote: (Notes) -> Unit,
    modifier: Modifier = Modifier // Add default Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(
            items = notes,
            key = { note -> note.noteId } // Provide a unique key
        ) { note ->
            NoteItem(
                notes = note,
                onDelete = { onDeleteNote(note) },
                onUpdate = { updatedNote -> onUpdateNote(updatedNote) }
            )
        }
    }
}

@Composable
fun NoteItem(notes: Notes, onDelete: () -> Unit, onUpdate: (Notes) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var updatedNoteText by remember { mutableStateOf(value = notes.noteText) }

    Card(modifier = Modifier.padding(8.dp), elevation = 2.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditing) {
                OutlinedTextField(
                    value = updatedNoteText,
                    onValueChange = { updatedNoteText = it },
                    label = { Text("Notes Text") }
                )
                Button(
                    onClick = {
                        onUpdate(notes.copy(noteText = updatedNoteText))
                        isEditing = false
                    }
                ) {
                    Text("Save")
                }
                Button(onClick = { isEditing = false }) {
                    Text("Cancel")
                }
            } else {
                Text(text = "Glucose Level: ${notes.glucoseLevel}")
                Text(text = "Notes: ${notes.noteText}")
                Row {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}