package com.carissac.learninp3k.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val registerResult: LiveData<Result<RegisterResponse>> = repository.registerResult
    val loginResult: LiveData<Result<LoginResponse>> = repository.loginResult

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
}