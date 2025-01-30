package com.roaa.expensetracker.Model

import androidx.compose.ui.graphics.vector.ImageVector
import com.roaa.expensetracker.Composables.Navigation.NavRoutes

data class NavigationItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: NavRoutes
)
