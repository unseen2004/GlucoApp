package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.glucoapp.data.db.entities.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity)

    @Update
    suspend fun update(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("SELECT * FROM Activities WHERE userId = :userId ORDER BY timestamp DESC")
    fun getActivitiesByUserId(userId: Int): Flow<List<Activity>>
}