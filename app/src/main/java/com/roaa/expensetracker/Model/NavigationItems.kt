package com.roaa.expensetracker.Model

import com.roaa.expensetracker.Composables.Navigation.RootScreen
import com.roaa.expensetracker.Composables.Navigation.SectionScreenNavRoutes

data class NavigationItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null,
    val route: SectionScreenNavRoutes
)
data class NavigationItems2(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null,
    val route: RootScreen
)
