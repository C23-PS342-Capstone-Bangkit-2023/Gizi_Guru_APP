package com.giziguru.app.data.remote.response.session

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("data")
    val data: DataLogin? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

data class DataLogin(
    @field:SerializedName("token_exp")
    val tokenExp: String? = null,

    @field:SerializedName("token")
    val token: String? = null,
)
