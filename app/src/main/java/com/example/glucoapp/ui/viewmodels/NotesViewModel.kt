package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.navigation.Screen.Notes
import com.example.glucoapp.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Notes>>(emptyList())
    val notes: StateFlow<List<Notes>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        // This method will be updated to use ViewModelProvider
    }

    suspend fun insertNote(notes: Notes) {
        return repository.insertNote(notes)
    }

    fun deleteNote(notes: Notes) {
        viewModelScope.launch {
            repository.deleteNote(notes)
        }
    }

    fun updateNote(notes: Notes) {
        viewModelScope.launch {
            repository.updateNote(notes)
        }
    }

    fun getUserId(mainViewModel: MainViewModel): Int {
        return mainViewModel.user.value?.userId ?: 0
    }
}