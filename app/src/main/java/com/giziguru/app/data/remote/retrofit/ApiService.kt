package com.giziguru.data.remote.retrofit

import com.giziguru.data.remote.response.LoginResponse
import com.giziguru.data.remote.response.RegisterResponse
import com.giziguru.utils.Constanta
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("session/login")
    @FormUrlEncoded
    fun doLogin(
        @Header("Deviceid") deviceId: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("session/registration")
    @FormUrlEncoded
    fun doRegister(
        @Header("Deviceid") deviceId: String,
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("age") age: Int,
        @Field("body_weight") bodyWeight: Int,
        @Field("body_height") bodyHeight: Int,
        @Field("gender") gender: Constanta.Gender,
        @Field("is_pregnant") isPregnant: Int = 0
    ): RegisterResponse
}