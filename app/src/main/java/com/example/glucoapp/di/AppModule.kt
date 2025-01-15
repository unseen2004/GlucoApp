package com.example.glucoapp.di

import android.content.Context
import androidx.room.Room
import com.example.glucoapp.data.UserPreferences
import com.example.glucoapp.data.db.AppDatabase
import com.example.glucoapp.data.db.daos.ActivityDao
import com.example.glucoapp.data.db.daos.InsulinTypeDao
import com.example.glucoapp.data.db.daos.MealDao
import com.example.glucoapp.data.db.daos.NoteDao
import com.example.glucoapp.data.db.daos.PredefinedMealDao
import com.example.glucoapp.data.db.daos.UserDao
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.data.repository.AppRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.glucoapp.data.db.MIGRATION_4_5

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "glucose_tracker_database"
        )
            .addMigrations(MIGRATION_4_5)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    @Singleton
    fun provideMealDao(db: AppDatabase): MealDao {
        return db.mealDao()
    }

    @Provides
    @Singleton
    fun provideActivityDao(db: AppDatabase): ActivityDao {
        return db.activityDao()
    }

    @Provides
    @Singleton
    fun provideInsulinTypeDao(db: AppDatabase): InsulinTypeDao {
        return db.insulinTypeDao()
    }

    @Provides
    @Singleton
    fun providePredefinedMealDao(db: AppDatabase): PredefinedMealDao {
        return db.predefinedMealDao()
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        userDao: UserDao,
        noteDao: NoteDao,
        mealDao: MealDao,
        activityDao: ActivityDao,
        insulinTypeDao: InsulinTypeDao,
        predefinedMealDao: PredefinedMealDao
    ): AppRepository {
        return AppRepositoryImpl(
            userDao,
            noteDao,
            mealDao,
            activityDao,
            insulinTypeDao,
            predefinedMealDao
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}