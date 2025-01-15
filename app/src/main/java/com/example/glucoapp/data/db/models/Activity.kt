package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "Activities",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Activity(
    @PrimaryKey(autoGenerate = true) val activityId: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "activityType") val activityType: String,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "notes") val notes: String?
)