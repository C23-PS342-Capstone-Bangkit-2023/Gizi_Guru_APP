package com.giziguru.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giziguru.data.remote.AuthRepository
import com.giziguru.utils.Constanta
import kotlinx.coroutines.launch


class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    suspend fun userRegister(
        name: String,
        username: String,
        password: String,
        age: Int,
        bodyWeight: Int,
        bodyHeight: Int,
        gender: Constanta.Gender,
        isPregnant: Int = 0
    ) = authRepository.userRegister(name, username, password, age, bodyWeight, bodyHeight, gender, isPregnant)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}