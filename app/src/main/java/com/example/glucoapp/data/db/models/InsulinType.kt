package com.example.glucoapp.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "InsulinTypes")
data class InsulinType(
    @PrimaryKey(autoGenerate = true) val typeId: Int = 0,
    @ColumnInfo(name = "typeName") val typeName: String
)