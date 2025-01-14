package com.example.glucoapp.ui.views
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.NotesViewModel

@Composable
fun NotesScreen(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    val notes by notesViewModel.notes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notes") }) },
        floatingActionButton = {
            IconButton(onClick = { navController.navigate(Screen.AddNote.route) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        NotesContent(
            notes = notes,
            onDeleteNote = { note -> notesViewModel.deleteNote(note) },
            onUpdateNote = { note -> notesViewModel.updateNote(note) },
            modifier = padding(innerPadding)
        )
    }
}

@Composable
fun NotesContent(
    notes: List<Note>,
    onDeleteNote: (Note) -> Unit,
    onUpdateNote: (Note) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(notes) { note ->
            NoteItem(
                note = note,
                onDelete = { onDeleteNote(note) },
                onUpdate = { updatedNote -> onUpdateNote(updatedNote) }
            )
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit, onUpdate: (Note) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var updatedNoteText by remember { mutableStateOf(note.noteText) }

    Card(modifier = Modifier.padding(8.dp), elevation = 2.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditing) {
                OutlinedTextField(
                    value = updatedNoteText,
                    onValueChange = { updatedNoteText = it },
                    label = { Text("Note Text") }
                )
                Button(
                    onClick = {
                        onUpdate(note.copy(noteText = updatedNoteText))
                        isEditing = false
                    }
                ) {
                    Text("Save")
                }
                Button(onClick = { isEditing = false }) {
                    Text("Cancel")
                }
            } else {
                Text(text = "Glucose Level: ${note.glucoseLevel}")
                Text(text = "Note: ${note.noteText}")
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