package com.giziguru.app.data.remote.response.meal

import com.google.gson.annotations.SerializedName

data class SingleMealsResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<DataSingleMeals>? = null,
)

data class DataSingleMeals(
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