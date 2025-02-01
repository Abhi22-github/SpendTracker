package com.roaa.expensetracker.Composables.Navigation

import kotlinx.serialization.Serializable

sealed class NavRoutes {
    @Serializable
    data object ScreenA : NavRoutes()

    @Serializable
    data class ScreenB(
        val categoryId: Long,
        val categoryName: String,
        val categoryIconNumber: Int,
        val categoryType: String
    ) : NavRoutes()

    @Serializable
    data object MainScreen : NavRoutes()

    @Serializable
    data object MonthScreen : NavRoutes()

    @Serializable
    data class DayScreen(val date: Long) : NavRoutes()

    @Serializable
    data object BankAccountScreen : NavRoutes()

    @Serializable
    data object SettingScreen : NavRoutes()
}




