package com.giziguru.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthenticationPreferencesDataSource(private val dataStore: DataStore<Preferences>) {

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_data")

        fun create(dataStore: DataStore<Preferences>): AuthenticationPreferencesDataSource {
            return AuthenticationPreferencesDataSource(dataStore)
        }
    }
}
