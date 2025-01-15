package com.example.glucoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    // Create a DataStore instance
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    // Keys for preferences
    object PreferencesKeys {
        val THEME_KEY = stringPreferencesKey("theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val USER_ID_KEY = intPreferencesKey("user_id") // To store the logged-in user's ID
    }

    // Save theme preference
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_KEY] = theme
        }
    }

    // Get theme preference
    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_KEY] ?: "system" // Default to system theme
        }

    // Save language preference
    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_KEY] = language
        }
    }

    // Get language preference
    val languageFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE_KEY] ?: "en" // Default to English
        }

    // Save logged-in user's ID
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID_KEY] = userId
        }
    }

    // Get logged-in user's ID
    val userIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_ID_KEY]
        }

    // Function to clear all preferences (for logout or clearing data)
    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}