package com.carissac.learninp3k.view.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.data.repository.LeaderboardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val repository: LeaderboardRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val topPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = repository.topPlayerResult
    val underTopPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = repository.underTopPlayerResult

    fun getLeaderboard() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getLeaderboard(token)
            }
        }
    }

    suspend fun getUserId(): Int? {
       return repository.getUserId().first()
    }
}