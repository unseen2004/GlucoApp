package com.example.glucoapp.di

import android.content.Context
import androidx.room.Room
import com.example.glucoapp.data.db.AppDatabase
import com.example.glucoapp.data.db.MIGRATION_4_5
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.data.repository.AppRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun provideAppRepository(db: AppDatabase): AppRepository {
        return AppRepositoryImpl(
            db.userDao(),
            db.noteDao(),
            db.mealDao(),
            db.activityDao(),
            db.insulinTypeDao(),
            db.predefinedMealDao()
        )
    }
}