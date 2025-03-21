package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.AllAttemptResponseItem
import com.carissac.learninp3k.data.remote.response.SubmitAttemptRequest
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import com.carissac.learninp3k.view.quiz.CooldownPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class AttemptRepository (
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val cooldownPreference: CooldownPreference
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _submitQuizResult = MutableLiveData<Result<TakeAttemptResponse>>()
    val submitQuizResult: LiveData<Result<TakeAttemptResponse>> = _submitQuizResult

    private val _allAttemptResult = MutableLiveData<Result<List<AllAttemptResponseItem>>>()
    val allAttemptResult: LiveData<Result<List<AllAttemptResponseItem>>> = _allAttemptResult

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

    suspend fun saveCooldown(courseId: Int, cooldownTime: Long) {
        cooldownPreference.saveCooldownPeriod(courseId, cooldownTime)
    }

    suspend fun clearCooldown(courseId: Int) {
        cooldownPreference.clearCooldown(courseId)
    }


    fun getCooldownTime(courseId: Int): Flow<Long?> {
        return cooldownPreference.getCooldownTime(courseId)
    }

    fun isCooldownActive(courseId: Int): Flow<Boolean> {
        return cooldownPreference.isCooldownActive(courseId)
    }

    suspend fun takeAttempt(token: String, id: Int, submitAttemptRequest: SubmitAttemptRequest) {
        _isLoading.postValue(true)
        try {
            val response = apiService.takeAttempt("Bearer $token", id, submitAttemptRequest)
            if(response.isSuccessful) {
                val submitCourseResponse = response.body()
                if(submitCourseResponse != null) {
                    _submitQuizResult.postValue(Result.success(submitCourseResponse))
                } else {
                    _submitQuizResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data leaderboard"
                _submitQuizResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _submitQuizResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _submitQuizResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllAttemptSession(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getAllAttemptSession("Bearer $token", id)
            if(response.isSuccessful) {
                val allAttemptResponse = response.body()
                if(allAttemptResponse != null) {
                    _allAttemptResult.postValue(Result.success(allAttemptResponse))
                } else {
                    _allAttemptResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data leaderboard"
                _allAttemptResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _allAttemptResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _allAttemptResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getDetailAttempt(token: String, id: Int, sessionId: Int) {

    }

    companion object {
        @Volatile
        private var instance: AttemptRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            cooldownPreference: CooldownPreference
        ): AttemptRepository =
            instance ?: synchronized(this) {
                instance ?: AttemptRepository(apiService, userPreference, cooldownPreference)
            }.also { instance = it }
    }
}