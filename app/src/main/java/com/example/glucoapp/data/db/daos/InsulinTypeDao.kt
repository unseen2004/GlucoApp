package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.glucoapp.data.db.entities.InsulinType
import kotlinx.coroutines.flow.Flow

@Dao
interface InsulinTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(insulinType: InsulinType)

    @Update
    suspend fun update(insulinType: InsulinType)

    @Delete
    suspend fun delete(insulinType: InsulinType)

    @Query("SELECT * FROM InsulinTypes")
    fun getAllInsulinTypes(): Flow<List<InsulinType>>
}