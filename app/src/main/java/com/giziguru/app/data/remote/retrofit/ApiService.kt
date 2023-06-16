package com.giziguru.app.data.remote.retrofit

import com.giziguru.app.data.remote.request.LoginRequest
import com.giziguru.app.data.remote.request.RegisterRequest
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.data.remote.response.dashboard.NutritionResponse
import com.giziguru.app.data.remote.response.dashboard.SuggestionResponse
import com.giziguru.app.data.remote.response.meal.MealsResponse
import com.giziguru.app.data.remote.response.meal.SingleMealsResponse
import com.giziguru.app.data.remote.response.session.LoginResponse
import com.giziguru.app.data.remote.response.session.LogoutResponse
import com.giziguru.app.data.remote.response.session.RegisterResponse
import com.giziguru.app.data.remote.response.users.AddMealsResponse
import com.giziguru.app.data.remote.response.users.ProfileResponse
import com.giziguru.app.utils.Constanta
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("session/login")
    suspend fun doLogin(
        @Header("Deviceid") deviceId: String,
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @POST("session/registration")
    suspend fun doRegister(
        @Header("Deviceid") deviceId: String,
        @Body registerRequest: RegisterRequest,
    ): RegisterResponse

    @POST("session/logout")
    suspend fun doLogout(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): LogoutResponse

    @GET("dashboard/akg")
    suspend fun getAkg(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): NutritionResponse

    @GET("history/mini")
    suspend fun getMiniHistory(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): HistoryResponse

    @GET("suggestion/mini")
    suspend fun getMiniSuggestion(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): SuggestionResponse

    @GET("user/my")
    suspend fun getMyProfile(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): ProfileResponse

    @PUT("user/update")
    suspend fun updateMyProfile(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    )

    @PUT("user/update/password")
    suspend fun updateMyPassword(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
        @Body password: Constanta.UpdatePassword,
    )

    @POST("user/add/meals")
    suspend fun addMeals(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
        @Body meals: Constanta.AddMeals,
    ): AddMealsResponse

    @GET("meal/")
    suspend fun getMeals(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
    ): MealsResponse

    @GET("meal/single/{mealId}")
    suspend fun getSingleMeals(
        @Header("Deviceid") deviceId: String,
        @Header("Token") token: String,
        @Path("mealId") mealId: String,
    ): SingleMealsResponse
}