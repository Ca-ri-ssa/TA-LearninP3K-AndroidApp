package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.CourseDetailResponse
import com.carissac.learninp3k.data.remote.response.CourseEnrollmentResponse
import com.carissac.learninp3k.data.remote.response.CourseIntroResponse
import com.carissac.learninp3k.data.remote.response.CourseNearestResponse
import com.carissac.learninp3k.data.remote.response.CourseResponse
import com.carissac.learninp3k.data.remote.response.CourseStatusResponse
import com.carissac.learninp3k.data.remote.response.QuizResponse
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException

class CourseRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _allCourseResult = MutableLiveData<Result<CourseResponse>>()
    val allCourseResult: LiveData<Result<CourseResponse>> = _allCourseResult

    private val _continueCourseStatusResult = MutableLiveData<Result<CourseStatusResponse>>()
    val continueCourseStatusResult: LiveData<Result<CourseStatusResponse>> = _continueCourseStatusResult

    private val _completedCourseStatusResult = MutableLiveData<Result<CourseStatusResponse>>()
    val completedCourseStatusResult: LiveData<Result<CourseStatusResponse>> = _completedCourseStatusResult

    private val _nearestCourseResult = MutableLiveData<Result<CourseNearestResponse>>()
    val nearestCourseResult: LiveData<Result<CourseNearestResponse>> = _nearestCourseResult

    private val _introCourseFlow = MutableStateFlow<Result<CourseIntroResponse>?>(null)
    val introCourseFlow: StateFlow<Result<CourseIntroResponse>?> = _introCourseFlow.asStateFlow()

    private val _enrollCourseFlow = MutableStateFlow<Result<CourseEnrollmentResponse>?>(null)
    val enrollCourseFlow: StateFlow<Result<CourseEnrollmentResponse>?> = _enrollCourseFlow.asStateFlow()

//    private val _detailCourseResult = MutableLiveData<Result<CourseDetailResponse>>()
//    val detailCourseResult: LiveData<Result<CourseDetailResponse>> = _detailCourseResult

    private val _detailCourseFlow = MutableStateFlow<Result<CourseDetailResponse>?>(null)
    val detailCourseFlow: StateFlow<Result<CourseDetailResponse>?> = _detailCourseFlow

    private val _listCourseQuizResult = MutableLiveData<Result<List<QuizResponse>>>()
    val listCourseQuizResult: LiveData<Result<List<QuizResponse>>> = _listCourseQuizResult

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

    suspend fun getAllCourse(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getAllCourse("Bearer $token")
            if(response.isSuccessful) {
                val allCourseResponse = response.body()
                if(allCourseResponse != null) {
                    _allCourseResult.postValue(Result.success(allCourseResponse))
                } else {
                    _allCourseResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil semua data kelas"
                _allCourseResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _allCourseResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _allCourseResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun enrollCourse(token: String, courseId: Int) {
        _isLoading.postValue(true)
        try {
            _enrollCourseFlow.value = null
            val response = apiService.enrollCourse("Bearer $token", courseId)
            if (response.isSuccessful) {
                val enrollCourseResponse = response.body()
                if (enrollCourseResponse != null) {
                    _enrollCourseFlow.value = Result.success(enrollCourseResponse)
                    _introCourseFlow.value = null
                } else {
                    _enrollCourseFlow.value = Result.failure(Exception("Respon kosong dari server"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mendaftarkan kelas"
                _enrollCourseFlow.value = Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            _enrollCourseFlow.value = Result.failure(Exception("Gagal terhubung ke server"))
        } catch (e: HttpException) {
            _enrollCourseFlow.value = Result.failure(Exception("Terjadi kesalahan server"))
        } finally {
            _isLoading.postValue(false)
        }
    }

    fun clearEnrollResult() {
        _enrollCourseFlow.value = null
    }

    suspend fun getCourseIntro(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            _introCourseFlow.value = null
            val response = apiService.getIntroCourse("Bearer $token", id)
            if(response.isSuccessful) {
                val introCourseResponse = response.body()
                if(introCourseResponse != null) {
                    _introCourseFlow.value = Result.success(introCourseResponse)
                } else {
                    _introCourseFlow.value = Result.failure(Exception("Respon kosong dari server"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data intro kelas"
                _introCourseFlow.value = Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            _introCourseFlow.value = Result.failure(Exception("Gagal terhubung ke server"))
        } catch (e: HttpException) {
            _introCourseFlow.value = Result.failure(Exception("Terjadi kesalahan server"))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getCourseDetail(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            _detailCourseFlow.value = null
            val response = apiService.getCourseDetail("Bearer $token", id)
            if (response.isSuccessful) {
                val detailCourseResponse = response.body()
                if (detailCourseResponse != null) {
                    _detailCourseFlow.value = Result.success(detailCourseResponse) // Gunakan StateFlow
                } else {
                    _detailCourseFlow.value = Result.failure(Exception("Respon kosong dari server"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data detail kelas"
                _detailCourseFlow.value = Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            _detailCourseFlow.value = Result.failure(Exception("Gagal terhubung ke server"))
        } catch (e: HttpException) {
            _detailCourseFlow.value = Result.failure(Exception("Terjadi kesalahan server"))
        } finally {
            _isLoading.postValue(false)
        }
    }

//    suspend fun getCourseDetail(token: String, id: Int) {
//        _isLoading.postValue(true)
//        try {
//            val response = apiService.getCourseDetail("Bearer $token", id)
//            if(response.isSuccessful) {
//                val detailCourseResponse = response.body()
//                if(detailCourseResponse != null) {
//                    _detailCourseResult.postValue(Result.success(detailCourseResponse))
//                } else {
//                    _detailCourseResult.postValue(Result.failure(Exception("Respon kosong dari server")))
//                }
//            } else {
//                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data detail kelas"
//                _detailCourseResult.postValue(Result.failure(Exception(errorMessage)))
//            }
//        } catch (e: IOException) {
//            _detailCourseResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
//        } catch (e: HttpException) {
//            _detailCourseResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
//        } finally {
//            _isLoading.postValue(false)
//        }
//    }

    suspend fun getStatusCourseByContinue(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getCourseByStatus("Bearer $token", "in-progress")
            if(response.isSuccessful) {
                val continueCourseStatusResponse = response.body()
                if(continueCourseStatusResponse != null) {
                    _continueCourseStatusResult.postValue(Result.success(continueCourseStatusResponse))
                } else {
                    _continueCourseStatusResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data kelas status \"Sedang Belajar\""
                _continueCourseStatusResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _continueCourseStatusResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _continueCourseStatusResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getStatusCourseByCompleted(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getCourseByStatus("Bearer $token", "completed")
            if(response.isSuccessful) {
                val completedCourseStatusResponse = response.body()
                if(completedCourseStatusResponse != null) {
                    _completedCourseStatusResult.postValue(Result.success(completedCourseStatusResponse))
                } else {
                    _completedCourseStatusResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data kelas status \"Selesai Belajar\""
                _completedCourseStatusResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _completedCourseStatusResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _completedCourseStatusResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getCourseQuiz(token: String, id: Int) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getCourseQuiz("Bearer $token", id)
            if(response.isSuccessful) {
                val listQuizResponse = response.body()
                if(listQuizResponse != null) {
                    _listCourseQuizResult.postValue(Result.success(listQuizResponse))
                } else {
                    _listCourseQuizResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data kuis"
                _listCourseQuizResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _listCourseQuizResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _listCourseQuizResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getNearestCourse(token: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.getNearestCourse("Bearer $token")
            if(response.isSuccessful) {
                val nearestCourseResponse = response.body()
                if(nearestCourseResponse != null) {
                    _nearestCourseResult.postValue(Result.success(nearestCourseResponse))
                } else {
                    _nearestCourseResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mengambil data kelas status \"Sedang Belajar\""
                _nearestCourseResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _nearestCourseResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _nearestCourseResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    companion object {
        @Volatile
        private var instance: CourseRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): CourseRepository =
            instance ?: synchronized(this) {
                instance ?: CourseRepository(apiService, userPreference)
            }.also { instance = it }
    }
}