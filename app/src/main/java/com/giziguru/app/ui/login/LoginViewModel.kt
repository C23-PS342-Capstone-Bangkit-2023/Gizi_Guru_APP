package com.giziguru.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giziguru.app.data.remote.SessionRepository
import com.giziguru.app.data.remote.response.session.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private var sessionRepository: SessionRepository) :
    ViewModel() {
    suspend fun userLogin(username: String, password: String): Flow<Result<LoginResponse>> {
        return sessionRepository.userLogin(username, password)
    }

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            sessionRepository.saveAuthToken(token)
        }
    }
}



