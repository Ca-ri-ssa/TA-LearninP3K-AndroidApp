package com.carissac.learninp3k.view.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponseItem
import com.carissac.learninp3k.data.repository.LeaderboardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val repository: LeaderboardRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val topPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = repository.topPlayerResult
    val underTopPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = repository.underTopPlayerResult
    val listBadgeResult: LiveData<Result<List<UserBadgeResponseItem>>> = repository.listBadgeResult
    val userBadgeResult: LiveData<Result<UserBadgeResponse>> = repository.userBadgeResult

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

    fun getAllUserBadge() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getAllUserBadge(token)
            }
        }
    }

    fun getUserBadgeById(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getBadgeDetail(token, id)
            }
        }
    }
}