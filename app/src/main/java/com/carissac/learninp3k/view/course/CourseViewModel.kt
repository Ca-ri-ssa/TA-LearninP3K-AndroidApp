package com.carissac.learninp3k.view.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.CourseNearestResponse
import com.carissac.learninp3k.data.remote.response.CourseResponse
import com.carissac.learninp3k.data.remote.response.CourseStatusResponse
import com.carissac.learninp3k.data.repository.CourseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val listCourseStatusResult: LiveData<Result<CourseStatusResponse>> = repository.listCourseStatusResult
    val allCourseResult: LiveData<Result<CourseResponse>> = repository.allCourseResult
    val nearestCourseResult: LiveData<Result<CourseNearestResponse>> = repository.nearestCourseResult

    fun getAllCourse() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getAllCourse(token)
            }
        }
    }

    fun getCourseByStatus(status: String) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getCourseByStatus(token, status)
            }
        }
    }

    fun getNearestCourse() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getNearestCourse(token)
            }
        }
    }
}