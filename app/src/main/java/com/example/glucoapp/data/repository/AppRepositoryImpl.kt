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


class AppRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val noteDao: NoteDao,
    private val mealDao: MealDao,
    private val activityDao: ActivityDao
) : AppRepository {
    // User
    override suspend fun insertUser(user: User) = userDao.insertUser(user)
    override fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)
    override suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Notes
    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    override fun getNotesByUserId(userId: Int): Flow<List<Note>> = noteDao.getNotesByUserId(userId)
    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    // Meals
    override suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)
    override fun getMealsByUserId(userId: Int): Flow<List<Meal>> = mealDao.getMealsByUserId(userId)
    override suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    override suspend fun updateMeal(meal: Meal) = mealDao.updateMeal(meal)

    // Activities
    override suspend fun insertActivity(activity: Activity) = activityDao.insertActivity(activity)
    override fun getActivitiesByUserId(userId: Int): Flow<List<Activity>> =
        activityDao.getActivitiesByUserId(userId)
    override suspend fun deleteActivity(activity: Activity) = activityDao.deleteActivity(activity)
    override suspend fun updateActivity(activity: Activity) = activityDao.updateActivity(activity)
}
