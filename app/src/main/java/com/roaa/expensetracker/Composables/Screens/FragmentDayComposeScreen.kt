package com.roaa.expensetracker.Composables.Screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.RestBudgetPill
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import com.roaa.expensetracker.Utilities.convertLocalDateToLong
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.ViewModels.AnimationViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.absoluteValue

@Composable
fun FragmentDayScreen(
    navigationManager: NavigationManager,
    showSingleDateTransactions: Boolean,
    date: LocalDate,
    animationViewModel: AnimationViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {

    val longDate = convertLocalDateToLong(date)
    transactionsViewModel.getTotalExpenseForRange(
        longDate, longDate
    )
    val budget by preferencesViewModel.getBudgetValue.collectAsState(1f)
    val newDailyBudget by transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()

    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget
    } else {
        0f
    }
    animationViewModel.method("₹$amountInString", percent)

    Column {
        Row(modifier = Modifier.padding(12.dp, 16.dp)) {
            RestBudgetPill(date)
        }

        TransactionsListCompose(navigationManager, Modifier, true, date)
    }
}


@Composable
fun DayViewScreen(
    navigationManager: NavigationManager,
    date: Long,
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionsViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 500 / 2, pageCount = { 500 })
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val configuration = LocalConfiguration.current
    val tabs = getPreviousAndNext500Days(LocalDate.now())

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Track the current tab offset and width for the indicator
    var indicatorOffset by remember { mutableFloatStateOf(0f) }
    var indicatorWidth by remember { mutableFloatStateOf(0f) }
    var itemInfoSize by remember { mutableIntStateOf(0) }

    // Automatically scroll LazyRow when pager changes
    LaunchedEffect(pagerState.currentPage) {
        lazyListState.scrollToItem(index = pagerState.settledPage)
//        lazyListState.animateScrollToItem(
//            index = pagerState.currentPage,
//           // scrollOffset = calculateCenteredScrollOffset(lazyListState, pagerState.currentPage)
//        )
        lazyListState.scrollToItem(pagerState.currentPage)
        val itemInfo =
            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == pagerState.currentPage }

//        val itemInfoList = lazyListState.layoutInfo.visibleItemsInfo.map { it.key }
//        itemInfoList.forEach {
//            Log.d("******$$$$$", it.toString())
//        }
//        if (pagerState.currentPage in itemInfoList) {
//            Log.d("******1234", pagerState.currentPage.toString())
//        } else {
        if (itemInfo != null) {
            Log.d(
                "****viewPortEndOffset", lazyListState.layoutInfo.viewportEndOffset.toString()
            )
            Log.d("****itemInfo.size", itemInfo.size.toString())
            var centerOffset = ((itemInfo.size - lazyListState.layoutInfo.viewportEndOffset) / 2)
            Log.d("****centerOffset", centerOffset.toString())
            lazyListState.scrollToItem(pagerState.currentPage, centerOffset)
            // indicatorWidth = itemInfo.size.toFloat() / 2
            // itemInfoSize = itemInfo.size
            Log.d("****off", itemInfo.offset.toString())
        }
//            } else {
//                val d = lazyListState.layoutInfo.viewportEndOffset / 2
//                Log.d("****d", itemInfoSize.toString())
//
//                lazyListState.scrollToItem(pagerState.currentPage, -(d - itemInfoSize))
//            }
        // }
    }
    LaunchedEffect(pagerState.currentPageOffsetFraction) {
        // Log.d("****", pagerState.currentPageOffsetFraction.toString())
    }

    // Coroutine scope for launching pager state updates
    val scope = rememberCoroutineScope()
    Column {
//        ScrollableTabRow(
//            selectedTabIndex = pagerState.currentPage,
//            modifier = Modifier.fillMaxWidth(),
//            edgePadding = 0.dp,
//        indicator = { tabPositions ->
//            TabRowDefaults.in(
//                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
//            )
//        }
        //       ) {
//            LazyRow {
//                itemsIndexed(tabs) { index, item ->
//                    val currentDay = calculateCurrentPageDay(index, 250)
//                    Tab(
//                        selected = pagerState.currentPage == index,
//                        onClick = {
//                            scope.launch {
//                                pagerState.animateScrollToPage(index)
//                            }
//                        },
//                        text = { Text(text = currentDay.toString()) }
//                    )
//                }
//            }

//            tabs.forEachIndexed { index, title ->
//                val currentDay = calculateCurrentPageDay(500, 500)
//                Tab(
//                    selected = pagerState.currentPage == 500,
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(500)
//                        }
//                    },
//                    text = { Text(text = currentDay.toString()) }
//                )
//            }
        //       }
        Box(
            modifier = Modifier.fillMaxWidth()

        ) {
            LazyRow(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                flingBehavior = rememberSnapFlingBehavior(
                    lazyListState, snapPosition = SnapPosition.Center
                )
            ) {
                itemsIndexed(tabs) { index, data ->
                    val isSelected = index == pagerState.currentPage
                    TabItem(index = data, isSelected = isSelected, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, onTabMeasured = { offset, width ->
                        if (isSelected) {
                            indicatorOffset = offset
                            indicatorWidth = width
                        }
                    })
                }
            }

            // Sliding Indicator
//            Box(
//                modifier = Modifier
//                    .offset { IntOffset(indicatorOffset.roundToInt(), 0) }
//                    .width(indicatorWidth.dp)
//                    .height(2.dp)
//                    .background(Color.Blue)
//                    .align(Alignment.BottomStart)
//            )
        }

        HorizontalPager(
            state = pagerState,
        ) { page ->
            val currentDay = calculateCurrentPageDay(page, 250)
            FragmentDayScreen(navigationManager, false, currentDay)
        }
    }
}

// TabSliderWithPager()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyTabRow(
    selectedTabIndex: Int, tabs: List<Int>, pagerState: PagerState, onTabSelected: (Int) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll to the selected tab when it changes
    LaunchedEffect(selectedTabIndex) {
        lazyListState.scrollToItem(selectedTabIndex)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            flingBehavior = rememberSnapFlingBehavior(lazyListState)
        ) {
            itemsIndexed(tabs) { index, title ->
                Tab(modifier = Modifier.height(56.dp),
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(text = title.toString()) })
            }
        }

        // Custom indicator
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .offset {
//                    IntOffset(
//                        x = (selectedTabIndex * 1).dp.roundToPx(), // Adjust based on tab width
//                        y = 30
//                    )
//                }
//                .padding(30.dp)
//                .width(40.dp) // Adjust based on tab width
//                .height(60.dp)
//                .background(Color.Blue)
//        )
    }
}


@Composable
fun TabItem(
    index: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onTabMeasured: (offset: Float, width: Float) -> Unit
) {
    val tabRef = remember { mutableStateOf<LayoutCoordinates?>(null) }

    // Measure the tab's position and width
    LaunchedEffect(isSelected, tabRef.value) {
//        if (isSelected) {
//            tabRef.value?.let { coordinates ->
//                val offset = coordinates.localToWindow(Offset.Zero).x
//                val width = coordinates.size.width.toFloat()
//                onTabMeasured(offset, width)
//            }
//        }
    }
    val color by animateColorAsState(MaterialTheme.colorScheme.onPrimary)
    val colorGreen by animateColorAsState(Color.Green)
    val dataSplit = index.split(",")

    Box(modifier = Modifier
        .padding(horizontal = 4.dp)
        .width(48.dp)
        .clickable { onClick() }
        .onGloballyPositioned { tabRef.value = it }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = dataSplit[0],
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(1.dp, 1.dp)
            )
            Spacer(Modifier.height(2.dp))
            Box(
                Modifier
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .padding(2.dp, 2.dp)
                    .size(30.dp)

            ) {
                Text(
                    text = dataSplit[1],
                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.38f
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 2.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

// Calculate scroll offset to center the item
//private fun calculateCenteredScrollOffset(
//    state: LazyListState,
//    index: Int
//): Int {
//    val layoutInfo = state.layoutInfo
//    val visibleItems = layoutInfo.visibleItemsInfo
//    if (visibleItems.isEmpty()) return 0
//
//    val itemInfo = visibleItems.find { it.index == index }
//    return if (itemInfo != null) {
//        val centerOffset = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
//        (itemInfo.offset + itemInfo.size / 2) - centerOffset
//    } else {
//        0
//    }
//}

@Composable
fun TabItem(
    text: String, isSelected: Boolean, onClick: () -> Unit, textColor: Color, scale: Float
) {
    Box(modifier = Modifier
        .clickable { onClick() }
        .scale(scale)
        .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState, tabPositions: List<TabPosition>
): Modifier = composed {
    val currentPage = pagerState.currentPage
    val currentPageOffset = pagerState.currentPageOffsetFraction
    val indicatorOffset = if (currentPage >= 0 && currentPage < tabPositions.size) {
        val currentTab = tabPositions[currentPage]
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        if (nextTab != null) {
            val progress = currentPageOffset.absoluteValue
            currentTab.left + (nextTab.left - currentTab.left) * progress
        } else {
            currentTab.left
        }
    } else {
        0f
    }
    offset(x = 30.dp)
}

private fun calculateCurrentPageDay(page: Int, initialPage: Int): LocalDate {
    val initialDate = LocalDate.now()
    val monthsOffset = (page - initialPage).toLong()
    return initialDate.plusDays(monthsOffset)
}


