package com.roaa.expensetracker.Composables.Navigation

import kotlinx.serialization.Serializable


sealed class NavRoutes() {
    @Serializable
    data object HomeScreen : NavRoutes()

    @Serializable
    data object AppScreen : NavRoutes()
}
sealed class Destinations(){

    @Serializable
    data object BudgetScreen : Destinations()

    @Serializable
    data object CategoryScreen : Destinations()

    @Serializable
    data class CategoryDetailsScreen(
        val categoryId: Long,
        val categoryName: String,
        val categoryIconNumber: Int,
        val categoryType: String
    ) : Destinations()

    @Serializable
    data object BankAccountScreen : Destinations()

    @Serializable
    data object SettingScreen : Destinations()

    @Serializable
    data class DetailsScreen(val amount: Float, val categoryName: String) : Destinations()

    @Serializable
    data object ListScreen : Destinations()

    @Serializable
    data object MonthScreen : Destinations()

    @Serializable
    data class DayScreen(val date: Long) : Destinations()

}


