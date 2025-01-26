package com.example.glucoapp.di
import android.content.Context
import androidx.room.Room
import com.example.glucoapp.data.UserPreferences
import com.example.glucoapp.data.db.AppDatabase
import com.example.glucoapp.data.db.daos.*
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
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()

    @Provides
    @Singleton
    fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()

    @Provides
    @Singleton
    fun provideInsulinTypeDao(db: AppDatabase): InsulinTypeDao = db.insulinTypeDao()

    @Provides
    @Singleton
    fun provideIngredientDao(db: AppDatabase): IngredientDao = db.ingredientDao()

    @Provides
    @Singleton
    fun provideAppRepository(
        userDao: UserDao,
        noteDao: NoteDao,
        mealDao: MealDao,
        activityDao: ActivityDao,
        insulinTypeDao: InsulinTypeDao,
        ingredientDao: IngredientDao
    ): AppRepository {
        return AppRepositoryImpl(
            userDao,
            noteDao,
            mealDao,
            activityDao,
            insulinTypeDao,
            ingredientDao
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Notes : Screen("notes")
    object Meals : Screen("meals")
    object Settings : Screen("settings")
    object AddNote : Screen("add_note")
    object AddMeal : Screen("add_meal")
    object Register : Screen("register")
}