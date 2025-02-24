package com.carissac.learninp3k.view.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")
class ThemePreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { pref ->
            pref[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")

        @Volatile
        private var INSTANCE: ThemePreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}