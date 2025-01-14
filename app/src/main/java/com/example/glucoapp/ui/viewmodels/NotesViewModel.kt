package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: AppRepository,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    // Combine notes with user data
    val notes: StateFlow<List<Note>> = mainViewModel.user.combine(_notes) { user, notes ->
        if (user != null) {
            notes.filter { it.userId == user.userId }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        loadNotes()
    }

    private fun loadNotes() {
        val userId = mainViewModel.user.value?.userId ?: return
        viewModelScope.launch {
            repository.getNotesByUserId(userId).collect { notesList ->
                _notes.value = notesList
            }
        }
    }
fun getUserId(): Int {
    return mainViewModel.user.value?.userId ?: 0
}
    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
}