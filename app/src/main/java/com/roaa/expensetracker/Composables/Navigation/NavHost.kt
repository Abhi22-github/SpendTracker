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
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.roaa.expensetracker.Composables.Screens.AddCategory
import com.roaa.expensetracker.Composables.Screens.BudgetScreen
import com.roaa.expensetracker.Composables.Screens.CategoryScreen
import com.roaa.expensetracker.Composables.Screens.DayViewScreen
import com.roaa.expensetracker.Composables.Screens.MonthViewScreen
import com.roaa.expensetracker.Composables.Screens.PaymentDetailsScreen
import com.roaa.expensetracker.Composables.Screens.PaymentMethodScreen
import com.roaa.expensetracker.Composables.Screens.SettingsScreen
import com.roaa.expensetracker.Composables.Screens.StatisticsScreen
import com.roaa.expensetracker.Composables.Screens.TransactionDetailsScreen
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.UiViewModel
import java.time.LocalDate


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootNavGraph(
    rooNavController: NavHostController,
    navigationManager: NavigationManager,
    uiViewModel: UiViewModel,
) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = rooNavController,
            startDestination = NavRoutes.HomeScreen,
        ) {

            navigation<NavRoutes.HomeScreen>(startDestination = Destinations.ListScreen) {
                composable<Destinations.ListScreen> {
                    TransactionsListCompose(
                        navigationManager,
                        modifier = Modifier,
                        showSingleDateTransactions = false,
                        date = LocalDate.now().toLong(),
                        uiViewModel,
                    )
                }
                composable<Destinations.MonthScreen> {
                    MonthViewScreen(
                        rooNavController,
                        navigationManager,
                        modifier = Modifier,
                        uiViewModel
                    )
                }
                composable<Destinations.DayScreen> {
                    val args = it.toRoute<Destinations.DayScreen>()
                    DayViewScreen(rooNavController, navigationManager, args.date, uiViewModel)
                }
            }

            navigation<NavRoutes.AppScreen>(startDestination = Destinations.BudgetScreen) {

                composable<Destinations.CategoryScreen>() {
                    CategoryScreen(
                        rooNavController,
                        navigationManager,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
                composable<Destinations.CategoryDetailsScreen> {
                    val args = it.toRoute<Destinations.CategoryDetailsScreen>()
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


                composable<Destinations.BudgetScreen>() {
                    BudgetScreen(
                        rooNavController,
                        navigationManager,
                        modifier = Modifier
                    )
                }

                composable<Destinations.BankAccountScreen>() {
                    PaymentMethodScreen(
                        rooNavController,
                        navigationManager,
                        Modifier,
                        {}, uiViewModel
                    )
                }
                composable<Destinations.SettingScreen>() {
                    SettingsScreen(rooNavController, navigationManager, {})
                }

                composable<Destinations.StatisticsScreen>() {
                    StatisticsScreen(rooNavController, navigationManager)
                }
            }
            composable<Destinations.DetailsScreen> {
                val args = it.toRoute<Destinations.DetailsScreen>()
                TransactionDetailsScreen(
                    modifier = Modifier,
                    rooNavController,
                    navigationManager,
                    args.amount,
                    args.categoryName,
                    uiViewModel
                )
            }
            composable<Destinations.BankDetailsScreen> {
                val args = it.toRoute<Destinations.BankDetailsScreen>()
                PaymentDetailsScreen(
                    rootNavController = rooNavController,
                    navigationManager = navigationManager,
                    modifier = Modifier,
                    bankAccountId = args.bankAccountId,
                    uiViewModel = uiViewModel
                )
            }
        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(
    rooNavController: NavHostController,
    navigationManager: NavigationManager,
    uiViewModel: UiViewModel
) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = rooNavController,
            startDestination = NavRoutes.HomeScreen,
        ) {

            navigation<NavRoutes.HomeScreen>(startDestination = Destinations.ListScreen) {
                composable<Destinations.ListScreen> {
                    TransactionsListCompose(
                        navigationManager,
                        modifier = Modifier,
                        showSingleDateTransactions = false,
                        date = LocalDate.now().toLong(),
                        uiViewModel
                    )
                }
                composable<Destinations.MonthScreen> {
                    MonthViewScreen(
                        rooNavController,
                        navigationManager,
                        modifier = Modifier,
                        uiViewModel
                    )
                }
                composable<Destinations.DayScreen> {
                    val args = it.toRoute<Destinations.DayScreen>()
                    DayViewScreen(rooNavController, navigationManager, args.date, uiViewModel)
                }
            }

            navigation<NavRoutes.AppScreen>(startDestination = Destinations.BudgetScreen) {

                composable<Destinations.CategoryScreen>() {
                    CategoryScreen(
                        rooNavController,
                        navigationManager,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
                composable<Destinations.CategoryDetailsScreen> {
                    val args = it.toRoute<Destinations.CategoryDetailsScreen>()
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


                composable<Destinations.BudgetScreen>() {
                    BudgetScreen(
                        rooNavController,
                        navigationManager,
                        modifier = Modifier
                    )
                }

                composable<Destinations.BankAccountScreen>() {
                    PaymentMethodScreen(
                        rooNavController,
                        navigationManager,
                        Modifier,
                        {}, uiViewModel
                    )
                }
                composable<Destinations.SettingScreen>() {
                    SettingsScreen(rooNavController, navigationManager, {})
                }
                composable<Destinations.StatisticsScreen>() {
                    StatisticsScreen(rooNavController, navigationManager)
                }
            }
            composable<Destinations.DetailsScreen> {
                val args = it.toRoute<Destinations.DetailsScreen>()
                TransactionDetailsScreen(
                    modifier = Modifier,
                    rooNavController,
                    navigationManager,
                    args.amount,
                    args.categoryName,
                    uiViewModel
                )
            }
            composable<Destinations.BankDetailsScreen> {
                val args = it.toRoute<Destinations.BankDetailsScreen>()
                PaymentDetailsScreen(
                    rootNavController = rooNavController,
                    navigationManager = navigationManager,
                    modifier = Modifier,
                    args.bankAccountId,
                    uiViewModel = uiViewModel
                )
            }
        }

    }
}

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

fun NavController.navigateToWithSingleTop(screen: Destinations) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(Destinations.ListScreen, inclusive = false) // Pop up to MainScreen
        .setLaunchSingleTop(true)
        .build()

    navigate(screen, navOptions)
}

// Function to handle back navigation logic
fun handleBackNavigation(navigationManager: NavigationManager) {
    if (navigationManager.navController.previousBackStackEntry != null) {
        navigationManager.navController.popBackStack() // Pop one screen if there is a back stack
    } else {
        navigationManager.navController.navigate(Destinations.ListScreen) {
            popUpTo(Destinations.ListScreen) { inclusive = true }
        }
    }
}



