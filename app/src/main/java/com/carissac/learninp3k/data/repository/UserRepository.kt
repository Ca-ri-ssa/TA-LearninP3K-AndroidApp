package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserModel
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.AvatarDetailResponse
import com.carissac.learninp3k.data.remote.response.AvatarResponseItem
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.LoginResult
import com.carissac.learninp3k.data.remote.response.ProfileResponse
import com.carissac.learninp3k.data.remote.response.ProfileResultResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.remote.response.UserLeaderboardResponse
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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

    private val _userLeaderboardResult = MutableLiveData<Result<UserLeaderboardResponse>>()
    val userLeaderboardResult: LiveData<Result<UserLeaderboardResponse>> = _userLeaderboardResult

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    private val _listAvatarResult = MutableLiveData<Result<List<AvatarResponseItem>>>()
    val listAvatarResult: LiveData<Result<List<AvatarResponseItem>>> = _listAvatarResult

    private val _avatarResult = MutableLiveData<Result<AvatarDetailResponse>>()
    val avatarResult: LiveData<Result<AvatarDetailResponse>> = _avatarResult

    private val _updateProfileResult = MutableLiveData<Result<ProfileResultResponse>>()
    val updateProfileResult: LiveData<Result<ProfileResultResponse>> = _updateProfileResult

    suspend fun logout() {
        userPreference.clearSession()
    }

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

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

                    withContext(Dispatchers.IO) {
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

    suspend fun getUserLeaderboard(token: String) {
        _isLoading.postValue(true)
        try {
            val response: Response<UserLeaderboardResponse> = apiService.getUserLeaderboard("Bearer $token")
            if(response.isSuccessful) {
                val userLeaderboardResponse = response.body()
                if(userLeaderboardResponse != null) {
                    _userLeaderboardResult.postValue(Result.success(userLeaderboardResponse))
                } else {
                    _userLeaderboardResult.postValue(Result.failure(Exception("Response kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil profil"
                _userLeaderboardResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _userLeaderboardResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _userLeaderboardResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getProfile(token: String) {
        _isLoading.postValue(true)
        try {
            val response: Response<ProfileResponse> = apiService.getProfile("Bearer $token")
            if(response.isSuccessful) {
                val profileResponse = response.body()
                if(profileResponse != null) {
                    _profileResult.postValue(Result.success(profileResponse))
                } else {
                    _profileResult.postValue(Result.failure(Exception("Response kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil profil"
                _profileResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _profileResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _profileResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllAvatar(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getAllAvatar("Bearer $token")
            if(response.isSuccessful) {
                val avatarList = response.body()
                if(avatarList != null) {
                    _listAvatarResult.postValue(Result.success(avatarList))
                } else {
                    _listAvatarResult.postValue(Result.failure(Exception("Response kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil avatar"
                _listAvatarResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _listAvatarResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _listAvatarResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getDetailAvatar(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getAvatarDetail("Bearer $token", id)
            if(response.isSuccessful) {
                val avatarDetail = response.body()
                if(avatarDetail != null) {
                    _avatarResult.postValue(Result.success(avatarDetail))
                } else {
                    _avatarResult.postValue(Result.failure(Exception("Response kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil detail avatar"
                _avatarResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _avatarResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _avatarResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun updateProfile(token: String, name: String, email: String, avatarId: Int?) {
        _isLoading.postValue(true)
        try {
            val response: Response<ProfileResultResponse> = apiService.updateProfile("Bearer $token", name, email, avatarId)
            if(response.isSuccessful) {
                val updateResponse = response.body()
                if(updateResponse != null) {
                    withContext(Dispatchers.IO) {
                        userPreference.updateUserProfile(name, email)
                    }
                    _updateProfileResult.postValue(Result.success(updateResponse))
                } else {
                    _updateProfileResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal memperbarui profil"
                _updateProfileResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _updateProfileResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _updateProfileResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
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