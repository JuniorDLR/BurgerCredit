package com.theburgerclub.burgercredit.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
        private val SAVED_USERNAME_KEY = stringPreferencesKey("saved_username")
        private val SAVED_PASSWORD_KEY = stringPreferencesKey("saved_password")
    }

    val rememberMe: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[REMEMBER_ME_KEY] ?: false
    }

    val savedUsername: Flow<String> = dataStore.data.map { preferences ->
        preferences[SAVED_USERNAME_KEY] ?: ""
    }

    val savedPassword: Flow<String> = dataStore.data.map { preferences ->
        preferences[SAVED_PASSWORD_KEY] ?: ""
    }

    suspend fun setRememberMe(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_ME_KEY] = enabled
        }
    }

    suspend fun saveCredentials(username: String, password: String) {
        dataStore.edit { preferences ->
            preferences[SAVED_USERNAME_KEY] = username
            preferences[SAVED_PASSWORD_KEY] = password
        }
    }

    suspend fun clearCredentials() {
        dataStore.edit { preferences ->
            preferences.remove(SAVED_USERNAME_KEY)
            preferences.remove(SAVED_PASSWORD_KEY)
            preferences[REMEMBER_ME_KEY] = false
        }
    }
} 