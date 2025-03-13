package com.carissac.learninp3k.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.DetailNewsResponse
import com.carissac.learninp3k.data.remote.response.NewsResponseItem
import com.carissac.learninp3k.data.remote.response.SearchNewsResponse
import com.carissac.learninp3k.data.repository.NewsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val searchNewsResult: LiveData<Result<SearchNewsResponse>> = repository.searchNewsResult
    val newsResult: LiveData<Result<DetailNewsResponse>> = repository.newsResult
    val listNewsResult: LiveData<Result<List<NewsResponseItem>>> = repository.listNewsResult

    fun searchNews(newsTitle: String) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.searchNews(token, newsTitle)
            }
        }
    }

    fun getNewsDetail(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getDetailNews(token, id)
            }
        }
    }

    fun getAllNews() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getAllNews(token)
            }
        }
    }
}