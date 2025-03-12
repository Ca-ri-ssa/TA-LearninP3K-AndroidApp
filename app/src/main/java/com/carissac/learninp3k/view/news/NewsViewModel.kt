package com.carissac.learninp3k.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.SearchNewsResponse
import com.carissac.learninp3k.data.repository.NewsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val searchNewsResult: LiveData<Result<SearchNewsResponse>> = repository.searchNewsResult

    fun searchNews(newsTitle: String) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if (token.isNotEmpty()) {
                repository.searchNews(token, newsTitle)
            }
        }
    }
}