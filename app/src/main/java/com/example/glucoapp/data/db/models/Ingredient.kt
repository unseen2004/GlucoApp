package com.example.glucoapp.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    @ColumnInfo(name = "foodName") val foodName: String,
    @ColumnInfo(name = "protein") val protein: Double,
    @ColumnInfo(name = "carbs") val carbs: Double,
    @ColumnInfo(name = "fat") val fat: Double,
    @ColumnInfo(name = "kcal") val kcal: Double,
    @ColumnInfo(name = "mealId") val mealId: Int
)