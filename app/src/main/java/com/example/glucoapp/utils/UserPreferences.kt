package com.example.glucoapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val IS_DOCTOR_LOGGED_IN_KEY = booleanPreferencesKey("is_doctor_logged_in")
    }

    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    suspend fun setUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }

    val isDoctorLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DOCTOR_LOGGED_IN_KEY] ?: false
    }

    suspend fun setDoctorLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DOCTOR_LOGGED_IN_KEY] = isLoggedIn
        }
    }
}