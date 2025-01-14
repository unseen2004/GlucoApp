package com.example.glucoapp.di

import android.content.Context
import com.example.glucoapp.data.db.AppDatabase
import com.example.glucoapp.data.repository.AppRepository
import com.example.glucoapp.data.repository.AppRepositoryImpl
import com.example.glucoapp.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideAppRepository(db: AppDatabase): AppRepository {
        return AppRepositoryImpl(db.userDao(), db.noteDao(), db.mealDao(), db.activityDao())
    }

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}