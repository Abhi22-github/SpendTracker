package com.roaa.expensetracker.Composables.Navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roaa.expensetracker.Composables.Screens.AddCategory
import com.roaa.expensetracker.Composables.Screens.CategoryScreen


@Composable
fun SetupNavigationGraph() {
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenA
    ) {
        composable<ScreenA> {
            CategoryScreen(navController = navController, { sendUserBack(context) })
        }
        composable<ScreenB> {
            AddCategory(navController = navController)
        }
    }

}
private fun sendUserBack(context: Context) {
    val activity = context as? ComponentActivity
    activity?.onBackPressedDispatcher?.onBackPressed()
}

