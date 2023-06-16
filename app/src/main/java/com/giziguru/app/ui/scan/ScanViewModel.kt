package com.giziguru.app.ui.scan

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.remote.MealsRepository
import com.giziguru.app.data.remote.response.meal.SingleMealsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
) : ViewModel() {

    suspend fun getDataScan(mealId: String): Flow<Result<SingleMealsResponse>> =
        mealsRepository.getSingleMeals(mealId)
}