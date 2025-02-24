package com.carissac.learninp3k.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveUserSession(user: UserModel) {
        dataStore.edit { pref ->
            pref[USER_ID] = user.id
            pref[USER_NAME] = user.name
            pref[USER_EMAIL] = user.email
            pref[USER_TOKEN] = user.token
        }
    }

    fun getUserId(): Flow<Int> {
        return dataStore.data.map { pref ->
            pref[USER_ID] ?: -1
        }
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_NAME] ?: ""
        }
    }

    fun getUserEmail(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_EMAIL] ?: ""
        }
    }

    fun getUserToken(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_TOKEN] ?: ""
        }
    }

    suspend fun clearSession() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_TOKEN = stringPreferencesKey("token")

        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}