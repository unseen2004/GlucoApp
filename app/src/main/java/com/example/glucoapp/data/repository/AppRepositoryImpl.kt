package com.example.glucoapp.data.repository

import com.example.glucoapp.data.db.daos.ActivityDao
import com.example.glucoapp.data.db.daos.InsulinTypeDao
import com.example.glucoapp.data.db.daos.MealDao
import com.example.glucoapp.data.db.daos.NoteDao
import com.example.glucoapp.data.db.daos.PredefinedMealDao
import com.example.glucoapp.data.db.daos.UserDao
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.Note
import com.example.glucoapp.data.db.models.PredefinedMeal
import com.example.glucoapp.data.db.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val noteDao: NoteDao,
    private val mealDao: MealDao,
    private val activityDao: ActivityDao,
    private val insulinTypeDao: InsulinTypeDao,
    private val predefinedMealDao: PredefinedMealDao
) : AppRepository {

    // User operations
    override suspend fun insertUser(user: User) = userDao.insert(user)
    override suspend fun updateUser(user: User) = userDao.update(user)
    override suspend fun deleteUser(user: User) = userDao.delete(user)
    override fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)
    override fun getUserByUsername(username: String): Flow<User?> = userDao.getUserByUsername(username)

    // Note operations
    override suspend fun insertNote(note: Note) = noteDao.insert(note)
    override suspend fun updateNote(note: Note) = noteDao.update(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)
    override fun getNotesByUserId(userId: Int): Flow<List<Note>> = noteDao.getNotesByUserId(userId)

    // Meal operations
    override suspend fun insertMeal(meal: Meal) = mealDao.insert(meal)
    override suspend fun updateMeal(meal: Meal) = mealDao.update(meal)
    override suspend fun deleteMeal(meal: Meal) = mealDao.delete(meal)
    override fun getMealsByUserId(userId: Int): Flow<List<Meal>> = mealDao.getMealsByUserId(userId)
    override fun getMealById(mealId: Int): Flow<Meal?> = mealDao.getMealById(mealId)

    // Activity operations
    override suspend fun insertActivity(activity: Activity): Long {
        return activityDao.insert(activity)
    }
    override suspend fun updateActivity(activity: Activity) = activityDao.update(activity)
    override suspend fun deleteActivity(activity: Activity) = activityDao.delete(activity)
    override fun getActivitiesByUserId(userId: Int): Flow<List<Activity>> = activityDao.getActivitiesByUserId(userId)

    // InsulinType operations
    override suspend fun insertInsulinType(insulinType: InsulinType) = insulinTypeDao.insert(insulinType)
    override suspend fun updateInsulinType(insulinType: InsulinType) = insulinTypeDao.update(insulinType)
    override suspend fun deleteInsulinType(insulinType: InsulinType) = insulinTypeDao.delete(insulinType)
    override fun getAllInsulinTypes(): Flow<List<InsulinType>> {
        return insulinTypeDao.getAllInsulinTypes()
    }

    // PredefinedMeal operations
    override suspend fun insertPredefinedMeal(predefinedMeal: PredefinedMeal) = predefinedMealDao.insert(predefinedMeal)
    override suspend fun updatePredefinedMeal(predefinedMeal: PredefinedMeal) = predefinedMealDao.update(predefinedMeal)
    override suspend fun deletePredefinedMeal(predefinedMeal: PredefinedMeal) = predefinedMealDao.delete(predefinedMeal)
    override fun getAllPredefinedMeals(): Flow<List<PredefinedMeal>> = predefinedMealDao.getAllPredefinedMeals()
    override fun getPredefinedMealById(predefinedMealId: Int): Flow<PredefinedMeal?> = predefinedMealDao.getPredefinedMealById(predefinedMealId)
}