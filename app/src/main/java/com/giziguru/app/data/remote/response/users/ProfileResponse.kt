package com.giziguru.app.data.remote.response.users

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: DataProfile,
)

data class DataProfile(

    @field:SerializedName("image_profile")
    val image: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("body_weight")
    val bodyWeight: Int,

    @field:SerializedName("body_height")
    val bodyHeight: Int,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("is_pregnant")
    val isPregnant: Int? = null,
)