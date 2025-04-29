package com.carissac.learninp3k.view.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.CourseDetailResponse
import com.carissac.learninp3k.data.remote.response.CourseEnrollmentResponse
import com.carissac.learninp3k.data.remote.response.CourseIntroResponse
import com.carissac.learninp3k.data.remote.response.CourseNearestResponse
import com.carissac.learninp3k.data.remote.response.CourseResponse
import com.carissac.learninp3k.data.remote.response.CourseStatusResponse
import com.carissac.learninp3k.data.remote.response.QuizResponse
import com.carissac.learninp3k.data.repository.CourseRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val continueCourseResult: LiveData<Result<CourseStatusResponse>> = repository.continueCourseStatusResult
    val completedCourseResult: LiveData<Result<CourseStatusResponse>> = repository.completedCourseStatusResult
    val allCourseResult: LiveData<Result<CourseResponse>> = repository.allCourseResult
    val nearestCourseResult: LiveData<Result<CourseNearestResponse>> = repository.nearestCourseResult
    val introCourseResult: StateFlow<Result<CourseIntroResponse>?> = repository.introCourseFlow
    val detailCourseResult: StateFlow<Result<CourseDetailResponse>?> = repository.detailCourseFlow
    val enrollCourseResult: StateFlow<Result<CourseEnrollmentResponse>?> = repository.enrollCourseFlow
    val listCourseQuizResult: LiveData<Result<List<QuizResponse>>> = repository.listCourseQuizResult

    fun getAllCourse() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getAllCourse(token)
            }
        }
    }

    fun getCourseByContinueStatus() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getStatusCourseByContinue(token)
            }
        }
    }

    fun getCourseByCompletedStatus() {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getStatusCourseByCompleted(token)
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

    fun getIntroCourse(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getCourseIntro(token, id)
            }
        }
    }

    fun getDetailCourse(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getCourseDetail(token, id)
            }
        }
    }

    fun enrollCourse(courseId: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.enrollCourse(token, courseId)
                getIntroCourse(courseId)
            }
        }
    }

    fun clearEnrollResult() {
        repository.clearEnrollResult()
    }

    fun getCourseQuiz(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.getCourseQuiz(token, id)
            }
        }
    }
}