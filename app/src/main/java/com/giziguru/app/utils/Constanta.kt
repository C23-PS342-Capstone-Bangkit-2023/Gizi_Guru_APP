package com.giziguru.app.utils

object Constanta {
    enum class Gender {
        Male,
        Female
    }

    data class UpdatePassword(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String,
    )

    data class AddMeals(
        val title: String,
        val history_date: String,
        val details: List<MealDetail>,
    )

    data class MealDetail(
        val meal_id: String,
        val serve: Int,
    )

    data class MealName(
        val name: String,
    )
}