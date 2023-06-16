package com.giziguru.app.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.giziguru.app.data.local.AuthenticationPreferencesDataSource
import com.giziguru.app.data.remote.response.users.AddMealsResponse
import com.giziguru.app.data.remote.response.users.ProfileResponse
import com.giziguru.app.data.remote.retrofit.ApiService
import com.giziguru.app.utils.Constanta.AddMeals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
    context: Context,
) {
    @SuppressLint("HardwareIds")
    private val androidId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    suspend fun getMyProfile(): Flow<Result<ProfileResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getMyProfile(androidId, token!!)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    suspend fun addMeals(meals: AddMeals): Flow<Result<AddMealsResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.addMeals(androidId, token!!, meals)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
}