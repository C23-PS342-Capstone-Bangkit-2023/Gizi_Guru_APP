package com.giziguru.app.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.giziguru.app.data.local.AuthenticationPreferencesDataSource
import com.giziguru.app.data.remote.response.meal.MealsResponse
import com.giziguru.app.data.remote.response.meal.SingleMealsResponse
import com.giziguru.app.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MealsRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
    context: Context,
) {
    @SuppressLint("HardwareIds")
    private val androidId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    suspend fun getMeals(): Flow<Result<MealsResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getMeals(androidId, token!!)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getSingleMeals(mealId: String): Flow<Result<SingleMealsResponse>> = flow {
        val token = preferencesDataSource.getAuthToken().firstOrNull()
        try {
            val response = apiService.getSingleMeals(androidId, token!!, mealId)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


}