package com.roaa.expensetracker.composable.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.colorBad
import com.roaa.expensetracker.composable.colorEditor
import com.roaa.expensetracker.composable.colorGood
import com.roaa.expensetracker.composable.colorNotGood
import com.roaa.expensetracker.composable.components.HomeStatCardSingleNew
import com.roaa.expensetracker.composable.components.RestBudgetPill
import com.roaa.expensetracker.composable.components.TransactionsListCompose
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.composable.utils.harmonize
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.database.relations.BudgetWithDayDetails
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.currentMonth
import com.roaa.expensetracker.utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.utilities.parseAmount
import com.roaa.expensetracker.utilities.toDisplayStringForMonthWithYear
import com.roaa.expensetracker.utilities.toLocalDate
import com.roaa.expensetracker.utilities.toLong
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetDayClass
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun DayScreen(
    navigationManager: NavigationManager,
    showSingleDateTransactions: Boolean,
    date: Long,
    viewModel: AllViewModel,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch { viewModel.uiViewModel.selectedMonth.emit(currentMonth) }
    }
    Column {
        TransactionsListCompose(navigationManager,viewModel, Modifier, true, date )
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DayViewScreen(
    navController: NavController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    date: Long,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = 500 / 2, pageCount = { 500 })

    val tabs = getPreviousAndNext500Days(date.toLocalDate())

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val visibleItemInfo by remember { derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo } }
    var currentIndex by remember { mutableIntStateOf(pagerState.currentPage) }


    LaunchedEffect(pagerState.currentPage) {
        currentIndex = pagerState.currentPage

        val itemInfo =
            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentIndex }
        val isPresent = visibleItemInfo.drop(1).dropLast(1).any {
            currentIndex == it.index
        }
        if (isPresent) {
            Log.d("DayComposeScreen", "A")
        } else {
            Log.d("DayComposeScreen", "B")

            if (itemInfo != null) {
                Log.d("DayComposeScreen", "c")
                lazyListState.scrollToItem(pagerState.currentPage - 2)
            } else {
                lazyListState.scrollToItem(pagerState.currentPage)
            }

        }
        Log.d("DayComposeScreen", "Total - $currentIndex -- ")
    }

    LaunchedEffect(Unit) { lazyListState.scrollToItem(pagerState.currentPage - 4) }

    //vars
    var currentDay by remember { mutableStateOf(LocalDate.now()) }
    val getCurrentBudgetFromRoom by viewModel.budgetViewModel.getCurrentBudgetWithDetails()
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
    val getTotalExpenseAmountForDate by viewModel.transactionsViewModel.getTotalExpenseAmountForDateCompose(
        currentDay.toLong()
    ).collectAsState(BigDecimal.ZERO)
    val getTotalIncomeAmountForDate by viewModel.transactionsViewModel.getTotalIncomeAmountForDateCompose(
        currentDay.toLong()
    ).collectAsState(BigDecimal.ZERO)
    val getTotalAmountForDateExcludingLast by viewModel.transactionsViewModel.getTotalExpenseAmountForDateExcludingLastCompose(
        currentDay.toLong()
    ).collectAsState(BigDecimal.ZERO)
    var isBudgetSet by remember { mutableStateOf(false) }
    LaunchedEffect(getCurrentBudgetFromRoom) {
//        getCurrentBudgetFromRoom?.let {
//            isBudgetSet = it?.budgetSummary?.isActive ?: false
//        }
        getCurrentBudget = getCurrentBudgetFromRoom ?: BudgetWithDayDetails(
            emptyBudgetClass, listOf(emptyBudgetDayClass)
        )
    }
    val oldPercent = if (getCurrentBudget.budgetSummary.budgetAmountPerDay != BigDecimal.ZERO) {
        // Safe division: Handle division by zero and null values
        getTotalAmountForDateExcludingLast / getCurrentBudget.budgetSummary.budgetAmountPerDay
    } else {
        // Handle edge case (division by zero or null value)
        BigDecimal.ZERO  // or use another default value, depending on your requirements
    }
    val percent = if (getCurrentBudget.budgetSummary.budgetAmountPerDay != BigDecimal.ZERO) {
        // Safe division: Handle division by zero and null values
        getTotalExpenseAmountForDate / getCurrentBudget.budgetSummary.budgetAmountPerDay
    } else {
        // Handle edge case (division by zero or null value)
        BigDecimal.ZERO  // or use another default value, depending on your requirements
    }

    LaunchedEffect(pagerState.currentPage,getCurrentBudget) {

        currentDay = calculateCurrentPageDay(pagerState.currentPage, 250, date.toLocalDate())
        if (currentDay.toLong() <= getCurrentBudget.budgetSummary.budgetEndDate && currentDay.toLong() >= getCurrentBudget.budgetSummary.budgetStartDate
        ) {
            isBudgetSet = true
        } else {
            isBudgetSet = false
        }
    }

    Column {
        BoxWithConstraints(
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
                    TabItem(
                        data = data,
                        index = index,
                        currentIndex = currentIndex,
                        maxWidth / 9,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                                currentIndex = index
                            }
                        })
                }
            }
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            DayStatCard(
                Modifier,
                currentDay,
                getTotalExpenseAmountForDate,
                getTotalIncomeAmountForDate,
                isBudgetSet,
                BigDecimal.ONE.minus(oldPercent),
                BigDecimal.ONE.minus(percent),
                getCurrentBudget.budgetSummary.budgetAmountPerDay
            )
        }
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(top = 0.dp)
        ) { page ->
            val selectedDay = calculateCurrentPageDay(page, 250, date.toLocalDate())
            Column {
                DayScreen(
                    navigationManager,
                    false,
                    selectedDay.toLong(),
                    viewModel
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
    tabSize: Dp,
    onClick: () -> Unit,
) {
    val tabRef = remember { mutableStateOf<LayoutCoordinates?>(null) }
    val isSelected = index == currentIndex

    val dataSplit = data.split(",")
    Box(Modifier.padding(horizontal = 5.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .background(
//                if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primaryContainer.copy(
//                    alpha = 0.4f
//                ),
                Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .width(tabSize)
            .onGloballyPositioned { tabRef.value = it }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                Spacer(Modifier.height(5.dp))
                Text(
                    text = dataSplit[0],
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    ),
                    style = typography.bodySmall.copy(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding()
                )
                Spacer(Modifier.height(3.dp))
                Box(
                    Modifier
                        .padding(0.dp, 0.dp)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.3f
                            ),
                            RoundedCornerShape(50)
                        )
                        .aspectRatio(1f)
                ) {
                    Text(
                        text = dataSplit[1],
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary.copy(
                            alpha = 1f
                        ),
                        style = typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                            .align(Alignment.Center)
                    )
                }
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DayStatCard(
    modifier: Modifier = Modifier,
    currentDay: LocalDate,
    expense: BigDecimal,
    income: BigDecimal,
    isBudgetSet: Boolean,
    oldPercent: BigDecimal,
    percent: BigDecimal,
    budgetAmountPerDay: BigDecimal,
) {

    val percentWithNewSpentAnimated = animateFloatAsState(
        label = "percentWithNewSpentAnimated",
        targetValue = percent.toFloat(),
        animationSpec = TweenSpec(300),
    ).value

    val harmonizedColor = toPalette(
        harmonize(
            combineColors(
                listOf(
                    colorBad,
                    colorNotGood,
                    colorGood,
                ),
                percentWithNewSpentAnimated.coerceIn(0f, 1f),
            ),
            colorEditor
        )
    )

    val cardColor = combineColors(
        MaterialTheme.colorScheme.surface,
        harmonizedColor.container,
        angle = 0.4f,
    )
    var mainContentVisibility by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(horizontal = 12.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        ConstraintLayout(
            Modifier
        ) {
            val (content, decoration1, decoration2) = createRefs()
            Column(Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.animateContentSize()
                .zIndex(1f)) {
                Box(Modifier.clickable { mainContentVisibility = !mainContentVisibility }) {
                    Row(
                        Modifier
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(24.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    harmonizedColor.container.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(50)
                                )
                                .weight(0.1f)
                                .aspectRatio(1f)
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.GraphicEq,
                                modifier = Modifier.size(18.dp),
                                contentDescription = "Pie Icon"
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(0.8f)) {
                            Text(
                                text = currentDay.toDisplayStringForMonthWithYear(),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                style = typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { mainContentVisibility = !mainContentVisibility },
                            modifier = Modifier
                                .weight(0.1f)
                                .aspectRatio(1f)
                        ) {
                            Icon(
                                Icons.Rounded.KeyboardArrowDown,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Drop Down"
                            )
                        }
                        Spacer(Modifier.width(24.dp))
                    }
                }
                if (false)
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                    )
                BoxWithConstraints {
                    val width by animateDpAsState(if (isBudgetSet) maxWidth / 3 else maxWidth / 2)
                    Row(
                        Modifier
                    ) {
                        HomeStatCardSingleNew(
                            Modifier
                                .width(width)
                                .wrapContentHeight(), parseAmount(expense), EXPENSE
                        )
                        HomeStatCardSingleNew(
                            Modifier
                                .width(width)
                                .wrapContentHeight(), parseAmount(income), INCOME
                        )
                        AnimatedVisibility(isBudgetSet) {
                            HomeStatCardSingleNew(
                                Modifier
                                    .width(width)
                                    .wrapContentHeight(),
                                parseAmount(budgetAmountPerDay), "Budget"
                            )
                        }
                    }
                }

                AnimatedVisibility(isBudgetSet) {
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                    )
                    Row(modifier = Modifier.padding(12.dp, 16.dp)) {
                        RestBudgetPill(
                            expense, budgetAmountPerDay, oldPercent, percent
                        )
                    }
                }
            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(128.dp)
                    .constrainAs(decoration1) {
                        top.linkTo(parent.top, margin = -30.dp)
                        end.linkTo(parent.end, margin = -30.dp)
                    }, colorFilter = ColorFilter.tint(harmonizedColor.container)
            )
        }
    }
}




