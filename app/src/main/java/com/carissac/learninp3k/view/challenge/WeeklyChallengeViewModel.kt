package com.carissac.learninp3k.view.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.TakeWeeklyChallengeResponse
import com.carissac.learninp3k.data.remote.response.WeeklyChallengeResponse
import com.carissac.learninp3k.data.repository.LeaderboardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WeeklyChallengeViewModel(private val repository: LeaderboardRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    val weeklyChallengeResult: LiveData<Result<WeeklyChallengeResponse>> = repository.weeklyChallengeResult
    val takeWeeklyChallengeResult: LiveData<Result<TakeWeeklyChallengeResponse>> = repository.takeWeeklyChallengeResult

    fun getWeeklyChallenge() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getWeeklyChallenge(token)
            }
        }
    }

    fun takeWeeklyChallenge(challengeId: Int, userAnswer: String) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.takeWeeklyChallenge(token, challengeId, userAnswer)
            }
        }
    }
}