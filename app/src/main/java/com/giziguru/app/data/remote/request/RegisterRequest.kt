package com.giziguru.app.data.remote.request

import com.giziguru.app.utils.Constanta
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("age") val age: Int,
    @SerializedName("body_weight") val bodyWeight: Int,
    @SerializedName("body_height") val bodyHeight: Int,
    @SerializedName("gender") val gender: Constanta.Gender,
    @SerializedName("is_pregnant") val isPregnant: Int = 0,
)
