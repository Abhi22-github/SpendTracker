package com.roaa.expensetracker.Composables.Screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.RestBudgetPill
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Model.emptyBudgetClass
import com.roaa.expensetracker.Model.emptyBudgetDayClass
import com.roaa.expensetracker.Utilities.currentMonth
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.AnimationViewModel
import com.roaa.expensetracker.ViewModels.BudgetViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun DayScreen(
    navigationManager: NavigationManager,
    showSingleDateTransactions: Boolean,
    date: Long,
    isBudgetSet: Boolean,
    totalExpenseAmountForDate: Float,
    budgetAmountPerDay: Float,
    oldPercent: Float,
    percent: Float,
    uiViewModel: UiViewModel,
    animationViewModel: AnimationViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    budgetViewModel: BudgetViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch { uiViewModel.selectedMonth.emit(currentMonth) }
    }
    Column {
        Row(modifier = Modifier.padding(12.dp, 16.dp)) {
            RestBudgetPill(totalExpenseAmountForDate, budgetAmountPerDay, oldPercent, percent)
        }
        TransactionsListCompose(navigationManager, Modifier, true, date, uiViewModel)
    }
}


@Composable
fun DayViewScreen(
    navController: NavController,
    navigationManager: NavigationManager,
    date: Long,
    uiViewModel: UiViewModel,
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    budgetViewModel: BudgetViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 500 / 2, pageCount = { 500 })

    val tabs = getPreviousAndNext500Days(date.toLocalDate())

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Track the current tab offset and width for the indicator
    var indicatorOffset by remember { mutableFloatStateOf(0f) }
    var indicatorWidth by remember { mutableFloatStateOf(0f) }

//    LaunchedEffect(pagerState.currentPage) {
//        lazyListState.scrollToItem(index = pagerState.settledPage)
//        lazyListState.scrollToItem(pagerState.currentPage)
//        val itemInfo =
//            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == pagerState.currentPage }
//
//        if (itemInfo != null) {
//            Log.d(
//                "****viewPortEndOffset", lazyListState.layoutInfo.viewportEndOffset.toString()
//            )
//            Log.d("****itemInfo.size", itemInfo.size.toString())
//            var centerOffset = ((itemInfo.size - lazyListState.layoutInfo.viewportEndOffset) / 2)
//            Log.d("****centerOffset", centerOffset.toString())
//            lazyListState.scrollToItem(pagerState.currentPage, centerOffset)
//            // indicatorWidth = itemInfo.size.toFloat() / 2
//            // itemInfoSize = itemInfo.size
//            Log.d("****off", itemInfo.offset.toString())
//        }
//    }

    val visibleItemInfo by remember { derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo } }
    var isSelected by remember { mutableStateOf(false) }

//    LaunchedEffect(pagerState.currentPage) {
//        val itemInfo =
//            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == pagerState.currentPage }
//        if(itemInfo != null){
//            var centerOffset = ((itemInfo.size - lazyListState.layoutInfo.viewportEndOffset) / 2)
//            lazyListState.scrollToItem(pagerState.currentPage, centerOffset)
//        }else{
//            lazyListState.scrollToItem(index = pagerState.settledPage)
//            lazyListState.scrollToItem(pagerState.currentPage)
//        }
//    }

    var currentIndex by remember { mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState.currentPage) {
        currentIndex = pagerState.currentPage
        visibleItemInfo.forEach {
            Log.d("DayComposeScreen", "VisiblieItem --- ${it.index}")
        }
        val itemInfo =
            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentIndex }
        val isPresent = visibleItemInfo.drop(1).dropLast(1).any{
            currentIndex == it.index
        }
        if (isPresent) {
            Log.d("DayComposeScreen", "A")
        } else {
            Log.d("DayComposeScreen", "B")

            if (itemInfo != null) {
                Log.d("DayComposeScreen", "c")
                var centerOffset =
                    ((itemInfo.size * (currentIndex - lazyListState.layoutInfo.visibleItemsInfo.last().index)) / 2)

                Log.d("DayComposeScreen", "${lazyListState.layoutInfo.viewportEndOffset} $centerOffset")
                lazyListState.scrollToItem(currentIndex)
            }else{

            }
            lazyListState.scrollToItem(pagerState.currentPage)
        }
        Log.d("DayComposeScreen", "Total - $currentIndex -- ")


    }


    val scope = rememberCoroutineScope()
    Column {

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
                    TabItem(data = data, index = index, currentIndex = currentIndex, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                            currentIndex = index
                        }
                    })
                }
            }
        }


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(top = 0.dp)
        ) { page ->
            val currentDay = calculateCurrentPageDay(page, 250, date.toLocalDate())
            val getCurrentBudgetFromRoom by budgetViewModel.getCurrentBudgetWithDetails()
                .collectAsState(
                    BudgetWithDayDetails(
                        emptyBudgetClass, listOf(emptyBudgetDayClass)
                    )
                )
            var getCurrentBudget by remember {
                mutableStateOf(
                    BudgetWithDayDetails(
                        emptyBudgetClass, listOf(emptyBudgetDayClass)
                    )
                )
            }
            val getTotalAmountForDate by transactionViewModel.getTotalExpenseAmountForDateCompose(
                currentDay.toLong()
            ).collectAsState(0f)
            val getTotalAmountForDateExcludingLast by transactionViewModel.getTotalExpenseAmountForDateExcludingLastCompose(
                currentDay.toLong()
            ).collectAsState(0f)
            var isBudgetSet by remember { mutableStateOf(false) }
            LaunchedEffect(getCurrentBudgetFromRoom) {
                getCurrentBudgetFromRoom?.let {
                    isBudgetSet = it?.budgetSummary?.isActive ?: false
                }
                getCurrentBudget = getCurrentBudgetFromRoom ?: BudgetWithDayDetails(
                    emptyBudgetClass, listOf(emptyBudgetDayClass)
                )
            }
            val oldPercent = if (getCurrentBudget.budgetSummary.budgetAmountPerDay != 0f) {
                // Safe division: Handle division by zero and null values
                getTotalAmountForDateExcludingLast / getCurrentBudget.budgetSummary.budgetAmountPerDay
            } else {
                // Handle edge case (division by zero or null value)
                0f  // or use another default value, depending on your requirements
            }
            val percent = if (getCurrentBudget.budgetSummary.budgetAmountPerDay != 0f) {
                // Safe division: Handle division by zero and null values
                getTotalAmountForDate / getCurrentBudget.budgetSummary.budgetAmountPerDay
            } else {
                // Handle edge case (division by zero or null value)
                0f  // or use another default value, depending on your requirements
            }
            Column {
                DayScreen(
                    navigationManager,
                    false,
                    currentDay.toLong(),
                    isBudgetSet,
                    getTotalAmountForDate,
                    getCurrentBudget.budgetSummary.budgetAmountPerDay,
                    1 - oldPercent,
                    1 - percent,
                    uiViewModel
                )
            }
        }
    }
}


@Composable
fun TabItem(
    data: String,
    index: Int,
    currentIndex: Int,
    onClick: () -> Unit,
) {
    val tabRef = remember { mutableStateOf<LayoutCoordinates?>(null) }
    val isSelected = index == currentIndex

    val color by animateColorAsState(MaterialTheme.colorScheme.onPrimary)
    val colorGreen by animateColorAsState(Color.Green)
    val dataSplit = data.split(",")

    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .clickable { onClick() }
        .background(
            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            RoundedCornerShape(10.dp)
        )
        .padding(horizontal = 5.dp, vertical = 5.dp)
        .width(48.dp)

        .onGloballyPositioned { tabRef.value = it }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = dataSplit[0],
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 1f
                ),
                style = MaterialTheme.typography.bodySmall.copy(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(1.dp, 1.dp)
            )
            Spacer(Modifier.height(1.dp))
            Box(
                Modifier
                    .padding(0.dp, 0.dp)
            ) {
                Text(
                    text = dataSplit[1],
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 1f
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private fun calculateCurrentPageDay(
    page: Int,
    initialPage: Int,
    currentDate: LocalDate
): LocalDate {
    val monthsOffset = (page - initialPage).toLong()
    return currentDate.plusDays(monthsOffset)
}


