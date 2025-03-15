package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
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

    suspend fun getUserBadge(token: String) {

    }

    suspend fun getBadgeDetail(token: String, id: Int) {

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