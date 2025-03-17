package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponseItem
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class LeaderboardRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _topPlayerResult = MutableLiveData<Result<List<LeaderboardResponse>>>()
    val topPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = _topPlayerResult

    private val _underTopPlayerResult = MutableLiveData<Result<List<LeaderboardResponse>>>()
    val underTopPlayerResult: LiveData<Result<List<LeaderboardResponse>>> = _underTopPlayerResult

    private val _listBadgeResult = MutableLiveData<Result<List<UserBadgeResponseItem>>>()
    val listBadgeResult: LiveData<Result<List<UserBadgeResponseItem>>> = _listBadgeResult

    private val _userBadgeResult = MutableLiveData<Result<UserBadgeResponse>>()
    val userBadgeResult: LiveData<Result<UserBadgeResponse>> = _userBadgeResult

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

    fun getUserId(): Flow<Int?> {
        return userPreference.getUserId()
    }

    suspend fun getLeaderboard(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getLeaderboard("Bearer $token")
            if(response.isSuccessful) {
                val leaderboardResponse = response.body()
                if(leaderboardResponse != null) {
                    val topPlayer = leaderboardResponse.take(3)
                    val underTopPlayer = leaderboardResponse.drop(3)

                    _topPlayerResult.postValue(Result.success(topPlayer))
                    _underTopPlayerResult.postValue(Result.success(underTopPlayer))
                } else {
                    _topPlayerResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                    _underTopPlayerResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data leaderboard"
                _topPlayerResult.postValue(Result.failure(Exception(errorMessage)))
                _underTopPlayerResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _topPlayerResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
            _underTopPlayerResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _topPlayerResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
            _underTopPlayerResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllUserBadge(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getAllUserBadge("Bearer $token")
            if(response.isSuccessful) {
                val allUserBadgeResponse = response.body()
                if(allUserBadgeResponse != null) {
                    _listBadgeResult.postValue(Result.success(allUserBadgeResponse))
                } else {
                    _listBadgeResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data leaderboard"
                _listBadgeResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _listBadgeResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _listBadgeResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getBadgeDetail(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getUserBadgeById("Bearer $token", id)
            if(response.isSuccessful) {
                val userBadgeResponse = response.body()
                if(userBadgeResponse != null) {
                    _userBadgeResult.postValue(Result.success(userBadgeResponse))
                } else {
                    _userBadgeResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data leaderboard"
                _userBadgeResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _userBadgeResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _userBadgeResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    companion object {
        @Volatile
        private var instance: LeaderboardRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): LeaderboardRepository =
            instance ?: synchronized(this) {
                instance ?: LeaderboardRepository(apiService, userPreference)
            }.also { instance = it }
    }
}