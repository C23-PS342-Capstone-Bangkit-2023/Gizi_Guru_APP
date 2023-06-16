package com.giziguru.app.data.remote.response.session

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)