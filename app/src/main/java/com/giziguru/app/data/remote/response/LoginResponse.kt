package com.giziguru.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("rc")
    val rc: String,

    @field:SerializedName("data")
    val data: DataLogin,

    @field:SerializedName("message")
    val message: String
)

data class DataLogin(
    @field:SerializedName("token_exp")
    val tokenExp: String,

    @field:SerializedName("token")
    val token: String
)
