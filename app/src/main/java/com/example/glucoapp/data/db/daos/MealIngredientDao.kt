package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.glucoapp.data.db.models.MealIngredient
import kotlinx.coroutines.flow.Flow

@Dao
interface MealIngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mealIngredient: MealIngredient)

    @Update
    suspend fun update(mealIngredient: MealIngredient)

    @Delete
    suspend fun delete(mealIngredient: MealIngredient)

    @Query("SELECT * FROM MealIngredients WHERE mealId = :mealId")
    fun getMealIngredientsByMealId(mealId: Int): Flow<List<MealIngredient>>

    @Query("SELECT * FROM MealIngredients WHERE ingredientId = :ingredientId")
    fun getMealIngredientsByIngredientId(ingredientId: Int): Flow<List<MealIngredient>>
}