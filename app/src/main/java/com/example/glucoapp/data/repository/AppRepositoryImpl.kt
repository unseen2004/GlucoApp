package com.example.glucoapp.data.repository
import com.example.glucoapp.data.db.daos.*
import com.example.glucoapp.data.db.models.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val noteDao: NoteDao,
    private val mealDao: MealDao,
    private val activityDao: ActivityDao,
    private val insulinTypeDao: InsulinTypeDao,
    private val ingredientDao: IngredientDao
) : AppRepository {

    // User operations
    override fun getUserById(userId: Int): Flow<User?> = userDao.getUserById(userId)
    override suspend fun getUserByUsername(username: String): Flow<User?> = userDao.getUserByUsername(username)
    override suspend fun insertUser(user: User) = userDao.insert(user)
    override suspend fun updateUser(user: User) = userDao.update(user)
    override suspend fun deleteUser(user: User) = userDao.delete(user)
    override suspend fun registerUser(user: User) = userDao.insert(user) // Added function

    // Note operations
    override suspend fun insertNote(note: Note) = noteDao.insert(note)
    override suspend fun updateNote(note: Note) = noteDao.update(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)
    override fun getNotesByUserId(userId: Int): Flow<List<Note>> = noteDao.getNotesByUserId(userId)
    override suspend fun updatePassword(userId: Int, newPasswordHash: String) {
        userDao.updatePassword(userId, newPasswordHash)
    }

    override suspend fun updateUsername(userId: Int, newUsername: String) {
        userDao.updateUsername(userId, newUsername)
    }

    override suspend fun updateDoctorPassword(userId: Int, newDoctorPasswordHash: String) {
        userDao.updateDoctorPassword(userId, newDoctorPasswordHash)
    }

    override suspend fun deleteUserAndRelatedData(userId: Int) {
        userDao.deleteUserAndRelatedData(userId)
    }
    // Meal operations
    override suspend fun insertMeal(meal: Meal) = mealDao.insert(meal)
    override suspend fun updateMeal(meal: Meal) = mealDao.update(meal)
    override suspend fun deleteMeal(meal: Meal) = mealDao.delete(meal)
    override fun getMealsByUserId(userId: Int): Flow<List<Meal>> = mealDao.getMealsByUserId(userId)
    override fun getMealById(mealId: Int): Flow<Meal?> = mealDao.getMealById(mealId)

    // Activity operations
    override suspend fun insertActivity(activity: Activity): Long = activityDao.insert(activity)
    override suspend fun updateActivity(activity: Activity) = activityDao.update(activity)
    override suspend fun deleteActivity(activity: Activity) = activityDao.delete(activity)
    override fun getActivitiesByUserId(userId: Int): Flow<List<Activity>> = activityDao.getActivitiesByUserId(userId)
    override fun getActivityById(activityId: Int): Flow<Activity?> = activityDao.getActivityById(activityId)

    // InsulinType operations
    override suspend fun insertInsulinType(insulinType: InsulinType) = insulinTypeDao.insert(insulinType)
    override suspend fun updateInsulinType(insulinType: InsulinType) = insulinTypeDao.update(insulinType)
    override suspend fun deleteInsulinType(insulinType: InsulinType) = insulinTypeDao.delete(insulinType)
    override fun getAllInsulinTypes(): Flow<List<InsulinType>> = insulinTypeDao.getAllInsulinTypes()
    override fun getInsulinTypeById(typeId: Int): Flow<InsulinType?> = insulinTypeDao.getInsulinTypeById(typeId)

    // Ingredient operations
    override suspend fun insertIngredient(ingredient: Ingredient) = ingredientDao.insert(ingredient)
    override suspend fun updateIngredient(ingredient: Ingredient) = ingredientDao.update(ingredient)
    override suspend fun deleteIngredient(ingredient: Ingredient) = ingredientDao.delete(ingredient)
    override fun getAllIngredients(): Flow<List<Ingredient>> = ingredientDao.getAllIngredients()
    override fun getIngredientById(ingredientId: Int): Flow<Ingredient?> = ingredientDao.getIngredientById(ingredientId)
}