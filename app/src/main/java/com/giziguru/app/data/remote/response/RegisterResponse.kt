package com.giziguru.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("rc")
    val rc: String,

    @field:SerializedName("data")
    val data: DataRegister,

    @field:SerializedName("message")
    val message: String
)

data class DataRegister(
    @field:SerializedName("token_exp")
    val tokenExp: String,

    @field:SerializedName("token")
    val token: String
)
