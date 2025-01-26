package com.roaa.expensetracker.Composables.Navigation

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.roaa.expensetracker.Composables.Screens.AddCategory
import com.roaa.expensetracker.Composables.Screens.CategoryScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavigationGraph() {
    SharedTransitionLayout {
        val context = LocalContext.current
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = ScreenA
        ) {
            composable<ScreenA> {
                CategoryScreen(
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
            composable<ScreenB> {
                val args = it.toRoute<ScreenB>()
                AddCategory(
                    navController = navController,
                    args.categoryId,
                    args.categoryName,
                    args.categoryIconNumber,
                    args.categoryType,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }

}

private fun sendUserBack(context: Context) {
    val activity = context as? ComponentActivity
    activity?.onBackPressedDispatcher?.onBackPressed()
}

fun NavController.popBackStackOrFinish(context: Context) {
    if (!popBackStack()) {
        (context as Activity).finish()
    } else {
        popBackStack()
    }
}

