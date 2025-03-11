package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserModel
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.LoginResult
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult:LiveData<Result<LoginResponse>> = _loginResult

    suspend fun register(name: String, email: String, password: String) {
        _isLoading.postValue(true)
        try {
            val response: Response<RegisterResponse> = apiService.register(name, email, password)

            if(response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    _registerResult.postValue(Result.success(registerResponse))
                } else {
                    _registerResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Registrasi gagal"
                _registerResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _registerResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _registerResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun login(email: String, password: String) {
        _isLoading.postValue(true)
        try {
            val response: Response<LoginResponse> = apiService.login(email, password)
            if(response.isSuccessful) {
                val loginResponse = response.body()
                if(loginResponse?.token != null) {
                    val user = loginResponse.loginResult ?: LoginResult()

                    val userSession = UserModel(
                        user.userId ?: -1,
                        user.userName ?: "Unknown",
                        user.userEmail ?: "Unknown",
                        loginResponse.token
                    )
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        userPreference.saveUserSession(userSession)
                    }

                    _loginResult.postValue(Result.success(loginResponse))
                } else {
                    _loginResult.postValue(Result.failure(Exception("Response kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Login gagal"
                _loginResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _loginResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _loginResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun logout() {
        userPreference.clearSession()
    }

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}