package com.roaa.expensetracker.Composables.Navigation

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
import com.roaa.expensetracker.Activity.NavigationDrawer
import com.roaa.expensetracker.Composables.Screens.AddCategory
import com.roaa.expensetracker.Composables.Screens.BudgetScreen
import com.roaa.expensetracker.Composables.Screens.CategoryScreen
import com.roaa.expensetracker.Composables.Screens.DayViewScreen
import com.roaa.expensetracker.Composables.Screens.MonthViewScreen
import com.roaa.expensetracker.Composables.Screens.PaymentMethodScreen
import com.roaa.expensetracker.Composables.Screens.SettingsScreen
import com.roaa.expensetracker.Composables.Screens.TransactionDetailsScreen
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import com.roaa.expensetracker.Utilities.toLong
import java.time.LocalDate


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootNavGraph(
    rooNavController: NavHostController,
    navController: NavHostController,
    navigationManager: NavigationManager
) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = rooNavController,
            startDestination = RootScreen.MainScreen,
        ) {
            // Main Navigation (Contains the Drawer & HomeNavGraph)
            composable<RootScreen.MainScreen> {
                NavigationDrawer(rooNavController, navController, navigationManager, Modifier)
            }
            composable<RootScreen.CategoryScreen>() {
                CategoryScreen(
                    rooNavController,
                    navigationManager,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
            composable<RootScreen.CategoryDetailsScreen> {
                val args = it.toRoute<RootScreen.CategoryDetailsScreen>()
                AddCategory(
                    rooNavController,
                    navigationManager,
                    args.categoryId,
                    args.categoryName,
                    args.categoryIconNumber,
                    args.categoryType,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }


            composable<RootScreen.BudgetScreen>() {
                BudgetScreen(
                    rooNavController,
                    navigationManager,
                    modifier = Modifier
                )
            }

            composable<RootScreen.DetailsScreen> {
                TransactionDetailsScreen(
                    rooNavController,
                    navigationManager,
                    modifier = Modifier
                )
            }

            composable<RootScreen.BankAccountScreen>() {
                PaymentMethodScreen(
                    rooNavController,
                    navigationManager,
                    Modifier,
                    {})
            }
            composable<RootScreen.SettingScreen>() {
                SettingsScreen(rooNavController, navigationManager, {})
            }
        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SectionNavigation(navController: NavHostController, navigationManager: NavigationManager) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = navController, startDestination = SectionScreenNavRoutes.ListScreen
        ) {

            composable<SectionScreenNavRoutes.ListScreen> {
                TransactionsListCompose(
                    navigationManager,
                    modifier = Modifier,
                    showSingleDateTransactions = false,
                    date = LocalDate.now().toLong(),
                )
            }
            composable<SectionScreenNavRoutes.MonthScreen> {
                MonthViewScreen(navController, navigationManager, modifier = Modifier)
            }

            composable<SectionScreenNavRoutes.DayScreen> {
                val args = it.toRoute<SectionScreenNavRoutes.DayScreen>()
                DayViewScreen(navController, navigationManager, args.date)
            }
        }
    }
}

//@OptIn(ExperimentalSharedTransitionApi::class)
//@Composable
//fun FullNavigation(
//    navRootController: NavHostController,
//    navController: NavHostController,
//    navigationManager: NavigationManager
//) {
//    SharedTransitionLayout {
//        val context = LocalContext.current
//        BackHandler {
//            // Navigate to the HomeNavGraph default screen instead of exiting
//            navRootController.navigate(RootScreen.MainScreen) {
//                popUpTo(0) // Clear back stack
//            }
//        }
//        NavHost(
//            navController = navController, startDestination = FullScreenNavRoutes.BudgetScreen
//        ) {
//            composable<FullScreenNavRoutes.CategoryScreen> {
//                CategoryScreen(
//                    navigationManager,
//                    sharedTransitionScope = this@SharedTransitionLayout,
//                    animatedVisibilityScope = this
//                )
//            }
//            composable<FullScreenNavRoutes.CategoryDetailsScreen> {
//                val args = it.toRoute<FullScreenNavRoutes.CategoryDetailsScreen>()
//                AddCategory(
//                    navigationManager,
//                    args.categoryId,
//                    args.categoryName,
//                    args.categoryIconNumber,
//                    args.categoryType,
//                    sharedTransitionScope = this@SharedTransitionLayout,
//                    animatedVisibilityScope = this
//                )
//            }
//
//
//            composable<FullScreenNavRoutes.BudgetScreen> {
//                BudgetScreen(navigationManager, modifier = Modifier)
//            }
//
//            composable<FullScreenNavRoutes.DetailsScreen> {
//                TransactionDetailsScreen(navigationManager, modifier = Modifier)
//            }
//
//            composable<FullScreenNavRoutes.BankAccountScreen> {
//                PaymentMethodScreen(navigationManager, Modifier, {})
//            }
//            composable<FullScreenNavRoutes.SettingScreen> {
//                SettingsScreen(navigationManager, {})
//            }
//
//
//        }
//    }
//
//}

val enterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(durationMillis = 300)
    )
}
val exitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(durationMillis = 300)
    )
}
val popEnterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(durationMillis = 300)
    )
}
val popExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(durationMillis = 300)
    )
}

fun NavController.onBackPressed() {
    navigateUp()
}

//fun NavController.navigateTo(screen: SectionScreenNavRoutes) {
//    val navOptions = NavOptions.Builder()
//        .setPopUpTo(RootScreen.MainScreen, inclusive = false) // Pop up to MainScreen
//        .build()
//
//    navigate(screen, navOptions)
//}



