package com.giziguru.app.data.remote.response.dashboard

import com.google.gson.annotations.SerializedName

data class SuggestionResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<DataSuggestions>? = null,
)

data class DataSuggestions(
    @field:SerializedName("meal_id")
    val mealId: String? = null,

    @field:SerializedName("meal_name")
    val mealName: String? = null,

    @field:SerializedName("meal_image")
    val mealImage: String? = null,

    @field:SerializedName("calories")
    val calories: Float,

    @field:SerializedName("carb")
    val carb: Float,

    @field:SerializedName("protein")
    val protein: Float,

    @field:SerializedName("fat")
    val fat: Float,

    @field:SerializedName("tag")
    val tag: String? = null,
)