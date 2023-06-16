package com.giziguru.app.data.remote.response.dashboard

import com.google.gson.annotations.SerializedName

data class NutritionResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: DataNutrition? = null,
)

data class DataNutrition(

    @field:SerializedName("akg")
    val akg: Int,

    @field:SerializedName("calories")
    val calories: Float,

    @field:SerializedName("carb")
    val carb: Float,

    @field:SerializedName("protein")
    val protein: Float,

    @field:SerializedName("fat")
    val fat: Float,

    @field:SerializedName("water")
    val water: Int,
)