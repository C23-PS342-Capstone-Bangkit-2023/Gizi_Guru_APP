package com.giziguru.app.ui.explore

import androidx.lifecycle.ViewModel
import com.giziguru.app.data.remote.MealsRepository
import com.giziguru.app.data.remote.response.meal.MealsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
) : ViewModel() {
    suspend fun getExplore(): Flow<Result<MealsResponse>> =
        mealsRepository.getMeals()
}