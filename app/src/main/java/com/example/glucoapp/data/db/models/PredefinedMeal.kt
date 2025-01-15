package com.example.glucoapp.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "PredefinedMeals")
data class PredefinedMeal(
    @PrimaryKey(autoGenerate = true) val predefinedMealId: Int = 0,
    @ColumnInfo(name = "foodName") val foodName: String,
    @ColumnInfo(name = "protein") val protein: Double,
    @ColumnInfo(name = "carbs") val carbs: Double,
    @ColumnInfo(name = "fat") val fat: Double,
    @ColumnInfo(name = "kcal") val kcal: Double,
    @ColumnInfo(name = "imagePath") val imagePath: String?
)