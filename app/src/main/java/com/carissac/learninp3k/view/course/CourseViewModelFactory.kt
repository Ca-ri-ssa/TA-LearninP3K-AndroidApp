package com.carissac.learninp3k.view.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissac.learninp3k.data.repository.CourseRepository

class CourseViewModelFactory(private val repository: CourseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}