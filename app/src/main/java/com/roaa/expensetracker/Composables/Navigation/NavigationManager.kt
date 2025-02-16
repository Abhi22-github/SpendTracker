package com.roaa.expensetracker.Composables.Navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

class NavigationManager(val navController: NavController) {
    fun navigateTo(screen: RootScreen) {
        val navOptions = NavOptions.Builder()
            //.setPopUpTo(RootScreen.MainScreen, inclusive = false) // Pop up to MainScreen
            .build()

        navController.navigate(screen, navOptions)
    }
//    fun navigateTo(screen: FullScreenNavRoutes) {
//        val navOptions = NavOptions.Builder()
//           // .setPopUpTo(RootScreen.MainScreen, inclusive = false) // Pop up to MainScreen
//            .build()
//
//        navController.navigate(screen, navOptions)
//    }
    fun navigateTo(screen: SectionScreenNavRoutes) {
        val navOptions = NavOptions.Builder()
            //.setPopUpTo(RootScreen.MainScreen, inclusive = false) // Pop up to MainScreen
            .build()

        navController.navigate(screen, navOptions)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}