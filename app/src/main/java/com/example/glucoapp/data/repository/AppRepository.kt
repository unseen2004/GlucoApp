package com.example.glucoapp.data.repository

import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.Note
import com.example.glucoapp.data.db.models.PredefinedMeal
import com.example.glucoapp.data.db.models.User
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    // User operations
    suspend fun insertUser(user: User): Long // Returns the ID of the new row
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    fun getUserById(userId: Int): Flow<User?>
    fun getUserByUsername(username: String): Flow<User?>

    // Note operations
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotesByUserId(userId: Int): Flow<List<Note>>

    // Meal operations
    suspend fun insertMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    fun getMealsByUserId(userId: Int): Flow<List<Meal>>
    fun getMealById(mealId: Int): Flow<Meal?>

    // Activity operations
    suspend fun insertActivity(activity: Activity): Long // Return Long
    suspend fun updateActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    fun getActivitiesByUserId(userId: Int): Flow<List<Activity>>

    // InsulinType operations
    suspend fun insertInsulinType(insulinType: InsulinType)
    suspend fun updateInsulinType(insulinType: InsulinType)
    suspend fun deleteInsulinType(insulinType: InsulinType)
    fun getAllInsulinTypes(): Flow<List<InsulinType>>

    // PredefinedMeal operations
    suspend fun insertPredefinedMeal(predefinedMeal: PredefinedMeal)
    suspend fun updatePredefinedMeal(predefinedMeal: PredefinedMeal)
    suspend fun deletePredefinedMeal(predefinedMeal: PredefinedMeal)
    fun getAllPredefinedMeals(): Flow<List<PredefinedMeal>>
    fun getPredefinedMealById(predefinedMealId: Int): Flow<PredefinedMeal?>
}