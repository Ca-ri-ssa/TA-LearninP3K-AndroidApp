package com.carissac.learninp3k.data.di

import android.content.Context
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.local.dataStore
import com.carissac.learninp3k.data.remote.retrofit.ApiConfig
import com.carissac.learninp3k.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, userPreference)
    }
}