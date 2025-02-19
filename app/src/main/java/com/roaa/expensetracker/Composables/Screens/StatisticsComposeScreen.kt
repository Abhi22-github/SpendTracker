package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(
    navHostController: NavHostController,
    navigationManager: NavigationManager,
) {
    // Function to handle back navigation logic
    fun handleBackNavigation() {
        if (navigationManager.navController.previousBackStackEntry != null) {
            navigationManager.navController.popBackStack() // Pop one screen if there is a back stack
        } else {
            navigationManager.navController.navigate(Destinations.ListScreen) {
                popUpTo(Destinations.ListScreen) { inclusive = true }
            }
        }
    }

    BackHandler() {
        handleBackNavigation()
    }
    Scaffold(
        topBar = {
            TopBar(title = "Statistics",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation() },
                delete = {})
        },
    ) {

    }
}