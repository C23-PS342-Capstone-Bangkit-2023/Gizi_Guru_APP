package com.giziguru.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.giziguru.data.local.AuthenticationPreferencesDataSource
import com.giziguru.data.remote.response.LoginResponse
import com.giziguru.data.remote.response.RegisterResponse
import com.giziguru.data.remote.retrofit.ApiService
import com.giziguru.utils.Constanta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(
    private val apiService: ApiService,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
    context: Context
) {
    @SuppressLint("HardwareIds")
    private val androidId: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    suspend fun userLogin(
        username: String,
        password: String
    ): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.doLogin(androidId, username, password)
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
        isPregnant: Int = 0
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.doRegister(androidId, name, username, password, age, bodyWeight, bodyHeight, gender, isPregnant)
            emit(Result.success(response))
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