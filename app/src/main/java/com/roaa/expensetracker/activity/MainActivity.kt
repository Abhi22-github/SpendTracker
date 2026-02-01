package com.roaa.expensetracker.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.ExpenseTrackerTheme
import com.roaa.expensetracker.composable.navigation.AppNavGraph
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.RootNavGraph
import com.roaa.expensetracker.composable.navigation.navigateToWithSingleTop
import com.roaa.expensetracker.composable.screens.MonthChip
import com.roaa.expensetracker.composable.syncTheme
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.notification.NotificationPermissionHandler
import com.roaa.expensetracker.notification.createNotificationChannel
import com.roaa.expensetracker.notification.notificationChannelList
import com.roaa.expensetracker.notification.workManager.scheduleDailyNotification
import com.roaa.expensetracker.utilities.appStartingChecks
import com.roaa.expensetracker.utilities.convertToWholeMonthName
import com.roaa.expensetracker.utilities.currentDay
import com.roaa.expensetracker.utilities.currentMonth
import com.roaa.expensetracker.utilities.currentYear
import com.roaa.expensetracker.utilities.getPreviousAndNext500Months
import com.roaa.expensetracker.utilities.lockScreenOrientation
import com.roaa.expensetracker.utilities.section1Items
import com.roaa.expensetracker.utilities.section2Items
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCurrency
import com.roaa.expensetracker.viewModels.UiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

val LocalWindowSize = compositionLocalOf { WindowWidthSizeClass.Compact }
val LocalWindowInsets = compositionLocalOf { PaddingValues(0.dp) }
val LocalErrorMessage = compositionLocalOf { mutableStateOf<String>("") }
val LocalCurrency = compositionLocalOf { defaultCurrency }

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    private val isDone: MutableState<Boolean> = mutableStateOf(false)
    private val isReady: MutableState<Boolean> = mutableStateOf(false)
    private val notificationUiViewModel: UiViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.getBooleanExtra("SHOW_ADD_TRANSACTION", false) == true) {
            notificationUiViewModel.isNotificationClicked = true // Update state
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition { !isDone.value }

        setContent {
            val scope = rememberCoroutineScope()
            val localContext = LocalContext.current
            val rootNavController = rememberNavController()
            val navigationManager = remember { NavigationManager(rootNavController) }
            val allViewModels: AllViewModel = AllViewModel(
                transactionsViewModel = hiltViewModel(),
                categoryViewModel = hiltViewModel(),
                preferencesViewModel = hiltViewModel(),
                uiViewModel = hiltViewModel(),
                budgetViewModel = hiltViewModel(),
                budgetDayViewModel = hiltViewModel(),
                bankAccountsViewModel = hiltViewModel(),
                animationViewModel = hiltViewModel()
            )
            val context = LocalContext.current
            val onboardingCompletedStatus by allViewModels.preferencesViewModel.isUserOnboarded.collectAsState(
                false
            )
            val startDestination =
                if (onboardingCompletedStatus) Destinations.ListScreen else Destinations.WelcomeScreen

            NotificationPermissionHandler(onPermissionGranted = {}, onPermissionDenied = {})

            scheduleDailyNotification(this)
            allViewModels.preferencesViewModel.setFirstStartupCompleted()


            LaunchedEffect(Unit) {
                syncTheme(localContext)
                //create notification channel
                notificationChannelList.forEach {
                    createNotificationChannel(context, it)
                }
                appStartingChecks(context, allViewModels)
                // App ready for work
                isReady.value = true
            }
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            if (widthSizeClass == WindowWidthSizeClass.Compact) {
                lockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }

            val windowInsets = WindowInsets.systemBars.asPaddingValues()

            val errorMessage = remember { mutableStateOf<String>("") }
            val localCurrencyClass by allViewModels.preferencesViewModel.getCurrency.collectAsState(
                defaultCurrency
            )

            if (isReady.value) {
                ExpenseTrackerTheme {
                    CompositionLocalProvider(
                        LocalWindowSize provides widthSizeClass,
                        LocalWindowInsets provides windowInsets,
                        LocalErrorMessage provides errorMessage,
                        LocalCurrency provides localCurrencyClass
                    ) {
                        NavigationDrawer(
                            rootNavController, navigationManager, allViewModels, startDestination
                        )
                        LaunchedEffect(Unit) {
                            // App rendered and splash screen can be hidden
                            isDone.value = true
                            if (notificationUiViewModel.isNotificationClicked) {
                                scope.launch {
                                    allViewModels.uiViewModel.addCategorySpecificOrBankSpecificTransaction.emit(
                                        true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle any additional logic here if needed
        if (intent.getBooleanExtra("SHOW_ADD_TRANSACTION", false) == true) {
            notificationUiViewModel.isNotificationClicked = true // Update state
        }
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    allViewModels: AllViewModel,
    startDestination: Destinations
) {

    val section1 = section1Items
    val section2 = section2Items

    val currentBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route?.substringBefore("/")

    //Remember Clicked index state
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    selectedItemIndex = when (currentRoute) {
        Destinations.ListScreen.javaClass.canonicalName -> 0
        Destinations.MonthScreen.javaClass.canonicalName -> 1
        Destinations.DayScreen::class.java.canonicalName -> 2
        else -> 0
    }
    val showAppBar = if (currentRoute in listOf(
            Destinations.ListScreen.javaClass.canonicalName,
            Destinations.MonthScreen.javaClass.canonicalName,
            Destinations.DayScreen::class.java.canonicalName
        )
    ) true else false

    val showMonthFilter = if (currentRoute in listOf(
            Destinations.ListScreen.javaClass.canonicalName,
            Destinations.MonthScreen.javaClass.canonicalName,
        )
    ) true else false


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showMonthFilterChips by allViewModels.uiViewModel.showMonthFilterChips.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            CompositionLocalProvider(
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable { scope.launch { drawerState.close() } }) {
                    ModalDrawerSheet(
                        Modifier
                            .width(320.dp)
                            .clickable(enabled = drawerState.isOpen) { // Close on outside tap
                                scope.launch { drawerState.close() }
                            }) {
                        Spacer(modifier = Modifier.height(16.dp)) //space (margin) from top
                        Row(
                            modifier = Modifier.padding(20.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Image(
                                painter = painterResource(R.drawable.app_icon),
                                contentDescription = "App icon",
                                modifier = Modifier.size(24.dp),
                            )

                            Text(
                                text = "Expense Tracker",
                                modifier = Modifier.padding(12.dp, 0.dp),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                        section1.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = {
                                Text(
                                    text = item.title,
                                    modifier = Modifier.padding(12.dp, 0.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                                selected = index == selectedItemIndex,
                                onClick = {
                                    selectedItemIndex = index
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    rootNavController.navigateToWithSingleTop(item.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            ImageVector.vectorResource(item.selectedIcon)
                                        } else ImageVector.vectorResource(item.unselectedIcon),
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                badge = {  // Show Badge
                                    item.badgeCount?.let {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                            )
                        }

                        HorizontalDivider(
                            Modifier.padding(5.dp),
                            color = (MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 1f))
                        )

                        section2.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = {
                                Text(
                                    text = item.title,
                                    modifier = Modifier.padding(12.dp, 0.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    rootNavController.navigateToWithSingleTop(item.route)

                                },
                                icon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(item.unselectedIcon),
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                badge = {  // Show Badge
                                    item.badgeCount?.let {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                            )
                        }

                    }
                }
            }
        },

        gesturesEnabled = false
    ) {
        val currentYear = currentYear
        val lazyMonthListState = rememberLazyListState()
        val monthList = getPreviousAndNext500Months(LocalDate.now())
        val selectedMonth by allViewModels.uiViewModel.selectedMonth.collectAsState()
        LaunchedEffect(true) {
            lazyMonthListState.scrollToItem(250)
        }
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        CompositionLocalProvider() {
            if (!showAppBar) {
                AppNavGraph(rootNavController, navigationManager, allViewModels, startDestination)
            } else {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { //TopBar to show title
                        Column {
                            CenterAlignedTopAppBar(
                                // colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                                title = {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                allViewModels.uiViewModel.showMonthFilterChips.emit(
                                                    !showMonthFilterChips
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                                    ) {
//                                        Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = convertToWholeMonthName(selectedMonth),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }) {
                                        Icon(  //Show Menu Icon on TopBar
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(),

                                actions = {
                                    Card(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(25.dp))
                                            .clickable {
                                                scope.launch {
                                                    lazyMonthListState.animateScrollToItem(250)
                                                    allViewModels.uiViewModel.selectedMonth.emit(
                                                        currentMonth
                                                    )
                                                }
                                            },
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = currentDay,
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                },
                                scrollBehavior = scrollBehavior,
                            )
                            AnimatedVisibility(
                                showMonthFilterChips && showMonthFilter,
                            ) {
                                LazyRow(
                                    state = lazyMonthListState,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    items(monthList) {
                                        MonthChip(
                                            it, currentYear, selectedMonth, {
                                                scope.launch {
                                                    allViewModels.uiViewModel.selectedMonth.emit(it)
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    },
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        RootNavGraph(
                            rootNavController, navigationManager, allViewModels, startDestination
                        )
                    }

                }
            }
        }
    }
}


