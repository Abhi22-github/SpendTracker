package com.roaa.expensetracker.composable.navigation

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
import com.roaa.expensetracker.composable.components.TransactionsListCompose
import com.roaa.expensetracker.composable.onBoarding.OnboardingScreen
import com.roaa.expensetracker.composable.screens.AddCategory
import com.roaa.expensetracker.composable.screens.BudgetScreen
import com.roaa.expensetracker.composable.screens.BudgetSetupScreen
import com.roaa.expensetracker.composable.screens.CategoryScreen
import com.roaa.expensetracker.composable.screens.DayViewScreen
import com.roaa.expensetracker.composable.screens.MonthViewScreen
import com.roaa.expensetracker.composable.screens.PaymentDetailsScreen
import com.roaa.expensetracker.composable.screens.PaymentMethodScreen
import com.roaa.expensetracker.composable.screens.SettingsScreen
import com.roaa.expensetracker.composable.screens.StatisticsScreenTest
import com.roaa.expensetracker.composable.screens.TransactionDetailsScreen
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.utilities.toLong
import java.time.LocalDate


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootNavGraph(
    rooNavController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    startDestination: Destinations
) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = rooNavController,
            startDestination = NavRoutes.HomeScreen,
        ) {

            navigation<NavRoutes.HomeScreen>(startDestination = startDestination) {
                composable<Destinations.ListScreen> {
                    TransactionsListCompose(
                        navigationManager,
                        viewModel,
                        modifier = Modifier,
                        showSingleDateTransactions = false,
                        date = LocalDate.now().toLong(),
                    )
                }
                composable<Destinations.MonthScreen> {
                    MonthViewScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        modifier = Modifier,
                    )
                }
                composable<Destinations.DayScreen> {
                    val args = it.toRoute<Destinations.DayScreen>()
                    DayViewScreen(rooNavController, navigationManager, viewModel, args.date)
                }

                composable<Destinations.WelcomeScreen> {
                    OnboardingScreen(rooNavController, navigationManager, viewModel)
                }
            }

            navigation<NavRoutes.AppScreen>(startDestination = Destinations.BudgetScreen) {

                composable<Destinations.CategoryScreen>() {
                    CategoryScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
                composable<Destinations.CategoryDetailsScreen> {
                    val args = it.toRoute<Destinations.CategoryDetailsScreen>()
                    AddCategory(
                        rooNavController,
                        navigationManager,
                        viewModel,
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
                        viewModel,
                        modifier = Modifier
                    )
                }

                composable<Destinations.BankAccountScreen>() {
                    PaymentMethodScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        Modifier,
                        {}
                    )
                }
                composable<Destinations.SettingScreen>() {
                    SettingsScreen(rooNavController, navigationManager, viewModel, {})
                }

                composable<Destinations.StatisticsScreen>() {
                    StatisticsScreenTest(rooNavController, navigationManager, viewModel)
                }
            }
            composable<Destinations.DetailsScreen> {
                val args = it.toRoute<Destinations.DetailsScreen>()
                TransactionDetailsScreen(
                    modifier = Modifier,
                    rooNavController,
                    navigationManager,
                    viewModel,
                )
            }
            composable<Destinations.BankDetailsScreen> {
                val args = it.toRoute<Destinations.BankDetailsScreen>()
                PaymentDetailsScreen(
                    rootNavController = rooNavController,
                    navigationManager = navigationManager,
                    viewModel = viewModel,
                    modifier = Modifier,
                    bankAccountId = args.bankAccountId,
                )
            }
            composable<Destinations.BudgetSetupScreen> {
                val args = it.toRoute<Destinations.BudgetSetupScreen>()
                BudgetSetupScreen(
                    rooNavController,
                    navigationManager,
                    viewModel,
                    args.type,
                    args.budgetId
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
    viewModel: AllViewModel,
    startDestination: Destinations
) {
    SharedTransitionLayout {
        val context = LocalContext.current
        NavHost(
            navController = rooNavController,
            startDestination = NavRoutes.HomeScreen,
        ) {

            navigation<NavRoutes.HomeScreen>(startDestination = startDestination) {
                composable<Destinations.ListScreen> {
                    TransactionsListCompose(
                        navigationManager,
                        viewModel,
                        modifier = Modifier,
                        showSingleDateTransactions = false,
                        date = LocalDate.now().toLong(),
                    )
                }
                composable<Destinations.MonthScreen> {
                    MonthViewScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        modifier = Modifier,
                    )
                }
                composable<Destinations.DayScreen> {
                    val args = it.toRoute<Destinations.DayScreen>()
                    DayViewScreen(rooNavController, navigationManager, viewModel, args.date)
                }
                composable<Destinations.WelcomeScreen> {
                    OnboardingScreen(rooNavController, navigationManager, viewModel)
                }
            }

            navigation<NavRoutes.AppScreen>(startDestination = Destinations.BudgetScreen) {

                composable<Destinations.CategoryScreen>() {
                    CategoryScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
                composable<Destinations.CategoryDetailsScreen> {
                    val args = it.toRoute<Destinations.CategoryDetailsScreen>()
                    AddCategory(
                        rooNavController,
                        navigationManager,
                        viewModel,
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
                        viewModel,
                        modifier = Modifier
                    )
                }

                composable<Destinations.BankAccountScreen>() {
                    PaymentMethodScreen(
                        rooNavController,
                        navigationManager,
                        viewModel,
                        Modifier,
                        {}
                    )
                }
                composable<Destinations.SettingScreen>() {
                    SettingsScreen(rooNavController, navigationManager, viewModel, {})
                }
                composable<Destinations.StatisticsScreen>() {
                    StatisticsScreenTest(rooNavController, navigationManager, viewModel)
                }
            }
            composable<Destinations.DetailsScreen> {
                val args = it.toRoute<Destinations.DetailsScreen>()
                TransactionDetailsScreen(
                    modifier = Modifier,
                    rooNavController,
                    navigationManager,
                    viewModel,
                )
            }
            composable<Destinations.BankDetailsScreen> {
                val args = it.toRoute<Destinations.BankDetailsScreen>()
                PaymentDetailsScreen(
                    rootNavController = rooNavController,
                    navigationManager = navigationManager,
                    viewModel = viewModel,
                    modifier = Modifier,
                    args.bankAccountId,
                )
            }
            composable<Destinations.BudgetSetupScreen>() {
                val args = it.toRoute<Destinations.BudgetSetupScreen>()
                BudgetSetupScreen(
                    rooNavController,
                    navigationManager,
                    viewModel,
                    args.type,
                    args.budgetId
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



