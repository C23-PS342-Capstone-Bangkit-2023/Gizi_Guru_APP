package com.giziguru.app.ui.addMeals

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.local.AuthenticationPreferencesDataSource
import com.giziguru.app.data.remote.SessionRepository
import com.giziguru.app.data.remote.UserRepository
import com.giziguru.app.data.remote.response.users.AddMealsResponse
import com.giziguru.app.data.remote.retrofit.ApiService
import com.giziguru.app.utils.Constanta.AddMeals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AddMealsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val preferencesDataSource: AuthenticationPreferencesDataSource,
) : ViewModel() {

    fun getAuthToken(): Flow<String?> = sessionRepository.getAuthToken()

    suspend fun addMeals(meals: AddMeals): Flow<Result<AddMealsResponse>> =
        userRepository.addMeals(meals)
}
