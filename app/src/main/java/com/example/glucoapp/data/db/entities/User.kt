package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String = "", // Placeholder for now
    val passwordHash: String = "", // Placeholder for now
    val doctorPasswordHash: String = "", // Placeholder for now
    val accessLevel: Int = 0, // 0 for user, 1 for doctor (Placeholder)
    val glucoseUnit: Int = 0 // 0 for mg/dL, 1 for mmol/L
)