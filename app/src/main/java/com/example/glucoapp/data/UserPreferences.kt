package com.example.glucoapp.data
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    // Keys for preferences
    private object PreferencesKeys {
        val THEME_KEY = stringPreferencesKey("theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val IS_DOCTOR_LOGGED_IN = booleanPreferencesKey("is_doctor_logged_in")
    }

    // DataStore instance (initialize once)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    // User ID operations
    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID_KEY]
    }

//    suspend fun setUserId(userId: Int) {
//        context.dataStore.edit { preferences ->
//            preferences[PreferencesKeys.USER_ID_KEY] = userId
//        }
//    }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID_KEY] = userId
        }
    }
    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_ID_KEY)
        }
    }

    // Doctor login status
    val isDoctorLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_DOCTOR_LOGGED_IN] ?: false
    }

    suspend fun setDoctorLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DOCTOR_LOGGED_IN] = isLoggedIn
        }
    }

    // Theme preferences
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_KEY] = theme
        }
    }

    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_KEY] ?: "system"
        }

    // Language preferences
    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_KEY] = language
        }
    }

    val languageFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE_KEY] ?: "en"
        }

    // Clear all preferences
    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}