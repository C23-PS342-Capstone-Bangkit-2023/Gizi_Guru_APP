package com.giziguru.app.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.giziguru.app.data.local.AuthenticationPreferencesDataSource
import com.giziguru.app.data.remote.request.LoginRequest
import com.giziguru.app.data.remote.request.RegisterRequest
import com.giziguru.app.data.remote.response.session.LoginResponse
import com.giziguru.app.data.remote.response.session.LogoutResponse
import com.giziguru.app.data.remote.response.session.RegisterResponse
import com.giziguru.app.data.remote.retrofit.ApiService
import com.giziguru.app.utils.Constanta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
    private val context: Context,
) {
    @SuppressLint("HardwareIds")
    private val androidId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    suspend fun userLogin(
        username: String,
        password: String,
    ): Flow<Result<LoginResponse>> = flow {
        try {
            val loginRequest = LoginRequest(username, password)
            val response = apiService.doLogin(androidId, loginRequest)
            response.data?.token?.let { token ->
                preferencesDataSource.saveAuthToken(token)
            }
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(
        name: String,
        username: String,
        password: String,
        age: Int,
        bodyWeight: Int,
        bodyHeight: Int,
        gender: Constanta.Gender,
        isPregnant: Int = 0,
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val registerRequest = RegisterRequest(
                name,
                username,
                password,
                age,
                bodyWeight,
                bodyHeight,
                gender,
                isPregnant
            )
            val response = apiService.doRegister(androidId, registerRequest)
            response.data.token?.let { token ->
                preferencesDataSource.saveAuthToken(token)
            }
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userLogout(): Flow<Result<LogoutResponse>> = flow {
        try {
            val token = preferencesDataSource.getAuthToken().firstOrNull()
            if (token != null) {
                val response = apiService.doLogout(androidId, token)
                emit(Result.success(response))
                preferencesDataSource.clearAuthToken()
            } else {
                emit(Result.failure(Exception("Token not found")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        preferencesDataSource.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> = preferencesDataSource.getAuthToken()
}