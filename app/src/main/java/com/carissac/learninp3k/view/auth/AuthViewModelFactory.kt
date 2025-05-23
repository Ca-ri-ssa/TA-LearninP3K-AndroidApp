package com.carissac.learninp3k.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carissac.learninp3k.data.repository.UserRepository

class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}