package com.example.glucoapp.data.repository

import com.example.glucoapp.data.db.entities.Activity
import com.example.glucoapp.data.db.entities.InsulinType
import com.example.glucoapp.data.db.entities.Meal
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.db.entities.PredefinedMeal
import com.example.glucoapp.data.db.entities.User
import com.example.glucoapp.navigation.Screen
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    // User operations
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    fun getUserById(userId: Int): Flow<User?>
    fun getUserByUsername(username: String): Flow<User?>

    // Note operations
    suspend fun insertNote(note: Screen.Notes)
    suspend fun updateNote(note: Screen.Notes)
    suspend fun deleteNote(note: Screen.Notes)
    fun getNotesByUserId(userId: Int): Flow<List<Note>>

    // Meal operations
    suspend fun insertMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    fun getMealsByUserId(userId: Int): Flow<List<Meal>>
    fun getMealById(mealId: Int): Flow<Meal?>

    // Activity operations
    suspend fun insertActivity(activity: Activity)
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