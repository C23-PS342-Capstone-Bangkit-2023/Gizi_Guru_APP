package com.giziguru.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giziguru.data.remote.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    suspend fun userLogin(email: String, password: String) =
        authRepository.userLogin(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}


