package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Activity(
    @PrimaryKey(autoGenerate = true) val activityId: Int = 0,
    val userId: Int = 0, // Assume a single user for now
    val timestamp: Long = 0L,
    val activityType: String = "",
    val duration: Int = 0, // in minutes
    val notes: String = ""
)