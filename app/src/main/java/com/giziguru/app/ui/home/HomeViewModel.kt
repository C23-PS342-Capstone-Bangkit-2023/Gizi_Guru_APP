package com.giziguru.app.ui.home

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.remote.DashboardRepository
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.data.remote.response.dashboard.NutritionResponse
import com.giziguru.app.data.remote.response.dashboard.SuggestionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    suspend fun getAkg(): Flow<Result<NutritionResponse>> = dashboardRepository.getAkg()

    suspend fun getHistory(): Flow<Result<HistoryResponse>> = dashboardRepository.getHistory()

    suspend fun getSuggestion(): Flow<Result<SuggestionResponse>> =
        dashboardRepository.getSuggestion()
}

