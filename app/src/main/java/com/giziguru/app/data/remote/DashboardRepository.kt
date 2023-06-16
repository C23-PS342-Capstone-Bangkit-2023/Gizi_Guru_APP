package com.giziguru.app.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.giziguru.app.data.local.AuthenticationPreferencesDataSource
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.data.remote.response.dashboard.NutritionResponse
import com.giziguru.app.data.remote.response.dashboard.SuggestionResponse
import com.giziguru.app.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
    context: Context,
) {
    @SuppressLint("HardwareIds")
    private val androidId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    suspend fun getAkg(): Flow<Result<NutritionResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getAkg(androidId, token!!)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getHistory(): Flow<Result<HistoryResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getMiniHistory(androidId, token!!)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    suspend fun getSuggestion(): Flow<Result<SuggestionResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getMiniSuggestion(androidId, token!!)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
}