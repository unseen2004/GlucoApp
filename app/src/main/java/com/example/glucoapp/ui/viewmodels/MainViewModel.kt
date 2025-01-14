package com.example.glucoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoapp.data.db.entities.Activity
import com.example.glucoapp.data.db.entities.InsulinType
import com.example.glucoapp.data.db.entities.Meal
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.db.entities.PredefinedMeal
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())

    private val _insulinTypes = MutableStateFlow<List<InsulinType>>(emptyList())

    private val _predefinedMeals = MutableStateFlow<List<PredefinedMeal>>(emptyList())

    // Combine flows to create a state flow for each data type
    val notes: StateFlow<List<Note>> = _notes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val meals: StateFlow<List<Meal>> = _meals.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val activities: StateFlow<List<Activity>> = _activities.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val insulinTypes: StateFlow<List<InsulinType>> = _insulinTypes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val predefinedMeals: StateFlow<List<PredefinedMeal>> = _predefinedMeals.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Functions to interact with the repository
    // User
    fun loadUserById(userId: Int) {
        viewModelScope.launch {
            repository.getUserById(userId).collect { user ->
                _user.value = user
            }
        }
    }

    fun loadUserByUsername(username: String) {
        viewModelScope.launch {
            repository.getUserByUsername(username).collect { user ->
                _user.value = user
            }
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            val newUserId = repository.insertUser(user)
            // Optionally reload user data
            loadUserById(newUserId.toInt())
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            // Optionally reload user data
            loadUserById(user.userId)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
            // Reset user data
            _user.value = null
        }
    }

    // Notes
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
            // Reload notes if necessary
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
            // Reload notes if necessary
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            // Reload notes if necessary
            _user.value?.userId?.let { loadNotesByUserId(it) }
        }
    }

    // Meals
    fun loadMealsByUserId(userId: Int) {
        viewModelScope.launch {
            repository.getMealsByUserId(userId).collect { meals ->
                _meals.value = meals
            }
        }
    }

    fun loadMealById(mealId: Int) {
        viewModelScope.launch {
            repository.getMealById(mealId).collect { meal ->
                // Handle meal loading, possibly by updating a separate meal detail state flow
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            repository.insertMeal(meal)
            // Reload meals if necessary
            _user.value?.userId?.let { loadMealsByUserId(it) }
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch {
            repository.updateMeal(meal)
            // Reload meals if necessary
            _user.value?.userId?.let { loadMealsByUserId(it) }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            // Reload meals if necessary
            _user.value?.userId?.let { loadMealsByUserId(it) }
        }
    }

    // Activities
    fun loadActivitiesByUserId(userId: Int) {
        viewModelScope.launch {
            repository.getActivitiesByUserId(userId).collect { activities ->
                _activities.value = activities
            }
        }
    }

    fun insertActivity(activity: Activity) {
        viewModelScope.launch {
            repository.insertActivity(activity)
            // Reload activities if necessary
            _user.value?.userId?.let { loadActivitiesByUserId(it) }
        }
    }

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
            // Reload activities if necessary
            _user.value?.userId?.let { loadActivitiesByUserId(it) }
        }
    }

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            repository.deleteActivity(activity)
            // Reload activities if necessary
            _user.value?.userId?.let { loadActivitiesByUserId(it) }
        }
    }

    // Insulin Types
    fun loadAllInsulinTypes() {
        viewModelScope.launch {
            repository.getAllInsulinTypes().collect { insulinTypes ->
                _insulinTypes.value = insulinTypes
            }
        }
    }

    fun insertInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.insertInsulinType(insulinType)
            // Reload insulin types
            loadAllInsulinTypes()
        }
    }

    fun updateInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.updateInsulinType(insulinType)
            // Reload insulin types
            loadAllInsulinTypes()
        }
    }

    fun deleteInsulinType(insulinType: InsulinType) {
        viewModelScope.launch {
            repository.deleteInsulinType(insulinType)
            // Reload insulin types
            loadAllInsulinTypes()
        }
    }

    // Predefined Meals
    fun loadAllPredefinedMeals() {
        viewModelScope.launch {
            repository.getAllPredefinedMeals().collect { predefinedMeals ->
                _predefinedMeals.value = predefinedMeals
            }
        }
    }

    fun loadPredefinedMealById(predefinedMealId: Int) {
        viewModelScope.launch {
            repository.getPredefinedMealById(predefinedMealId).collect { predefinedMeal ->
                // Handle predefined meal loading, possibly by updating a separate predefined meal detail state flow
            }
        }
    }

    fun insertPredefinedMeal(predefinedMeal: PredefinedMeal) {
        viewModelScope.launch {
            repository.insertPredefinedMeal(predefinedMeal)
            // Reload predefined meals
            loadAllPredefinedMeals()
        }
    }

    fun updatePredefinedMeal(predefinedMeal: PredefinedMeal) {
        viewModelScope.launch {
            repository.updatePredefinedMeal(predefinedMeal)
            // Reload predefined meals
            loadAllPredefinedMeals()
        }
    }

    fun deletePredefinedMeal(predefinedMeal: PredefinedMeal) {
        viewModelScope.launch {
            repository.deletePredefinedMeal(predefinedMeal)
            // Reload predefined meals
            loadAllPredefinedMeals()
        }
    }

    // Other functions as needed...
}