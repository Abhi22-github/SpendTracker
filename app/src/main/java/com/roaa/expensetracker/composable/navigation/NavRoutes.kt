package com.roaa.expensetracker.composable.navigation

import com.roaa.expensetracker.composable.utils.ActionTypes
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes() {
    @Serializable
    data object HomeScreen : NavRoutes()

    @Serializable
    data object AppScreen : NavRoutes()
}

@Serializable
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
    data object DetailsScreen : Destinations()

    @Serializable
    data object ListScreen : Destinations()

    @Serializable
    data object MonthScreen : Destinations()

    @Serializable
    data object StatisticsScreen : Destinations()

    @Serializable
    data class BankDetailsScreen(val bankAccountId:Long) : Destinations()

    @Serializable
    data class DayScreen(val date: Long) : Destinations()

    @Serializable
    data class BudgetSetupScreen(val type:ActionTypes,val budgetId:Long) : Destinations()

    @Serializable
    data object WelcomeScreen:Destinations()
}


