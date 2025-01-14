package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun getUserId(mainViewModel: MainViewModel): Int {
        return mainViewModel.user.value?.userId ?: 0
    }

    fun updateSettings(mainViewModel: MainViewModel) {
        val userId = getUserId(mainViewModel)
        viewModelScope.launch {
            // Update settings logic here
        }
    }
}