package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.glucoapp.data.db.entities.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Query("SELECT * FROM Activity WHERE userId = :userId")
    fun getActivitiesByUserId(userId: Int): Flow<List<Activity>>

    @Delete
    suspend fun deleteActivity(activity: Activity)

    @Update
    suspend fun updateActivity(activity: Activity)
}