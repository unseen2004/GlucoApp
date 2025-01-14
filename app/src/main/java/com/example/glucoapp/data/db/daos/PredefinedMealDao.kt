package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.glucoapp.data.db.entities.PredefinedMeal
import kotlinx.coroutines.flow.Flow

@Dao
interface PredefinedMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(predefinedMeal: PredefinedMeal)

    @Update
    suspend fun update(predefinedMeal: PredefinedMeal)

    @Delete
    suspend fun delete(predefinedMeal: PredefinedMeal)

    @Query("SELECT * FROM PredefinedMeals")
    fun getAllPredefinedMeals(): Flow<List<PredefinedMeal>>

    @Query("SELECT * FROM PredefinedMeals WHERE predefinedMealId = :predefinedMealId")
    fun getPredefinedMealById(predefinedMealId: Int): Flow<PredefinedMeal>
}