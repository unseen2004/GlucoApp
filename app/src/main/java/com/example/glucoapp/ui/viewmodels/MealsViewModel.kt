package com.example.glucoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.Meal
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    init {
        loadMeals()
    }

    private fun loadMeals() {
        // This method will be updated to use ViewModelProvider
    }

    suspend fun insertMeal(meal: Meal) {
        return repository.insertMeal(meal)
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch {
            repository.updateMeal(meal)
        }
    }

    fun getUserId(mainViewModel: MainViewModel): Int {
        return mainViewModel.user.value?.userId ?: 0
    }
}