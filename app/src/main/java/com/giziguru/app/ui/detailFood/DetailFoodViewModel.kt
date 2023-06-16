package com.giziguru.app.ui.detailFood

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.remote.MealsRepository
import com.giziguru.app.data.remote.response.meal.SingleMealsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailFoodViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
) : ViewModel() {
    suspend fun getSingleMeals(mealId: String): Flow<Result<SingleMealsResponse>> =
        mealsRepository.getSingleMeals(mealId)
}