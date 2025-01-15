package com.example.glucoapp.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "Meals",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PredefinedMeal::class,
            parentColumns = ["predefinedMealId"],
            childColumns = ["predefinedMealId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "predefinedMealId") val predefinedMealId: Int?, // Now allows nulls
    @ColumnInfo(name = "foodName") val foodName: String?, // Now allows null
    @ColumnInfo(name = "protein") val protein: Double?, // Allow null for custom values
    @ColumnInfo(name = "carbs") val carbs: Double?, // Allow null for custom values
    @ColumnInfo(name = "fat") val fat: Double?, // Allow null for custom values
    @ColumnInfo(name = "kcal") val kcal: Double?, // Allow null for custom values
    @ColumnInfo(name = "imagePath") val imagePath: String?,
    @ColumnInfo(name = "customProtein") val customProtein: Double? = null, // New field for custom protein
    @ColumnInfo(name = "customCarbs") val customCarbs: Double? = null, // New field for custom carbs
    @ColumnInfo(name = "customFat") val customFat: Double? = null, // New field for custom fat
    @ColumnInfo(name = "customKcal") val customKcal: Double? = null // New field for custom kcal
)