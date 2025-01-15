package com.example.glucoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.glucoapp.data.db.daos.ActivityDao
import com.example.glucoapp.data.db.daos.InsulinTypeDao
import com.example.glucoapp.data.db.daos.MealDao
import com.example.glucoapp.data.db.daos.NoteDao
import com.example.glucoapp.data.db.daos.PredefinedMealDao
import com.example.glucoapp.data.db.daos.UserDao
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.Meal
import com.example.glucoapp.data.db.models.Note
import com.example.glucoapp.data.db.models.PredefinedMeal
import com.example.glucoapp.data.db.models.User

@Database(
    entities = [User::class, Note::class, Meal::class, Activity::class, InsulinType::class, PredefinedMeal::class],
    version = 5, // Increment the version number
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun mealDao(): MealDao
    abstract fun activityDao(): ActivityDao
    abstract fun insulinTypeDao(): InsulinTypeDao
    abstract fun predefinedMealDao(): PredefinedMealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "glucose_tracker_database"
                )
                    .addMigrations(MIGRATION_4_5) // Add the migration from the external file
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}