package com.example.glucoapp.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.glucoapp.data.db.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long
    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM Users WHERE userId = :userId")
    fun getUserById(userId: Int): Flow<User>

    @Query("SELECT * FROM Users WHERE username = :username")
    fun getUserByUsername(username: String): Flow<User>
}