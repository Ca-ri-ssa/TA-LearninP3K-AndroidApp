package com.carissac.learninp3k.view.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissac.learninp3k.data.repository.NewsRepository

class NewsViewModelFactory(private val repository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}