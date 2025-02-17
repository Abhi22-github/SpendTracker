package com.roaa.expensetracker.Activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalDensity
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
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.Navigation.AppNavGraph
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.RootNavGraph
import com.roaa.expensetracker.Composables.Navigation.navigateToWithSingleTop
import com.roaa.expensetracker.Composables.Screens.MonthChip
import com.roaa.expensetracker.Composables.syncTheme
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.currentDay
import com.roaa.expensetracker.Utilities.currentMonth
import com.roaa.expensetracker.Utilities.currentYear
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Months
import com.roaa.expensetracker.Utilities.lockScreenOrientation
import com.roaa.expensetracker.Utilities.section1Items
import com.roaa.expensetracker.Utilities.section2Items
import com.roaa.expensetracker.ViewModels.UiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

val LocalWindowSize = compositionLocalOf { WindowWidthSizeClass.Compact }
val LocalWindowInsets = compositionLocalOf { PaddingValues(0.dp) }

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    private val isDone: MutableState<Boolean> = mutableStateOf(false)
    private val isReady: MutableState<Boolean> = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition { !isDone.value }
        setContent {
            val localContext = LocalContext.current
            val rootNavController = rememberNavController()
            val navigationManager = remember { NavigationManager(rootNavController) }
            LaunchedEffect(Unit) {
                syncTheme(localContext)
                // App ready for work
                isReady.value = true
            }
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            val deviceDensity = LocalDensity.current

            if (widthSizeClass == WindowWidthSizeClass.Compact) {
                lockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }

            val windowInsets = WindowInsets
                .systemBars
                .asPaddingValues()


            if (isReady.value) {
                ExpenseTrackerTheme {
                    CompositionLocalProvider(
                        LocalWindowSize provides widthSizeClass,
                        LocalWindowInsets provides windowInsets,
                    ) {
                        NavigationDrawer(rootNavController, navigationManager, Modifier)
                        LaunchedEffect(Unit) {
                            // App rendered and splash screen can be hidden
                            isDone.value = true
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    modifier: Modifier,
    uiViewModel: UiViewModel = hiltViewModel()
) {

    val section1 = section1Items
    val section2 = section2Items
    val deviceDensity = LocalDensity.current

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


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showMonthFilterChips by uiViewModel.showMonthFilterChips.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CompositionLocalProvider(
            ) {
                ModalDrawerSheet(Modifier.width(320.dp))
                {
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
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
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
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                        )
                    }

                }
            }
        },

        gesturesEnabled = true
    ) {
        val currentYear = currentYear
        val lazyMonthListState = rememberLazyListState()
        val monthList = getPreviousAndNext500Months(LocalDate.now())
        val selectedMonth by uiViewModel.selectedMonth.collectAsState()
        LaunchedEffect(true) {
            lazyMonthListState.scrollToItem(250)
        }
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        CompositionLocalProvider() {
            if (!showAppBar) {
                AppNavGraph(rootNavController, navigationManager, uiViewModel)
            } else {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { //TopBar to show title
                        Column {
                            TopAppBar(
                                // colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                                title = {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                uiViewModel.showMonthFilterChips.emit(!showMonthFilterChips)
                                            }
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                                    ) {
//                                        Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = selectedMonth,
                                            style = MaterialTheme.typography.titleMedium
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
                                            .clip(RoundedCornerShape(5.dp))
                                            .clickable {
                                                scope.launch {
                                                    lazyMonthListState.animateScrollToItem(250)
                                                    uiViewModel.selectedMonth.emit(currentMonth)
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
                                showMonthFilterChips,
                            ) {
                                LazyRow(
                                    state = lazyMonthListState,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    items(monthList) {
                                        MonthChip(
                                            it,
                                            currentYear,
                                            selectedMonth,
                                            {
                                                scope.launch {
                                                    uiViewModel.selectedMonth.emit(it)
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    },
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        RootNavGraph(rootNavController, navigationManager, uiViewModel)
                    }

                }
            }
        }
    }
}

