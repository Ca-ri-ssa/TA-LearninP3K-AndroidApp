package com.carissac.learninp3k.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.repository.UserRepository
import com.carissac.learninp3k.view.utils.Event
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val registerResult: LiveData<Result<RegisterResponse>> = repository.registerResult
    val loginResult: LiveData<Event<Result<LoginResponse>>> = repository.loginResult

    val sessionToken = repository.getUserSession().asLiveData()

    fun userRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password)
        }
    }

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}