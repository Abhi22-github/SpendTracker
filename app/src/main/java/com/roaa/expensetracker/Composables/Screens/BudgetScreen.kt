package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.StatisticsComponent.BarChartBudgetUsage
import com.roaa.expensetracker.Composables.StatisticsComponent.LineChartBudgetTotalUsage
import com.roaa.expensetracker.Composables.color1
import com.roaa.expensetracker.Composables.components.BudgetTopBar
import com.roaa.expensetracker.Composables.components.EmptyScreen
import com.roaa.expensetracker.Composables.components.SpendsBudgetCard
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.ActionTypes
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.harmonize
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Hilt.AllViewModel
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel
import com.roaa.expensetracker.Utilities.CalenderDayState
import com.roaa.expensetracker.Utilities.DayState
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBudgetClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBudgetDayClass
import com.roaa.expensetracker.Utilities.datesListForMonth
import com.roaa.expensetracker.Utilities.dayNameList
import com.roaa.expensetracker.Utilities.getDayDifference
import com.roaa.expensetracker.Utilities.getDaysRemaining
import com.roaa.expensetracker.Utilities.getMonthsBetween
import com.roaa.expensetracker.Utilities.toDayMonthFormat
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
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
    var isBudgetSet by remember { mutableStateOf(false) }
    LaunchedEffect(getCurrentBudgetFromRoom) {
        getCurrentBudgetFromRoom?.let {
            isBudgetSet = it?.budgetSummary?.isActive ?: false
        }
        getCurrentBudget = getCurrentBudgetFromRoom ?: BudgetWithDayDetails(
            emptyBudgetClass, listOf(emptyBudgetDayClass)
        )
    }
    val modifierWithHorizontalPadding = Modifier.padding(horizontal = 16.dp)

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
        Column(modifier = Modifier.padding(it)) {
            if (isBudgetSet) {
                var currentBudgetLocal by remember { mutableStateOf(0f) }
                var currentExpenseLocal by remember { mutableStateOf(0f) }
                var remainingBudget by remember { mutableStateOf(currentBudgetLocal - currentExpenseLocal) }
                var remainingDaysPercentage by remember { mutableStateOf(0f) }

                val barChartDataList = getCurrentBudget.budgetAllDays.map {
                    BarChartExpenseModel(
                        date = it.date,
                        dayName = it.date.toLocalDate().toDayMonthFormat(),
                        expenseAmount = it.totalExpense,
                        incomeAmount = it.totalIncome
                    )
                }
                val cumulativeBudgetList =
                    getCurrentBudget.budgetAllDays.runningFold(0f) { sum, item -> sum + item.totalExpense }
                        .drop(1)


                val lineChartDataList = getCurrentBudget.budgetAllDays.mapIndexed { index, item ->
                    item.date.toLocalDate().toDayMonthFormat() to
                            cumulativeBudgetList[index]
                }.toMap()


                LaunchedEffect(getCurrentBudget) {

                    if (getCurrentBudget.budgetSummary.totalBudgetAmount == 0f) {
                        currentBudgetLocal = getCurrentBudget.budgetSummary.totalBudgetAmount
                    } else {
                        currentBudgetLocal = getCurrentBudget.budgetSummary.totalBudgetAmount
                    }
                    currentExpenseLocal = getCurrentBudget.budgetAllDays.fold(0f) { acc, i ->
                        acc + i.totalExpense
                    }
                    remainingBudget = currentBudgetLocal - currentExpenseLocal
                }

                LaunchedEffect(getCurrentBudget) {
                    if (getCurrentBudget.budgetSummary.budgetTotalDays != 0L) {
                        remainingDaysPercentage =
                            (getDaysRemaining(getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()).toFloat()
                                .div(getCurrentBudget.budgetSummary.budgetTotalDays.toFloat()))
                    } else {
                        remainingDaysPercentage = 1f
                    }
                }

                Column(Modifier.verticalScroll(rememberScrollState())) {

                    Text(
                        text = "₹ ${getCurrentBudget.budgetSummary.totalBudgetAmount}",
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
                            text = "You can spend ₹${(currentBudgetLocal - currentExpenseLocal) / remainingBudgetDays}/day for ${remainingBudgetDays} more days",
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }



                    Spacer(Modifier.height(12.dp))

//                    Box(
//                        Modifier
//                            .wrapContentHeight()
//                            .fillMaxWidth(), contentAlignment = Alignment.Center
//                    ) {
//                        val dayValue = ((getDayDifference(
//                            LocalDate.now(),
//                            getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()
//                        )).toFloat() / getCurrentBudget.budgetSummary.budgetTotalDays.toFloat())
//                        IndeterminateCircularIndicator(
//                            Modifier.size(180.dp),
//                            animateFloatAsState(
//                                dayValue,
//                                animationSpec = tween(
//                                    durationMillis = 2000,
//                                    easing = FastOutSlowInEasing
//                                )
//                            ),
//                            orangeColorPalette
//                        )
//                        Column {
//
//                            Text(
//                                text = "₹${if (remainingBudget.toInt() < 0) 0 else remainingBudget}",
//                                style = typography.headlineSmall.copy(fontFamily = CustomFonts.numberFont),
//                                textAlign = TextAlign.Center,
//                                color = purpleColorPalette.main,
//                                modifier = Modifier.width(130.dp)
//                            )
//                            Text(
//                                text = "${getDaysRemaining(getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate())} days",
//                                style = typography.titleLarge.copy(fontFamily = CustomFonts.numberFont),
//                                textAlign = TextAlign.Center,
//                                color = orangeColorPalette.main,
//                                modifier = Modifier.width(130.dp)
//                            )
//                        }
//                        IndeterminateCircularIndicator(
//                            Modifier.size(240.dp),
//                            animateFloatAsState(
//                                ((currentBudgetLocal - currentExpenseLocal) / currentBudgetLocal),
//                                animationSpec = tween(
//                                    durationMillis = 2000,
//                                    easing = FastOutSlowInEasing
//                                )
//                            ),
//                            purpleColorPalette
//                        )
//
//                    }

                    Row(
                        Modifier
                            .height(150.dp)
                            .padding(horizontalPadding, verticalPadding)
                    ) {
                        SpendsBudgetCard(
                            Modifier,
                            if (currentBudgetLocal == 0f) 1f else currentBudgetLocal,
                            if (currentExpenseLocal == 0f) 1f else currentExpenseLocal,
                        )
                    }
                    Spacer(Modifier.height(12.dp))
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Row {
//                            Icon(
//                                Icons.Filled.MonetizationOn,
//                                contentDescription = null,
//                                tint = purpleColorPalette.main
//                            )
//                            Spacer(Modifier.width(8.dp))
//                            Text(
//                                text = "Budget Remaining",
//                                style = typography.bodyMedium,
//                                textAlign = TextAlign.Start,
//                                color = purpleColorPalette.main,
//                                modifier = Modifier
//                            )
//                        }
//                        Spacer(Modifier.height(4.dp))
//                        Row {
//                            Icon(
//                                Icons.Filled.Timelapse,
//                                contentDescription = null,
//                                tint = orangeColorPalette.main
//                            )
//                            Spacer(Modifier.width(8.dp))
//                            Text(
//                                text = "Days Remaining",
//                                style = typography.bodyMedium,
//                                textAlign = TextAlign.Start,
//                                color = orangeColorPalette.main,
//                                modifier = Modifier
//                            )
//                        }
//                    }

                    Spacer(Modifier.height(24.dp))

//                    Row(
//                        Modifier.padding(16.dp, 0.dp)
//                    ) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .weight(1f)
//                                .background(
//                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
//                                    shape = RoundedCornerShape(20.dp)
//                                )
//                                .padding(16.dp),
//                        ) {
//                            Spacer(Modifier.width(12.dp))
//                            Column {
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Amount Per day",
//                                    labelValue = "₹ ${getCurrentBudget.budgetSummary.budgetAmountPerDay}",
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.AttachMoney,
//                                )
//
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.surfaceContainer,
//                                    thickness = 1.dp
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Budget Remaining",
//                                    labelValue = "₹ ${if (remainingBudget.toInt() < 0) 0 else remainingBudget}",
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.AttachMoney,
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.surfaceContainer,
//                                    thickness = 1.dp
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Total Budget Days",
//                                    labelValue = "${getCurrentBudget.budgetSummary.budgetTotalDays} Days",
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.Timelapse,
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(Modifier.height(24.dp))
//                    Row(
//                        Modifier.padding(16.dp, 0.dp)
//                    ) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .weight(1f)
//                                .background(
//                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
//                                    shape = RoundedCornerShape(20.dp)
//                                )
//                                .padding(16.dp),
//                        ) {
//                            Spacer(Modifier.width(12.dp))
//                            Column {
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Budget Start Date",
//                                    labelValue = getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate()
//                                        .toDisplayDate(),
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.Start,
//                                )
//
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.surfaceContainer,
//                                    thickness = 1.dp
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Budget End Date",
//                                    labelValue = getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()
//                                        .toDisplayDate(),
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.Stop,
//                                )
//
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.surfaceContainer,
//                                    thickness = 1.dp
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Distribution Method",
//                                    labelValue = "${DistributionMethod.fromNumber(getCurrentBudget.budgetSummary.restDistributionType)}",
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.TurnRight,
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.surfaceContainer,
//                                    thickness = 1.dp
//                                )
//                                Spacer(Modifier.height(spaceHeightInDetail))
//                                ValueLabelList(
//                                    modifier = Modifier,
//                                    labelAndValueStyle = typography.bodyMedium,
//                                    labelName = "Usage Notification",
//                                    labelValue = "below ${getCurrentBudget.budgetSummary.notificationForBudgetUsage}%",
//                                    iconNumber = 12,
//                                    image = Icons.Rounded.Notifications,
//                                )
//
//                            }
//                        }
//                    }
                    //out of box
//                    Box(
//                        Modifier
//                            .wrapContentHeight()
//                            .fillMaxWidth(), contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp)
//                        ) {
//
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//
//                            ) {
//                                SingleInfoBox(
//                                    Modifier.weight(1f),
//                                    "Budget Amount",
//                                    "₹${getCurrentBudget.budgetSummary.totalBudgetAmount}"
//                                )
//                                Spacer(Modifier.width(12.dp))
//                                SingleInfoBox(
//                                    Modifier.weight(1f),
//                                    "BudgetMonth",
//                                    "${getCurrentBudget.budgetSummary.totalBudgetAmount}"
//                                )
//                            }
//                            Spacer(Modifier.height(12.dp))
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//                            ) {
//                                SingleInfoBox(
//                                    Modifier.weight(1f),
//                                    "Start-End Date",
//                                    "${
//                                        getCurrentBudget.budgetSummary.budgetStartDate.toLocalDate()
//                                            .toDisplayStringForMonth()
//                                    } - ${
//                                        getCurrentBudget.budgetSummary.budgetEndDate.toLocalDate()
//                                            .toDisplayStringForMonth()
//                                    }"
//                                )
//                                Spacer(Modifier.width(12.dp))
//                                SingleInfoBox(
//                                    Modifier.weight(1f),
//                                    "Total Amount",
//                                    "₹${getCurrentBudget.budgetSummary.budgetAmountPerDay}/day"
//                                )
//                            }
//                        }
//
//                    }
                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Total Budget Analysis",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "total cumulative expense by day for budget period",
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 0.dp
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )

                    }
                    LineChartBudgetTotalUsage(
                        Modifier,
                        toPalette(color1), lineChartDataList,
                        getCurrentBudget.budgetSummary.totalBudgetAmount
                    )
                    Spacer(Modifier.height(42.dp))

                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Expense Per Day Analysis",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "total cumulative expense by day for budget period",
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 0.dp
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )

                    }
                    BarChartBudgetUsage(
                        Modifier,
                        toPalette(orange),
                        barChartDataList,
                        getCurrentBudget.budgetSummary.budgetAmountPerDay
                    )
                    Spacer(Modifier.height(24.dp))
//                    Row(
//                        Modifier
//                            .height(150.dp)
//                            .padding(horizontalPadding, verticalPadding)
//                    ) {
//                        SpendsBudgetCard(
//                            Modifier,
//                            currentBudgetLocal,
//                            currentExpenseLocal
//                        )
//                    }
                    SpendCalender(Modifier, getCurrentBudget)
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
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(
    modifier: Modifier,
    progress: State<Float>,
    harmonizedColor: HarmonizedColorPalette
) {
    CircularProgressIndicator(
        progress = { progress.value },
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
                if (budgetDayMap[it.dayDate]!!.totalExpense == 0f) {
                    it.dayState = DayState.NOT_STARTED
                } else if (budgetDayMap[it.dayDate]!!.totalExpense > budgetDayMap[it.dayDate]!!.budgetAmount) {
                    it.dayState = DayState.OVER_LIMIT
                } else if (budgetDayMap[it.dayDate]!!.totalExpense < budgetDayMap[it.dayDate]!!.budgetAmount) {
                    it.dayState = DayState.IN_LIMIT
                } else {
                    it.dayState
                }
            } else {

            }
        }

        val chunkedList = list.chunked(7)

        chunkedList.forEachIndexed { index, weekList ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                var p = weekList.toMutableList()
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
            text = if (text != "-1") text.toString() else "",
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
    totalAmount: Float,
    expenseAmount: Float,
    day: Long, // Days to decorate
    dayName: String,
    isInBudget: Boolean
) {
    val lineColor = MaterialTheme.colorScheme.surfaceContainer
    val completedColor = MaterialTheme.colorScheme.primaryContainer
    val decorationColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    val progress =
        if (expenseAmount == 0f) 1f else expenseAmount / if (totalAmount == 0f) 1f else totalAmount
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
                end = Offset(segmentWidthForProgress * progress, size.height / 2),
                strokeWidth = height,
                cap = StrokeCap.Round
            )

            // Draw decorations for specific days

            if (isInBudget) {
                val xOffset = (segmentWidthForDayDecoration * day)
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