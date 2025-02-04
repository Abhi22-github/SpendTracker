package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.BudgetBottomSheet
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.harmonize
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Model.emptyBudgetClass
import com.roaa.expensetracker.Model.emptyTotalExpenseIncomeClass
import com.roaa.expensetracker.Utilities.CalenderDayState
import com.roaa.expensetracker.Utilities.DayState
import com.roaa.expensetracker.Utilities.datesListForMonth
import com.roaa.expensetracker.Utilities.dayNameList
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.BudgetViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import java.time.LocalDate


val horizontalPadding = 16.dp
val verticalPadding = 8.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val getCurrentBudget by budgetViewModel.getCurrentBudget().collectAsState(emptyBudgetClass)
//    val getTotalAmountForRange by transactionsViewModel.getTotalExpenseAmountForRangeFlow.collectAsState()
//    var currentBudgetLocal by remember { mutableStateOf(1f) }
//    var currentExpenseLocal by remember { mutableStateOf(1f) }
//    var remainingDaysPercentage by remember { mutableStateOf(1f) }
//    val scrollableState = rememberScrollState()
//
//    LaunchedEffect(getCurrentBudget, getTotalAmountForRange) {
//        transactionsViewModel.getTotalExpenseForRange(
//            getCurrentBudget.budgetMonthStartDate,
//            getCurrentBudget.budgetMonthEndDate
//        )
//        if (getCurrentBudget.budgetAmountForMonth == 0f || getTotalAmountForRange.totalAmount == 0f) {
//
//        } else {
//            currentBudgetLocal = getCurrentBudget.budgetAmountForMonth
//            currentExpenseLocal = getTotalAmountForRange.totalAmount
//        }
//    }
//
//    LaunchedEffect(getCurrentBudget) {
//        if (getCurrentBudget.budgetTotalDays != 0L) {
//            remainingDaysPercentage =
//                (getDaysRemaining(getCurrentBudget.budgetMonthEndDate.toLocalDate()).toFloat()
//                    .div(getCurrentBudget.budgetTotalDays.toFloat()))
//        } else {
//            remainingDaysPercentage = 1f
//        }
//    }
//
//    val normalColor = toPalette(blueColor)

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showBottomSheet = !showBottomSheet
                },
                icon = { Icon(Icons.Filled.Add, "Localized description") },
                text = { Text(text = "Add") },
            )
        },
    ) {
//        Column(Modifier.verticalScroll(rememberScrollState())) {
//
//            Spacer(Modifier.height(32.dp))
//            Box(
//                Modifier
//                    .wrapContentHeight()
//                    .fillMaxWidth(), contentAlignment = Alignment.Center
//            ) {
//                IndeterminateCircularIndicator(
//                    Modifier.size(160.dp),
//                    remainingDaysPercentage,
//                    normalColor
//                )
//                Text(
//                    text = "${getDaysRemaining(getCurrentBudget.budgetMonthEndDate.toLocalDate())} Days Left",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.width(130.dp)
//                )
//                // IndeterminateCircularIndicator(Modifier.size(200.dp), f2, harmonizedColor)
//
//            }
//            Spacer(Modifier.height(12.dp))
//            Box(
//                Modifier
//                    .wrapContentHeight()
//                    .fillMaxWidth(), contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(12.dp)
//                ) {
//
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//
//                    ) {
//                        SingleInfoBox(
//                            Modifier.weight(1f),
//                            "Budget Amount",
//                            "₹${getCurrentBudget.budgetAmountForMonth}"
//                        )
//                        Spacer(Modifier.width(12.dp))
//                        SingleInfoBox(
//                            Modifier.weight(1f),
//                            "BudgetMonth",
//                            "${getCurrentBudget.currentMonthName}"
//                        )
//                    }
//                    Spacer(Modifier.height(12.dp))
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                    ) {
//                        SingleInfoBox(
//                            Modifier.weight(1f),
//                            "Start-End Date",
//                            "${
//                                getCurrentBudget.budgetMonthStartDate.toLocalDate()
//                                    .toDisplayStringForMonth()
//                            } - ${
//                                getCurrentBudget.budgetMonthEndDate.toLocalDate()
//                                    .toDisplayStringForMonth()
//                            }"
//                        )
//                        Spacer(Modifier.width(12.dp))
//                        SingleInfoBox(
//                            Modifier.weight(1f),
//                            "Total Amount",
//                            "₹${getCurrentBudget.budgetAmountPerDay}/day"
//                        )
//                    }
//                }
//
//            }
//            Spacer(Modifier.height(24.dp))
//            Row(
//                Modifier
//                    .height(200.dp)
//                    .padding(horizontalPadding, verticalPadding)
//            ) {
//                SpendsBudgetCard(
//                    Modifier,
//                    currentBudgetLocal,
//                    currentExpenseLocal
//                )
//            }
//            SpendCalender(Modifier)
//        }
        Spacer(Modifier.height(36.dp))
    }
    AnimatedVisibility(showBottomSheet) {

        BudgetBottomSheet(
            bottomSheetState,
            bottomSheetDismissed = { showBottomSheet = !showBottomSheet },
            TextFieldValue("000"),
            false
        )
    }
}

@Composable
fun IndeterminateCircularIndicator(
    modifier: Modifier,
    progress: Float,
    harmonizedColor: HarmonizedColorPalette
) {

    CircularProgressIndicator(
        progress = { progress },
        modifier = modifier,
        color = harmonizedColor.main,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeWidth = 10.dp,
        gapSize = -2.dp,
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
        shape = RoundedCornerShape(50)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp, 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(0.38f)
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = value, style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Composable
fun SpendCalender(
    modifier: Modifier = Modifier,
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    val currentBudget by budgetViewModel.getCurrentBudget().collectAsState(emptyBudgetClass)
    val firstDayOfMonth = LocalDate.now().withDayOfMonth(1)
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())
    val totalAmountList by transactionsViewModel.getListOfTotalAmountPerDayForRangeForCompose(
        firstDayOfMonth.toLong(),
        lastDayOfMonth.toLong()
    ).collectAsState(
        listOf(emptyTotalExpenseIncomeClass)
    )
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
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "February",
                style = MaterialTheme.typography.titleMedium,
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

            val chunkedList = datesListForMonth(
                LocalDate.now(),
                currentBudget.budgetMonthStartDate,
                currentBudget.budgetMonthEndDate,
                currentBudget.budgetAmountPerDay,
                totalAmountList,
            ).chunked(7)
            chunkedList.forEachIndexed { index, weekList ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    var p = weekList.toMutableList()
                    if (weekList.size != 7) {
                        for (i in 0 until (7 - weekList.size)) {
                            p.add(CalenderDayState(-1, false, DayState.NOT_STARTED))
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
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(7),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.height(250.dp)
//            ) {
//                items(datesListForMonth(LocalDate.now())) {
//                    DayBox(Modifier, it.toString())
//                }
//            }
        }
    }
}

@Composable
fun DayName(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun DayBox(modifier: Modifier = Modifier, text: Int, inBudget: Boolean, dayState: DayState) {
    val greenColorPalette = toPalette(greenColor)
    val orangeColorPalette = toPalette(orange)
    val background =
        if (inBudget) {
            when (dayState) {
                DayState.IN_LIMIT -> greenColorPalette.main.copy(alpha = 0.1f)
                DayState.NOT_STARTED -> Color.Blue
                DayState.OVER_LIMIT -> orangeColorPalette.main.copy(alpha = 0.1f)
            }

        } else Color.Transparent
    Box(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
    ) {
        Text(
            text = if (text != -1) text.toString() else "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 12.dp)
        )
    }
}

@Preview
@Composable
private fun SpendCalenderPreview() {
    SpendCalender(Modifier)
}