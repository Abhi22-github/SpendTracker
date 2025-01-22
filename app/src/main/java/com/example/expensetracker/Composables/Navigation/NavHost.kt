package com.example.expensetracker.Composables.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Composables.Screens.AddCategory
import com.example.expensetracker.Composables.Screens.CategoryScreen


@Composable
fun SetupNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenA
    ) {
        composable<ScreenA> {
            CategoryScreen(navController = navController, {})
        }
        composable<ScreenB> {
            AddCategory(navController = navController)
        }
    }

}

