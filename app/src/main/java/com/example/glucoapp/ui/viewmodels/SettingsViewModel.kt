package com.example.glucosetracker.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.Activity
import com.example.glucoapp.data.db.entities.Meal
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val mainViewModel: MainViewModel
) : ViewModel() {
    // You'll add settings-related logic here later
    // For example:
    private val _glucoseUnitSetting = MutableStateFlow(0) // 0 for mg/dL, 1 for mmol/L
    val glucoseUnitSetting: StateFlow<Int> = _glucoseUnitSetting.asStateFlow()

    fun updateGlucoseUnitSetting(newSetting: Int) {
        // Update the setting in the database or SharedPreferences
        // and then update the _glucoseUnitSetting flow
    }
}