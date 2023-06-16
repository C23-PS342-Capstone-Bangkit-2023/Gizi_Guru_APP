package com.giziguru.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giziguru.app.data.remote.SessionRepository
import com.giziguru.app.data.remote.UserRepository
import com.giziguru.app.data.remote.response.session.LogoutResponse
import com.giziguru.app.data.remote.response.users.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    suspend fun getMyProfile(): Flow<Result<ProfileResponse>> = userRepository.getMyProfile()

    suspend fun doLogout(): Flow<Result<LogoutResponse>> = sessionRepository.userLogout()

    suspend fun saveAuthToken(token: String) {
        viewModelScope.launch {
            sessionRepository.saveAuthToken(token)
        }
    }
}