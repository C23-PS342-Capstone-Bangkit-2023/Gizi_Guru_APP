package com.giziguru.app.data.remote.response.users

import com.google.gson.annotations.SerializedName

data class AddMealsResponse(

    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)