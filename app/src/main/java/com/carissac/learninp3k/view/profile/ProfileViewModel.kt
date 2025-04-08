package com.carissac.learninp3k.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.AvatarDetailResponse
import com.carissac.learninp3k.data.remote.response.AvatarResponseItem
import com.carissac.learninp3k.data.remote.response.ProfileResponse
import com.carissac.learninp3k.data.remote.response.ProfileResultResponse
import com.carissac.learninp3k.data.remote.response.UserLeaderboardResponse
import com.carissac.learninp3k.data.repository.UserRepository
import com.carissac.learninp3k.view.utils.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val userLeaderboardResult: LiveData<Result<UserLeaderboardResponse>> = repository.userLeaderboardResult
    val profileResult: LiveData<Result<ProfileResponse>> = repository.profileResult
    val listAvatarResult: LiveData<Result<List<AvatarResponseItem>>> = repository.listAvatarResult
    val avatarResult: LiveData<Result<AvatarDetailResponse>> = repository.avatarResult

    val userName: LiveData<String?> = repository.getUserName().asLiveData()

    private val _updateProfileResult = MutableLiveData<Event<Result<ProfileResultResponse>>>()
    val updateProfileResult: LiveData<Event<Result<ProfileResultResponse>>> = _updateProfileResult

    fun getUserLeaderboard() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getUserLeaderboard(token)
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getProfile(token)
            }
        }
    }

    fun getAllAvatar() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getAllAvatar(token)
            }
        }
    }

    fun getDetailAvatar(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getDetailAvatar(token, id)
            }
        }
    }

    fun updateProfile(name: String, email: String, avatarId: Int?) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if (token.isNotEmpty()) {
                try {
                    val response = repository.updateProfile(token, name, email, avatarId)
                    _updateProfileResult.postValue(Event(Result.success(response)))
                } catch (e: Exception) {
                    _updateProfileResult.postValue(Event(Result.failure(e)))
                }
            }
        }
    }
}