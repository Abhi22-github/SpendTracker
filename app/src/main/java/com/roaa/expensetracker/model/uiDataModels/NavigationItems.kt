package com.roaa.expensetracker.model.uiDataModels

import com.roaa.expensetracker.composable.navigation.Destinations

data class NavigationItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null,
    val route: Destinations
)

