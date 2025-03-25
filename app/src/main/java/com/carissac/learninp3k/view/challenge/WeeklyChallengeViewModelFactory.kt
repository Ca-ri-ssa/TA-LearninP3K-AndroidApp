package com.carissac.learninp3k.view.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissac.learninp3k.data.repository.LeaderboardRepository

class WeeklyChallengeViewModelFactory(private val repository: LeaderboardRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeeklyChallengeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeeklyChallengeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}