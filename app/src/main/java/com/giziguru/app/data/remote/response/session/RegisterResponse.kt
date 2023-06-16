package com.giziguru.app.data.remote.response.session

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("data")
    val data: DataRegister,

    @field:SerializedName("message")
    val message: String? = null,
)

data class DataRegister(
    @field:SerializedName("token_exp")
    val tokenExp: String? = null,

    @field:SerializedName("token")
    val token: String? = null,
)
