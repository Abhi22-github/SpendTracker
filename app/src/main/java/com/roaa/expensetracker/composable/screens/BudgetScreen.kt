package com.roaa.expensetracker.composable.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.color1
import com.roaa.expensetracker.composable.components.BudgetTopBar
import com.roaa.expensetracker.composable.components.CircularProgress
import com.roaa.expensetracker.composable.components.ConfirmationAlertDialog
import com.roaa.expensetracker.composable.components.EmptyScreen
import com.roaa.expensetracker.composable.components.SpendsBudgetCard
import com.roaa.expensetracker.composable.greenColor
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.handleBackNavigation
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.statisticsComponent.BarChartBudgetUsage
import com.roaa.expensetracker.composable.statisticsComponent.LineChartBudgetTotalUsage
import com.roaa.expensetracker.composable.utils.ActionTypes
import com.roaa.expensetracker.composable.utils.HarmonizedColorPalette
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.composable.utils.harmonize
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.database.relations.BudgetWithDayDetails
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.uiDataModels.BarChartExpenseModel
import com.roaa.expensetracker.utilities.CalenderDayState
import com.roaa.expensetracker.utilities.DayState
import com.roaa.expensetracker.utilities.UiState
import com.roaa.expensetracker.utilities.datesListForMonth
import com.roaa.expensetracker.utilities.dayNameList
import com.roaa.expensetracker.utilities.getDayDifference
import com.roaa.expensetracker.utilities.getDaysRemaining
import com.roaa.expensetracker.utilities.getMonthsBetween
import com.roaa.expensetracker.utilities.toDayMonthFormat
import com.roaa.expensetracker.utilities.toLocalDate
import com.roaa.expensetracker.utilities.toLong
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetDayClass
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


val horizontalPadding = 16.dp
val verticalPadding = 8.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.budgetViewModel.uiState.collectAsState()
    val getCurrentBudgetFromRoom by viewModel.budgetViewModel.getCurrentBudget
        .collectAsState(
            BudgetWithDayDetails(
                emptyBudgetClass, listOf(emptyBudgetDayClass)
            )
        )
    LaunchedEffect(Unit) {
        viewModel.budgetViewModel.getCurrentBudgetWithDetailsForCompose()
    }
    val scope = rememberCoroutineScope()
    var getCurrentBudget by remember {
        mutableStateOf(
            BudgetWithDayDetails(
                emptyBudgetClass, listOf(emptyBudgetDayClass)
            )
        )
    }
    var isBudgetSet by remember { mutableStateOf(false) }
    var finishButtonClickStatus by remember { mutableStateOf(false) }
    LaunchedEffect(getCurrentBudgetFromRoom) {
        getCurrentBudgetFromRoom?.let {
            isBudgetSet = it?.budgetSummary?.isActive ?: false
        }
        getCurrentBudget = getCurrentBudgetFromRoom ?: BudgetWithDayDetails(
            emptyBudgetClass, listOf(emptyBudgetDayClass)
        )
    }
    val dayDifferenceForCharts = remember {
        mutableStateOf(
            getDayDifference(
                getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate(),
                LocalDate.now()
            )
        )
    }
    BackHandler {
        handleBackNavigation(navigationManager)
    }

    Scaffold(
        topBar = {
            BudgetTopBar(
                title = "Budget",
                showSetting = isBudgetSet,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                settingsClicked = {
                    navigationManager.navigateTo(
                        Destinations.BudgetSetupScreen(
                            ActionTypes.EDIT, getCurrentBudget.budgetSummary.budgetId
                        )
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgress()
                }

                is UiState.Success -> {
                    Column() {
                        if (isBudgetSet) {
                            var currentBudgetLocal by remember { mutableStateOf(BigDecimal.ZERO) }
                            var currentExpenseLocal by remember { mutableStateOf(BigDecimal.ZERO) }
                            var remainingBudget by remember { mutableStateOf(currentBudgetLocal - currentExpenseLocal) }
                            var remainingDaysPercentage by remember { mutableStateOf(BigDecimal.ZERO) }
                            val sortedBudgetAllDays = getCurrentBudget.budgetAllDays.sortedBy { it.date }
                            val barChartDataList = sortedBudgetAllDays.map {
                                BarChartExpenseModel(
                                    date = it.date,
                                    dayName = it.date.toLocalDate().toDayMonthFormat(),
                                    expenseAmount = it.totalExpense,
                                    incomeAmount = it.totalIncome
                                )
                            }
                            val cumulativeBudgetList =
                                sortedBudgetAllDays.runningFold(BigDecimal.ZERO) { sum, item -> sum + item.totalExpense }
                                    .drop(1)


                            val lineChartDataList =
                                sortedBudgetAllDays.mapIndexed { index, item ->
                                    item.date.toLocalDate().toDayMonthFormat() to
                                            cumulativeBudgetList[index]
                                }.toMap()


                            LaunchedEffect(getCurrentBudget) {

                                if (getCurrentBudget.budgetSummary.totalBudgetAmount == BigDecimal.ZERO) {
                                    currentBudgetLocal =
                                        getCurrentBudget.budgetSummary.totalBudgetAmount
                                } else {
                                    currentBudgetLocal =
                                        getCurrentBudget.budgetSummary.totalBudgetAmount
                                }
                                currentExpenseLocal =
                                    getCurrentBudget.budgetAllDays.fold(BigDecimal.ZERO) { acc, i ->
                                        acc + i.totalExpense
                                    }
                                remainingBudget = currentBudgetLocal - currentExpenseLocal
                            }

                            LaunchedEffect(getCurrentBudget) {
                                if (getCurrentBudget.budgetSummary.budgetTotalDays != 0L) {
                                    remainingDaysPercentage =
                                        (getDaysRemaining(getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()).toBigDecimal()
                                            .div(getCurrentBudget.budgetSummary.budgetTotalDays.toBigDecimal()))
                                } else {
                                    remainingDaysPercentage = BigDecimal.ONE
                                }
                            }

                            Column(Modifier.verticalScroll(rememberScrollState())) {

                                Text(
                                    text = "${LocalCurrency.current.currencySymbol} ${getCurrentBudget.budgetSummary.totalBudgetAmount}",
                                    modifier = modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Budget initial amount",
                                    modifier = modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(Modifier.height(24.dp))
                                Column(
                                    Modifier
                                        .padding(horizontal = 20.dp, vertical = 16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Text(
                                            text = getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate()
                                                .toDayMonthFormat(),
                                            modifier = modifier,
                                            textAlign = TextAlign.Center,
                                            style = typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                        DayProgressIndicator(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 12.dp),
                                            totalDays = getCurrentBudget.budgetSummary.budgetTotalDays,
                                            totalAmount = currentBudgetLocal,
                                            expenseAmount = currentExpenseLocal,
                                            day = getDayDifference(
                                                getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate(),
                                                LocalDate.now()
                                            ),
                                            height = 36f,
                                            dayName = LocalDate.now().toDayMonthFormat(),
                                            isInBudget = (LocalDate.now()
                                                .toLong() <= getCurrentBudget.budgetSummary.budgetEndDate && LocalDate.now()
                                                .toLong() >= getCurrentBudget.budgetSummary.budgetStartDate)

                                        )
                                        Text(
                                            text = getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()
                                                .toDayMonthFormat(),
                                            modifier = modifier,
                                            textAlign = TextAlign.Center,
                                            style = typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                    val remainingBudgetDays = getDayDifference(
                                        getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate(),
                                        LocalDate.now()
                                    )
                                    Text(
                                        text = if (remainingBudget > BigDecimal.ZERO) {
                                            "You can spend ${LocalCurrency.current.currencySymbol}${
                                                (remainingBudget).div(
                                                    BigDecimal(if (remainingBudgetDays == 0L) 1L else remainingBudgetDays)
                                                )
                                            }/day for ${remainingBudgetDays} more days"
                                        } else {
                                            "This budget is over. You have exceeded your budget limit"
                                        },
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        textAlign = TextAlign.Center,
                                        style = typography.bodyMedium,
                                        color = if (remainingBudget > BigDecimal.ZERO) MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        ) else MaterialTheme.colorScheme.error
                                    )
                                }



                                Spacer(Modifier.height(12.dp))

                                Row(
                                    Modifier
                                        .height(150.dp)
                                        .padding(horizontalPadding, verticalPadding)
                                ) {
                                    SpendsBudgetCard(
                                        Modifier,
                                        if (currentBudgetLocal == BigDecimal.ZERO) BigDecimal.ONE else currentBudgetLocal,
                                        if (currentExpenseLocal == BigDecimal.ZERO) BigDecimal.ONE else currentExpenseLocal,
                                    )
                                }
                                Spacer(Modifier.height(12.dp))

                                Spacer(Modifier.height(24.dp))

                                Column(
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = "Total Budget Analysis",
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 0.dp
                                        ),
                                        style = typography.titleMedium
                                    )
                                    Text(
                                        text = "total cumulative expense by day for budget period",
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp, vertical = 0.dp
                                        ),
                                        style = typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )

                                }
                                LineChartBudgetTotalUsage(
                                    Modifier,
                                    toPalette(color1), lineChartDataList,
                                    getCurrentBudget.budgetSummary.totalBudgetAmount,
                                    dayDifferenceForCharts
                                )
                                Spacer(Modifier.height(42.dp))

                                Column(
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = "Expense Per Day Analysis",
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 0.dp
                                        ),
                                        style = typography.titleMedium
                                    )
                                    Text(
                                        text = "total cumulative expense by day for budget period",
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp, vertical = 0.dp
                                        ),
                                        style = typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )

                                }
                                BarChartBudgetUsage(
                                    Modifier,
                                    toPalette(orange),
                                    barChartDataList,
                                    getCurrentBudget.budgetSummary.budgetAmountPerDay,
                                    dayDifferenceForCharts
                                )
                                Spacer(Modifier.height(24.dp))
                                SpendCalender(Modifier, getCurrentBudget)

                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(Modifier.padding(horizontalPadding, vertical = 16.dp)) {
                                        FilledTonalButton(
                                            onClick = {
                                                finishButtonClickStatus = !finishButtonClickStatus
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.filledTonalButtonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Icon(
                                                Icons.Rounded.Close,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onError
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                "Finish Early",
                                                color = MaterialTheme.colorScheme.onError
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            ConstraintLayout() {
                                val (emptyBudget, addBudgetButton) = createRefs()
                                Box(Modifier.constrainAs(emptyBudget) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }) {
                                    EmptyScreen(text = "No Budget Found")
                                }
                                FilledTonalButton(onClick = {
                                    navigationManager.navigateTo(
                                        Destinations.BudgetSetupScreen(
                                            ActionTypes.ADD, getCurrentBudget.budgetSummary.budgetId
                                        )
                                    )
                                }, Modifier.constrainAs(addBudgetButton) {
                                    top.linkTo(parent.top, 300.dp)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }) {
                                    Text("Add Budget", Modifier.align(Alignment.CenterVertically))
                                }
                            }
                        }
                        Spacer(Modifier.height(36.dp))

                        if (finishButtonClickStatus) {
                            ConfirmationAlertDialog(
                                onDismissRequest = {
                                    finishButtonClickStatus = !finishButtonClickStatus
                                },
                                onConfirmation = {
                                    scope.launch {
                                        isBudgetSet = false
                                        viewModel.budgetViewModel.updateBudget(
                                            getCurrentBudget.budgetSummary.copy(
                                                isActive = false
                                            )
                                        )
                                        finishButtonClickStatus = !finishButtonClickStatus
                                    }
                                },
                                dialogTitle = "Finish budget",
                                dialogText = "Your current budget will be completed and further expense will not be added to this budget",
                                icon = ImageVector.vectorResource(R.drawable.round_info_24),
                                confirmText = "Finish budget",
                                dismissText = "Cancel"
                            )
                        }
                    }
                }

                is UiState.Error -> {

                }

            }
        }

    }
}

@Composable
fun IndeterminateCircularIndicator(
    modifier: Modifier,
    progress: State<BigDecimal>,
    harmonizedColor: HarmonizedColorPalette
) {
    CircularProgressIndicator(
        progress = { progress.value.toFloat() },
        modifier = modifier,
        color = harmonizedColor.main,
        trackColor = harmonizedColor.container.copy(alpha = 0.3f),
        strokeWidth = 18.dp,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        gapSize = 0.dp,
    )
}

@Composable
fun SingleInfoBox(modifier: Modifier, label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.4f
            )
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp, 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, style = typography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(0.38f)
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = value, style = typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,

                )
        }
    }

}

@Composable
fun SpendCalender(
    modifier: Modifier = Modifier,
    getCurrentBudget: BudgetWithDayDetails,
) {
    val color =
        toPalette(
            harmonize(
                combineColors(
                    listOf(orange, MaterialTheme.colorScheme.surface),
                    0.2f
                )
            )
        )
    Card(
        Modifier
            .padding(horizontalPadding, verticalPadding)
            .wrapContentHeight()
            .fillMaxWidth(),
        colors = CardColors(
            containerColor = color.container.copy(alpha = 0.15f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(Modifier.padding(horizontalPadding, verticalPadding)) {
            Text(
                text = "This table shows how much you spent every day relative to your daily budge",
                style = typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            val getMonthsList = getMonthsBetween(
                getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate(),
                getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()
            )
            getMonthsList.forEachIndexed { index, it ->
                val list = datesListForMonth(
                    it.atDay(1),
                    getCurrentBudget.budgetSummary.budgetStartDate,
                    getCurrentBudget.budgetSummary.budgetEndDate,
                )

                SpendCalenderMonth(
                    modifier,
                    color,
                    getCurrentBudget,
                    it.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    list
                )
            }
        }
    }
}

@Composable
fun DayName(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun SpendCalenderMonth(
    modifier: Modifier = Modifier,
    color: HarmonizedColorPalette,
    getCurrentBudget: BudgetWithDayDetails,
    month: String,
    list: List<CalenderDayState>
) {

    Column() {
        Spacer(Modifier.height(10.dp))
        Text(
            text = month,
            style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            dayNameList.forEachIndexed { index, dayName ->
                DayName(Modifier.weight(1f), dayName)
            }
        }

        val budgetDayMap = getCurrentBudget.budgetAllDays.associateBy { it.date }


        list.forEachIndexed { index, it ->
            if (budgetDayMap.containsKey(it.dayDate)) {
                if (budgetDayMap[it.dayDate]!!.totalExpense == BigDecimal.ZERO) {
                    it.dayState = DayState.NOT_STARTED
                } else if (budgetDayMap[it.dayDate]!!.totalExpense > budgetDayMap[it.dayDate]!!.budgetAmount) {
                    it.dayState = DayState.OVER_LIMIT
                } else if (budgetDayMap[it.dayDate]!!.totalExpense < budgetDayMap[it.dayDate]!!.budgetAmount) {
                    it.dayState = DayState.IN_LIMIT
                } else {
                    it.dayState
                }
            }
        }

        val chunkedList = list.chunked(7)

        chunkedList.forEachIndexed { index, weekList ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                val p = weekList.toMutableList()
                if (weekList.size != 7) {
                    for (i in 0 until (7 - weekList.size)) {
                        p.add(CalenderDayState("-1", 0L, false, DayState.NOT_STARTED))
                    }
                }
                p.forEachIndexed { index, day ->
                    DayBox(
                        Modifier
                            .weight(1f)
                            .padding(2.dp, 0.dp), day.day, day.isInBudget, day.dayState
                    )
                }
            }
        }
    }

}

@Composable
fun DayBox(modifier: Modifier = Modifier, text: String, inBudget: Boolean, dayState: DayState) {
    val greenColorPalette = toPalette(greenColor)
    val orangeColorPalette = toPalette(orange)

    val (icon, tint) = when (dayState) {
        DayState.IN_LIMIT -> Pair(Icons.Rounded.Check, greenColorPalette.main)
        DayState.NOT_STARTED -> Pair(Icons.Rounded.Check, greenColorPalette.main)
        DayState.OVER_LIMIT -> Pair(Icons.Rounded.Close, orangeColorPalette.main)
        DayState.OUT_OF_BUDGET -> Pair(Icons.Rounded.Check, orangeColorPalette.main)
    }
    val background =
        if (inBudget) {
            when (dayState) {
                DayState.IN_LIMIT -> greenColorPalette.main.copy(alpha = 0.1f)
                DayState.NOT_STARTED -> MaterialTheme.colorScheme.surface
                DayState.OVER_LIMIT -> orangeColorPalette.main.copy(alpha = 0.1f)
                DayState.OUT_OF_BUDGET -> Color.Transparent
            }
        } else Color.Transparent

    val textColor =
        if (inBudget) {
            when (dayState) {
                DayState.IN_LIMIT -> greenColorPalette.main
                DayState.NOT_STARTED -> MaterialTheme.colorScheme.onSurface
                DayState.OVER_LIMIT -> orangeColorPalette.main
                DayState.OUT_OF_BUDGET -> Color.Transparent
            }
        } else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    Box(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background), contentAlignment = Alignment.Center
    ) {
        if (inBudget && (dayState == DayState.IN_LIMIT || dayState == DayState.OVER_LIMIT))
            Icon(
                icon,
                modifier = Modifier
                    .size(32.dp)
                    .zIndex(1f),
                contentDescription = null,
                tint = tint.copy(alpha = 0.3f)
            )
        Text(
            text = if (text != "-1") text else "",
            style = typography.labelMedium,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 12.dp)
        )
    }
}

@Composable
fun DayProgressIndicator(
    modifier: Modifier = Modifier,
    height: Float,
    totalDays: Long,
    totalAmount: BigDecimal,
    expenseAmount: BigDecimal,
    day: Long, // Days to decorate
    dayName: String,
    isInBudget: Boolean
) {
    val lineColor = MaterialTheme.colorScheme.surfaceContainer
    val decorationColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    var progress =
        (expenseAmount).divide(
            if (totalAmount == BigDecimal.ZERO) BigDecimal.ONE else totalAmount,
            2,
            RoundingMode.HALF_UP
        )
    val completedColor =
        if (progress > BigDecimal.ONE) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
    // progress = if (progress > BigDecimal.ZERO) BigDecimal.ONE else progress
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(10.dp)
        ) {
            val canvasWidth = size.width
            val segmentWidthForDayDecoration = canvasWidth / totalDays
            val segmentWidthForProgress = canvasWidth

            // Draw the total progress line
            drawLine(
                color = lineColor,
                start = Offset(0f, size.height / 2),
                end = Offset(canvasWidth, size.height / 2),
                strokeWidth = height,
                cap = StrokeCap.Round
            )

            // Draw completed progress line
            drawLine(
                color = completedColor,
                start = Offset(0f, size.height / 2),
                end = Offset(
                    segmentWidthForProgress * if (progress.toFloat() > 1f) 1f else progress.toFloat(),
                    size.height / 2
                ),
                strokeWidth = height,
                cap = StrokeCap.Round
            )

            // Draw decorations for specific days

            if (isInBudget) {
                val xOffset = (segmentWidthForDayDecoration * (day / totalDays))
                drawRoundRect(
                    color = decorationColor,
                    topLeft = Offset(xOffset, 0f - height / 2),
                    size = Size(20f, 60f),// Rectangle size
                    cornerRadius = CornerRadius(15f, 15f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (day.toFloat() / totalDays * 280).dp - 10.dp, y = (-25).dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = if (!isInBudget) "$dayName(Budget not started)" else dayName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}


@Preview
@Composable
private fun SpendCalenderPreview() {
    //SpendCalender(Modifier)
}