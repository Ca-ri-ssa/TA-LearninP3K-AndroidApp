package com.carissac.learninp3k.view.quiz

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cooldown_period")
class CooldownPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveCooldownPeriod(courseId: Int, time: Long) {
        val key = longPreferencesKey("cooldown_$courseId")
        dataStore.edit { preferences ->
            preferences[key] = time
        }
    }

    fun getCooldownTime(courseId: Int): Flow<Long?> {
        val key = longPreferencesKey("cooldown_$courseId")
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    fun isCooldownActive(courseId: Int): Flow<Boolean> {
        return getCooldownTime(courseId).map { cooldownEndTime ->
            cooldownEndTime != null && cooldownEndTime > System.currentTimeMillis()
        }
    }

    suspend fun clearCooldown(courseId: Int) {
        dataStore.edit { preferences ->
            preferences.remove(longPreferencesKey("cooldown_$courseId"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CooldownPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): CooldownPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = CooldownPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}