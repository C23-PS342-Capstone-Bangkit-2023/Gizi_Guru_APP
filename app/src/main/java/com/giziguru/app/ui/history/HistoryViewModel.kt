package com.giziguru.app.ui.history

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.remote.DashboardRepository
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {
    suspend fun getHistory(): Flow<Result<HistoryResponse>> = dashboardRepository.getHistory()

}