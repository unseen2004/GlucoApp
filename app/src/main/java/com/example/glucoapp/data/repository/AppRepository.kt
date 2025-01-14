package com.example.glucoapp.data.repository

import com.example.glucoapp.data.db.daos.ActivityDao
import com.example.glucoapp.data.db.daos.MealDao
import com.example.glucoapp.data.db.daos.NoteDao
import com.example.glucoapp.data.db.daos.UserDao
import com.example.glucoapp.data.db.entities.Activity
import com.example.glucoapp.data.db.entities.Meal
import com.example.glucoapp.data.db.entities.Note
import com.example.glucoapp.data.db.entities.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AppRepository {
    // User
    suspend fun insertUser(user: User)
    fun getUserById(userId: Int): Flow<User?>
    suspend fun updateUser(user: User)

    // Notes
    suspend fun insertNote(note: Note)
    fun getNotesByUserId(userId: Int): Flow<List<Note>>
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)

    // Meals
    suspend fun insertMeal(meal: Meal): Long
    fun getMealsByUserId(userId: Int): Flow<List<Meal>>
    suspend fun deleteMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)

    // Activities
    suspend fun insertActivity(activity: Activity)
    fun getActivitiesByUserId(userId: Int): Flow<List<Activity>>
    suspend fun deleteActivity(activity: Activity)
    suspend fun updateActivity(activity: Activity)
}