package com.giziguru.app.data.remote.response.dashboard

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("rc")
    val rc: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<DataHistory>? = null,
)

data class DataHistory(
    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("history_date")
    val historyDate: String? = null,

    @field:SerializedName("total_calories")
    val totalCalories: Float,

    @field:SerializedName("total_carbs")
    val totalCarbs: Float,

    @field:SerializedName("total_protein")
    val totalProtein: Float,

    @field:SerializedName("total_fat")
    val totalFat: Float,
)