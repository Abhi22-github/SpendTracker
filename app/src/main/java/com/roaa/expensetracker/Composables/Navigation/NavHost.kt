package com.roaa.expensetracker.Composables.Navigation

import android.app.Activity
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.roaa.expensetracker.Composables.Screens.AddCategory
import com.roaa.expensetracker.Composables.Screens.CategoryScreen
import com.roaa.expensetracker.Composables.Screens.DayViewScreen
import com.roaa.expensetracker.Composables.Screens.MonthViewScreen
import com.roaa.expensetracker.Composables.Screens.PaymentMethodScreen
import com.roaa.expensetracker.Composables.Screens.SettingsScreen
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import java.time.LocalDate


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavigationGraph(navController: NavHostController, navigationManager: NavigationManager) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = navController,
            startDestination = NavRoutes.MainScreen
        ) {
            composable<NavRoutes.ScreenA> {
                CategoryScreen(
                    navigationManager,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
            composable<NavRoutes.ScreenB> {
                val args = it.toRoute<NavRoutes.ScreenB>()
                AddCategory(
                    navigationManager,
                    args.categoryId,
                    args.categoryName,
                    args.categoryIconNumber,
                    args.categoryType,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }

            composable<NavRoutes.MainScreen> {
                TransactionsListCompose(
                    navigationManager,
                    modifier = Modifier,
                    showSingleDateTransactions = false,
                    date = LocalDate.now(),
                )
            }
            composable<NavRoutes.MonthScreen> {
                MonthViewScreen(navigationManager, modifier = Modifier)
            }

            composable<NavRoutes.DayScreen> {
                DayViewScreen(navigationManager)
            }

            composable<NavRoutes.BankAccountScreen> {
                PaymentMethodScreen(navigationManager, Modifier, {})
            }
            composable<NavRoutes.SettingScreen> {
                SettingsScreen(navigationManager, {})
            }


        }
    }

}

val enterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = 300)
    )
}
val exitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(durationMillis = 300)
    )
}
val popEnterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(durationMillis = 300)
    )
}
val popExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = 300)
    )
}

fun NavController.popBackStackOrFinish(context: Context) {
    if (!popBackStack()) {
        (context as Activity).finish()
    } else {
        popBackStack()
    }
}

