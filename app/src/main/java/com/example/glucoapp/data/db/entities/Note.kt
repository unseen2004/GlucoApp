package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    val userId: Int = 0, // Assume a single user for now
    val timestamp: Long = 0L,
    val glucoseLevel: Double = 0.0,
    val insulinDoseFast: Double = 0.0, // Placeholder
    val insulinDoseFastCorr: Double = 0.0, // Placeholder
    val insulinDoseLong: Double = 0.0, // Placeholder
    val insulinType: Int = 0, // Placeholder
    val noteText: String = "",
    val sugar: Double = 0.0, // Placeholder
    val carboExch: Double = 0.0, // Placeholder
    val mealId: Int? = null, // Placeholder
    val activityId: Int? = null // Placeholder
)