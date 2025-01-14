package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int = 0,
    val userId: Int = 0, // Assume a single user for now
    val foodName: String = "",
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val kcal: Double = 0.0,
    val imagePath: String? = null,
    val isPredefined: Int = 0
)