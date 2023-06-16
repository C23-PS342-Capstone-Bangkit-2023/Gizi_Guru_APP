package com.giziguru.app.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giziguru.app.data.remote.SessionRepository
import com.giziguru.app.utils.Constanta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val sessionRepository: SessionRepository) :
    ViewModel() {
    suspend fun userRegister(
        name: String,
        username: String,
        password: String,
        age: Int,
        bodyWeight: Int,
        bodyHeight: Int,
        gender: Constanta.Gender,
        isPregnant: Int = 0,
    ) = sessionRepository.userRegister(
        name,
        username,
        password,
        age,
        bodyWeight,
        bodyHeight,
        gender,
        isPregnant
    )

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            sessionRepository.saveAuthToken(token)
        }
    }
}