package com.roaa.expensetracker.Model

import com.roaa.expensetracker.Composables.Navigation.Destinations

data class NavigationItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null,
    val route: Destinations
)

