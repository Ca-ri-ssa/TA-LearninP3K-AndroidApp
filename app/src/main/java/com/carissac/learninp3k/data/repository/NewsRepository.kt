package com.carissac.learninp3k.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carissac.learninp3k.data.local.UserPreference
import com.carissac.learninp3k.data.remote.response.NewsResponseItem
import com.carissac.learninp3k.data.remote.response.SearchNewsResponse
import com.carissac.learninp3k.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listNewsResult = MutableLiveData<Result<List<NewsResponseItem>>>()
    val listNewsResult: LiveData<Result<List<NewsResponseItem>>> = _listNewsResult

    private val _newsResult = MutableLiveData<Result<NewsResponseItem>>()
    val newsResult: LiveData<Result<NewsResponseItem>> = _newsResult

    private val _searchNewsResult = MutableLiveData<Result<SearchNewsResponse>>()
    val searchNewsResult: LiveData<Result<SearchNewsResponse>> = _searchNewsResult

    fun getUserSession(): Flow<String?> {
        return userPreference.getUserToken()
    }

    suspend fun getAllNews(token: String) {
        _isLoading.postValue(true)
        try {

        } catch (e: IOException) {
            _listNewsResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _listNewsResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getDetailNews(token: String, id: Int) {
        _isLoading.postValue(true)
        try {

        } catch (e: IOException) {
            _newsResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _newsResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun searchNews(token: String, newsTitle: String) {
        _isLoading.postValue(true)
        try {
            val response = apiService.searchNews("Bearer $token", newsTitle)
            if(response.isSuccessful) {
                val searchNewsResponse = response.body()
                if(searchNewsResponse != null) {
                    _searchNewsResult.postValue(Result.success(searchNewsResponse))
                } else {
                    _searchNewsResult.postValue(Result.failure(Exception("Respon kosong dari server")))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Gagal mencari berita"
                _searchNewsResult.postValue(Result.failure(Exception(errorMessage)))
            }
        } catch (e: IOException) {
            _searchNewsResult.postValue(Result.failure(Exception("Gagal terhubung ke server")))
        } catch (e: HttpException) {
            _searchNewsResult.postValue(Result.failure(Exception("Terjadi kesalahan server")))
        } finally {
            _isLoading.postValue(false)
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, userPreference)
            }.also { instance = it }
    }
}