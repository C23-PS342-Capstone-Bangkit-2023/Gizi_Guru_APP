package com.giziguru.app.data.remote.response.meal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class YourMealsResponse(

    @field:SerializedName("meal_id")
    val meals_id: String? = null,

    @field:SerializedName("serve")
    val serve: Int,

    val food_name: String? = null,

    val food_name_value: String? = null,

    val food_image: String? = null,
) : Parcelable