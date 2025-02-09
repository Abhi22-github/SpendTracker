package com.roaa.expensetracker.Composables.Navigation

import kotlinx.serialization.Serializable


sealed class RootScreen() {

    @Serializable
    data object MainScreen : RootScreen()

    @Serializable
    data object BudgetScreen : RootScreen()

    @Serializable
    data object CategoryScreen : RootScreen()

    @Serializable
    data class CategoryDetailsScreen(
        val categoryId: Long,
        val categoryName: String,
        val categoryIconNumber: Int,
        val categoryType: String
    ) : RootScreen()

    @Serializable
    data object BankAccountScreen : RootScreen()

    @Serializable
    data object SettingScreen : RootScreen()

    @Serializable
    data class DetailsScreen(val amount: Float, val categoryName: String) : RootScreen()

}

sealed class SectionScreenNavRoutes() {

    @Serializable
    data object ListScreen : SectionScreenNavRoutes()

    @Serializable
    data object MonthScreen : SectionScreenNavRoutes()

    @Serializable
    data class DayScreen(val date: Long) : SectionScreenNavRoutes()

}
//
//@Serializable
//sealed class FullScreenNavRoutes() {
//
//    @Serializable
//    data object BudgetScreen : FullScreenNavRoutes()
//
//    @Serializable
//    data object CategoryScreen : FullScreenNavRoutes()
//
//    @Serializable
//    data class CategoryDetailsScreen(
//        val categoryId: Long,
//        val categoryName: String,
//        val categoryIconNumber: Int,
//        val categoryType: String
//    ) : FullScreenNavRoutes()
//
//    @Serializable
//    data object BankAccountScreen : FullScreenNavRoutes()
//
//    @Serializable
//    data object SettingScreen : FullScreenNavRoutes()
//
//    @Serializable
//    data object DetailsScreen : FullScreenNavRoutes()
//}
//
//
//

