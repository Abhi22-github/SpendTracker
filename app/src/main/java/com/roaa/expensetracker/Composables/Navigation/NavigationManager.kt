package com.roaa.expensetracker.Composables.Navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

class NavigationManager(val navController: NavController) {
    fun navigateTo(screen: NavRoutes) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(NavRoutes.MainScreen, inclusive = false) // Pop up to MainScreen
            .build()

        navController.navigate(screen, navOptions)
    }

    fun navigateCategory() {
        navController.popBackStack()
    }
}