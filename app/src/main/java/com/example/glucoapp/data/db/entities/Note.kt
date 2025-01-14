package com.example.glucoapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "Notes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InsulinType::class,
            parentColumns = ["typeId"],
            childColumns = ["insulinTypeId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["mealId"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Activity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "glucoseLevel") val glucoseLevel: Double,
    @ColumnInfo(name = "insulinTypeId") val insulinTypeId: Int?,
    @ColumnInfo(name = "noteText") val noteText: String?,
    @ColumnInfo(name = "sugar") val sugar: Double?,
    @ColumnInfo(name = "carboExch") val carboExch: Double?,
    @ColumnInfo(name = "mealId") val mealId: Int?,
    @ColumnInfo(name = "activityId") val activityId: Int?
)