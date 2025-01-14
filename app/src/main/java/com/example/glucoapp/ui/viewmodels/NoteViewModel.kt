package com.example.glucoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUserById(userId: Int) {
        viewModelScope.launch {
            repository.getUserById(userId).collect { user ->
                _user.value = user
            }
        }
    }

    fun loadNotesByUserId(userId: Int) {
        viewModelScope.launch {
            repository.getNotesByUserId(userId).collect { notes ->
                _notes.value = notes
            }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }
}