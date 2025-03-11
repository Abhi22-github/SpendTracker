package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.BubbleChart
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.aay.compose.donutChart.model.PieChartData
import com.roaa.expensetracker.Activity.LocalCurrency
import com.roaa.expensetracker.Composables.CustomFonts
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.StatisticsComponent.AnimatedPieChart
import com.roaa.expensetracker.Composables.StatisticsComponent.BarChartStatisticsScreen
import com.roaa.expensetracker.Composables.StatisticsComponent.BarChartTest
import com.roaa.expensetracker.Composables.StatisticsComponent.PieData
import com.roaa.expensetracker.Composables.StatisticsComponent.Test
import com.roaa.expensetracker.Composables.StatisticsComponent.generateDynamicColors
import com.roaa.expensetracker.Composables.cardBackgroundColor
import com.roaa.expensetracker.Composables.color1
import com.roaa.expensetracker.Composables.color2
import com.roaa.expensetracker.Composables.color3
import com.roaa.expensetracker.Composables.color4
import com.roaa.expensetracker.Composables.color5
import com.roaa.expensetracker.Composables.color6
import com.roaa.expensetracker.Composables.color7
import com.roaa.expensetracker.Composables.color8
import com.roaa.expensetracker.Composables.components.DatePickerModal
import com.roaa.expensetracker.Composables.components.ErrorRow
import com.roaa.expensetracker.Composables.components.SingleTransaction
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Hilt.AllViewModel
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.UtilityModalClass.CategorySummaryClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTotalExpenseIncomeClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTransactionWithDetailsClass
import com.roaa.expensetracker.Utilities.colorList
import com.roaa.expensetracker.Utilities.createListForBarGraph
import com.roaa.expensetracker.Utilities.currentYear
import com.roaa.expensetracker.Utilities.getDatesBetween
import com.roaa.expensetracker.Utilities.getDayDifference
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Months
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Weeks
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.Utilities.getPreviousAndNext500DaysForFilter
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toDisplayStringForMonthWithYear
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.Utilities.toLongMillis
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(
    navHostController: NavHostController,
    navigationManager: NavigationManager,
) {
    val options = listOf("Day", "Week", "Month")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val rememberLazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var filterRowContent by remember { mutableStateOf(getPreviousAndNext500Days(LocalDate.now())) }
    var selectedIndexForFilterChip by remember { mutableStateOf(filterRowContent.size / 2) }
    var categoryWiseDropDown by remember { mutableStateOf(true) }
    var expenseWiseDropDown by remember { mutableStateOf(true) }
    var incomeWiseDropDown by remember { mutableStateOf(true) }
    LaunchedEffect(selectedIndex) {
        filterRowContent = when (selectedIndex) {
            0 -> getPreviousAndNext500DaysForFilter(LocalDate.now())
            1 -> getPreviousAndNext100Weeks()
            2 -> getPreviousAndNext100Months(LocalDate.now())
            else -> getPreviousAndNext500Days(LocalDate.now())
        }
        selectedIndexForFilterChip = filterRowContent.size / 2
        rememberLazyListState.scrollToItem((filterRowContent.size / 2))
    }

    BackHandler() {
        handleBackNavigation(navigationManager)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = "Statistics",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                delete = {})
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Column {
                    FilledTonalButton(
                        onClick = {}, contentPadding = PaddingValues(
                            start = 24.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ), colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = "last 1 month", style = typography.titleMedium
                        )
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Settings")

                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Column {
                    ThreeOptionTextSwitch(
                        selectedIndex = selectedIndex,
                        items = options,
                        onSelectionChange = {
                            selectedIndex = it
                        })
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.Tune, contentDescription = "Settings")
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(
                state = rememberLazyListState, horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filterRowContent) { index, text ->
                    ChipsForFilter(
                        index, selectedIndexForFilterChip, text
                    ) { selectedIndexForFilterChip = it }
                }

            }
            Spacer(Modifier.height(32.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoryWiseDropDown = !categoryWiseDropDown },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category wise analysis",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .weight(0.9f),
                            style = typography.titleMedium
                        )
                        IconButton(onClick = { categoryWiseDropDown = !categoryWiseDropDown }) {
                            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "down")
                        }
                    }
                    AnimatedVisibility(categoryWiseDropDown) {
                        Column {
                            Spacer(Modifier.height(8.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AnimatedPieChart(
                                    Modifier.size(240.dp), listOf(
                                        PieData("Food", 40f, color1),
                                        PieData("Transportation", 62f, color2),
                                        PieData("Fuel", 50f, color3),
                                        PieData("Other", 100f, color4),
                                        PieData("Food", 40f, color5),
                                        PieData("Transportation", 62f, color6),
                                        PieData("Fuel", 50f, color7),
                                        PieData("Other", 100f, color8)
                                    )
                                )
                                //Test(Modifier, pieDataList)
                                Text("Testing")
                            }
                            Spacer(Modifier.height(8.dp))
                            CategoryStatEntry(Modifier, color1)
                            CategoryStatEntry(Modifier, color2)
                            CategoryStatEntry(Modifier, color3)
                            CategoryStatEntry(Modifier, color4)
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
            Row {
                Column {
                    ThreeOptionTextSwitch(
                        selectedIndex = selectedIndex,
                        items = options,
                        onSelectionChange = {
                            selectedIndex = it
                        })
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expenseWiseDropDown = !expenseWiseDropDown },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Expense Analysis",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .weight(0.9f),
                            style = typography.titleMedium
                        )
                        IconButton(onClick = { expenseWiseDropDown = !expenseWiseDropDown }) {
                            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "down")
                        }
                    }

                    AnimatedVisibility(expenseWiseDropDown) {
                        Column {
                            val palette = toPalette(orange)
                            BarChartTest(
                                modifier = Modifier, palette = palette
                            )
                        }

                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { incomeWiseDropDown = !incomeWiseDropDown },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Income Analysis",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .weight(0.9f),
                            style = typography.titleMedium
                        )
                        IconButton(onClick = { incomeWiseDropDown = !incomeWiseDropDown }) {
                            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "down")
                        }
                    }
                    AnimatedVisibility(incomeWiseDropDown) {
                        val palette = toPalette(orange)
                        BarChartTest(
                            modifier = Modifier, palette = palette
                        )

                    }
                }
            }
        }


    }
}

@Composable
fun ChipsForFilter(
    index: Int, selectedIndexForFilterChip: Int, text: String, selectChip: (Int) -> Unit
) {
    val temp = if (text.split(",").get(1) == currentYear) text.split(",")[0] else text
    FilterChip(
        onClick = { selectChip(index) },
        label = {
            Text(text = temp)
        },
        selected = index == selectedIndexForFilterChip,
        leadingIcon = if (index == selectedIndexForFilterChip) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline),
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh))
}

@Composable
fun BankChips(
    selectedBankAccountsClass: BankAccountsClass,
    bankAccountsClass: BankAccountsClass,
    selectChip: (BankAccountsClass) -> Unit
) {
    FilterChip(
        onClick = { selectChip(bankAccountsClass) },
        label = {
            Text(
                text = bankAccountsClass.bankName,
                modifier = Modifier.padding(vertical = 8.dp),
                style = typography.bodyMedium
            )
        },
        selected = selectedBankAccountsClass.bankAccountId == bankAccountsClass.bankAccountId,
        leadingIcon = if (selectedBankAccountsClass.bankAccountId == bankAccountsClass.bankAccountId) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(25.dp),
        border = if (selectedBankAccountsClass.bankAccountId == bankAccountsClass.bankAccountId) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        colors = FilterChipDefaults.filterChipColors())
}

@Composable
fun CategoryStatEntry(modifier: Modifier = Modifier, color: Color) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { }
                .background(color.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(0.8f), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(36.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image = rememberAsyncImagePainter(IconState.fromNumber(1))
                        Image(
                            painter = image,
                            contentDescription = "Image 1",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))

                Row() {
                    Text(
                        text = "Food & Expense",
                        style = typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "(60%)",
                        style = typography.bodyLarge.copy(fontFamily = CustomFonts.numberFont),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }


//                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = parseAmount(BigDecimal(34735)),
                style =typography.bodyLarge.copy(fontFamily = CustomFonts.numberFont)
            )
        }
    }
}

@Preview
@Composable
private fun CategoryStatEntryPreview() {
    CategoryStatEntry(Modifier, color1)
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun StatisticsScreenTest(
    navHostController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
) {
    val options = listOf("Expense", "Income")
    var selectedIndex by remember { mutableIntStateOf(0) }
    var startDate by remember { mutableStateOf<Long>(LocalDate.now().minusMonths(1).toLong()) }
    var endDate by remember { mutableStateOf<Long>(LocalDate.now().toLong()) }
    val scrollState = rememberScrollState()
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    var showBankAccountAnalysisBottomSheet by remember { mutableStateOf(false) }
    var showCategoryAnalysisBottomSheet by remember { mutableStateOf(false) }

    //Flows
    val transactionsForTimePeriodFromRoom by viewModel.transactionsViewModel.getTotalTransactionForPeriod(
        startDate, endDate
    ).collectAsState(
        listOf(emptyTransactionWithDetailsClass)
    )
    val bankAccountList by viewModel.bankAccountsViewModel.allBankAccountList.collectAsState()
    var selectedBankAccountClass by remember { mutableStateOf(emptyBank) }
    val totalAmountListForTimePeriodFromRoom by viewModel.transactionsViewModel.getListOfTotalAmountPerDayForRangeForCompose(
        startDate, endDate
    ).collectAsState(listOf(emptyTotalExpenseIncomeClass))

    val totalAmountMap = totalAmountListForTimePeriodFromRoom.associateBy { it.date }

    val expenseTransactions =
        transactionsForTimePeriodFromRoom.filter { it.transaction.type == EXPENSE }
    val totalExpense = expenseTransactions.sumOf { it.transaction.amount }
    val incomeTransaction =
        transactionsForTimePeriodFromRoom.filter { it.transaction.type == INCOME }
    val totalIncome = incomeTransaction.sumOf { it.transaction.amount }

    val title = if (selectedIndex == 0) "Total Expense" else "Total Income"
    val amount = if (selectedIndex == 0) totalExpense else totalIncome
    val transactionCount =
        if (selectedIndex == 0) expenseTransactions.size else incomeTransaction.size


    var currentTimePeriodExpenseAllDayAndDatesListAndMaxValue = createListForBarGraph(
        getDatesBetween(
            startDate.toLocalDate(), endDate.toLocalDate()
        ), totalAmountMap
    )

    var totalAmount = BigDecimal.ZERO

    val categoryListData =
        (if (selectedIndex == 0) expenseTransactions else incomeTransaction).groupBy { it.category }
            .mapValues { (category, list) ->
                totalAmount += list.fold(BigDecimal.ZERO) { acc, i -> acc + i.transaction.amount }
                CategorySummaryClass(
                    category,
                    list.size,
                    ((list.size.toBigDecimal().div(transactionCount.toBigDecimal())) * BigDecimal(100)),
                    list.sumOf { it.transaction.amount},
                    colorList.random()
                )
            }
    categoryListData.onEachIndexed { index, entry ->
        entry.value.color = colorList[index]
        entry.value.percentage =
            ((entry.value.totalAmount / if (totalAmount == BigDecimal.ZERO) BigDecimal.ONE else totalAmount) * BigDecimal(100))
    }
    val sortedCategoryListData = categoryListData.toList()
        .sortedByDescending { it.second.totalAmount } // Sort by value
        .toMap()



    BackHandler() {
        handleBackNavigation(navigationManager)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = "Statistics",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                delete = {})
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Column {
                    FilledTonalButton(
                        onClick = { showFilterBottomSheet = !showFilterBottomSheet },
                        contentPadding = PaddingValues(
                            start = 24.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = "${
                                startDate.toLocalDate().toDisplayStringForMonthWithYear()
                                    .split(",")[0]
                            }-${endDate.toLocalDate().toDisplayStringForMonthWithYear()}",
                            style =typography.titleMedium
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Rounded.FilterList, contentDescription = "Settings")

                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Row {
                Column {
                    TextSwitch(selectedIndex = selectedIndex, items = options, onSelectionChange = {
                        selectedIndex = it
                    })
                }
            }

            //          Spacer(Modifier.height(16.dp))
//            Column(
//                Modifier.padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Total Expense",
//                        "${LocalCurrency.current.currencySymbol} ${parseAmount(totalExpense)}"
//                    )
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Total Income",
//                        "${LocalCurrency.current.currencySymbol} ${parseAmount(totalIncome)}"
//                    )
//                }
//                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Average Expense",
//                        "${LocalCurrency.current.currencySymbol} ${
//                            parseAmount(
//                                totalExpense /
//                                        expenseTransactions.size
//                            )
//                        }"
//                    )
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Average Income",
//                        "${LocalCurrency.current.currencySymbol} ${
//                            parseAmount(
//                                totalIncome /
//                                        incomeTransaction.size
//                            )
//                        }"
//                    )
//                }
//            }

            Spacer(Modifier.height(24.dp))
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        AnimatedContent(targetState = title) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        AnimatedContent(targetState = amount) {
                            Text(
                                text = "${LocalCurrency.current.currencySymbol} ${parseAmount(it)}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                                style = typography.headlineMedium.copy(fontFamily = CustomFonts.numberFont)
                            )
                        }
                        AnimatedContent(targetState = transactionCount) {
                            Text(
                                text = "${it} Transaction",
                                modifier = Modifier.padding(
                                    horizontal = 16.dp, vertical = 8.dp
                                ),
                                style = typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                    FilledTonalButton(
                        onClick = {}, contentPadding = PaddingValues(
                            start = 24.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ), colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ), modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Daily", style = typography.bodyMedium
                        )
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Settings")

                    }
                }

                key(currentTimePeriodExpenseAllDayAndDatesListAndMaxValue.first) {
                    Column {
                        val palette = toPalette(orange)
                        BarChartStatisticsScreen(
                            modifier = Modifier,
                            currentTimePeriodExpenseAllDayAndDatesListAndMaxValue.first,
                            palette = palette,
                            selectedIndex = selectedIndex
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = "Category",
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Spacer(Modifier.height(16.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        val pieDataList = sortedCategoryListData.map {
                            PieChartData(
                                partName = it.key.categoryName,
                                data = if (it.value.totalAmount == BigDecimal.ZERO) 1.0 else it.value.totalAmount.toDouble(),
                                color = it.value.color,
                            )
                        }
                        val colors = generateDynamicColors(6)
//                        AnimatedGapPieChart(
//                            Modifier.size(240.dp),pieDataList
//                        )
//                        Box(Modifier) {
//                            DonutChartSample(pieDataList)
//                        }
                        key(sortedCategoryListData) {
                            Test(
                                Modifier,
                                sortedCategoryListData,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.surface
                            )
                        }
                        // Text("Testing")
                    }
                    Spacer(Modifier.height(24.dp))
                    Column {
                        sortedCategoryListData.values.toList().forEach {
                            CategoryStatEntryTest(Modifier, it)
                        }
                    }
                }
            }
            //bottom statistics
            if (false) {
                Spacer(Modifier.height(24.dp))
                Row(Modifier.padding(horizontal = 16.dp)) {
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showBankAccountAnalysisBottomSheet = !showBankAccountAnalysisBottomSheet
                        },
                        contentPadding = PaddingValues(
                            start = 24.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(
                                    Icons.Rounded.BarChart,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Bank Account Analysis",
                                    style = typography.titleMedium
                                )
                            }

                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Settings")
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.padding(horizontal = 16.dp)) {
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showCategoryAnalysisBottomSheet = !showCategoryAnalysisBottomSheet
                        },
                        contentPadding = PaddingValues(
                            start = 24.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(
                                    Icons.Rounded.BubbleChart,
                                    contentDescription = "pie chart",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Category Wise Analysis",
                                    style = typography.titleMedium
                                )
                            }

                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Settings")
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (showFilterBottomSheet) {
        FilterBottomSheet(
            Modifier,
            viewModel,
            { showFilterBottomSheet = !showFilterBottomSheet },
            bankAccountList,
            { startDateFinal, endDateFinal, DurationFinal, bankAccountClassFinal ->
                startDate = startDateFinal
                endDate = endDateFinal
                showFilterBottomSheet = !showFilterBottomSheet
                selectedBankAccountClass = bankAccountClassFinal
            })
    }

    if (showBankAccountAnalysisBottomSheet) {
        BankAnalysisBottomSheet(
            Modifier,
            { showBankAccountAnalysisBottomSheet = !showBankAccountAnalysisBottomSheet },
            bankAccountList,
            transactionsForTimePeriodFromRoom
        )
    }

    if (showCategoryAnalysisBottomSheet) {
        // BankAnalysisBottomSheet()
    }


}

@Composable
fun CategoryStatEntryTest(
    modifier: Modifier = Modifier, categorySummaryClass: CategorySummaryClass
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(0.8f), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(36.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(categorySummaryClass.categoryClass.categoryIconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Image 1",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))
                Row() {
                    Box(
                        Modifier.background(
                            categorySummaryClass.color, RoundedCornerShape(
                                topEnd = 0.dp,
                                topStart = 15.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 15.dp
                            )
                        )
                    ) {
                        Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(
                                text = "${String.format("%.2f", categorySummaryClass.percentage)}%",
                                style = typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Spacer(Modifier.width(5.dp))
                    Box(
                        Modifier.background(
                            categorySummaryClass.color.copy(alpha = 0.5f),
                            RoundedCornerShape(
                                topEnd = 15.dp,
                                topStart = 0.dp,
                                bottomEnd = 15.dp,
                                bottomStart = 0.dp
                            )
                        )
                    ) {
                        Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(
                                text = "${categorySummaryClass.categoryClass.categoryName} X${categorySummaryClass.transactionCount}",
                                style = typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
            Text(
                text = "${LocalCurrency.current.currencySymbol} ${parseAmount(categorySummaryClass.totalAmount)}",
                style = typography.bodyMedium.copy(fontFamily = CustomFonts.numberFont)
            )
        }
    }
}


//Filter Bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: AllViewModel,
    closeBottomSheet: () -> Unit,
    bankAccountList: List<BankAccountsClass>,
    saveButtonClicked: (Long, Long, Long, BankAccountsClass) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedBankAccount by remember { mutableStateOf(bankAccountList.get(0)) }
    var startDate by remember { mutableStateOf<Long>(LocalDate.now().minusMonths(1).toLong()) }
    var endDate by remember { mutableStateOf<Long>(LocalDate.now().toLong()) }
    var selectedDuration by remember {
        mutableStateOf(
            getDayDifference(
                startDate.toLocalDate(),
                endDate.toLocalDate()
            )
        )
    }
    val showErrorStatus by viewModel.uiViewModel.errorStatusInStatisticsFilter.collectAsState(false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(startDate, endDate) {
        if (endDate < startDate) {
            scope.launch {
                viewModel.uiViewModel.errorStatusInStatisticsFilter.emit(true)
                viewModel.uiViewModel.setErrorMessage("End Date should be greater than Start Date")
            }
        } else {
            viewModel.uiViewModel.errorStatusInStatisticsFilter.emit(false)
        }
        selectedDuration = getDayDifference(startDate.toLocalDate(), endDate.toLocalDate())
    }
    ModalBottomSheet(onDismissRequest = closeBottomSheet, sheetState = bottomSheetState) {
        FilterBottomSheetContent(
            Modifier.padding(horizontal = 16.dp),
            closeBottomSheet,
            bankAccountList,
            selectedBankAccount,
            { selectedBankAccount = it },
            startDate,
            { startDate = it },
            endDate,
            { endDate = it },
            selectedDuration,
            showErrorStatus,
            { saveButtonClicked(startDate, endDate, selectedDuration, selectedBankAccount) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit,
    bankAccountList: List<BankAccountsClass>,
    selectedBankAccount: BankAccountsClass,
    setSelectedChip: (BankAccountsClass) -> Unit,
    startDate: Long,
    setStartDate: (Long) -> Unit,
    endDate: Long,
    setEndDate: (Long) -> Unit,
    selectedDuration: Long,
    showErrorStatus: Boolean,
    saveButtonClicked: () -> Unit
) {
    val startDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = startDate.toLocalDate().toLongMillis())
    val endDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = endDate.toLocalDate().toLongMillis())
    var showStartDateDayPicker by remember { mutableStateOf(false) }
    var showEndDateDayPicker by remember { mutableStateOf(false) }
    Column(Modifier) {
        Column(
            verticalArrangement = Arrangement.Top, modifier = Modifier
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    Modifier
                        .weight(1f)
                        .background(
                            Color.Transparent, shape = RoundedCornerShape(25.dp)
                        )
                        .padding(start = 16.dp)
                        .border(
                            BorderStroke(
                                1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            ), RoundedCornerShape(25.dp)
                        )
                        .clip(RoundedCornerShape(25.dp))
                        .clickable {
                            showStartDateDayPicker = !showStartDateDayPicker
                        }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = startDate.toLocalDate().toDisplayStringForMonthWithYear(),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Box(
                    Modifier
                        .weight(1f)
                        .background(
                            Color.Transparent, shape = RoundedCornerShape(25.dp)
                        )
                        .padding(end = 16.dp)
                        .border(
                            BorderStroke(
                                1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            ), RoundedCornerShape(25.dp)
                        )
                        .clip(RoundedCornerShape(25.dp))
                        .clickable {
                            showEndDateDayPicker = !showEndDateDayPicker
                        }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = endDate.toLocalDate().toDisplayStringForMonthWithYear(),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy()
                        )
                    }
                }
            }
            Text(
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Selected duration ${selectedDuration} days",
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Account",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier
            ) {
                bankAccountList.forEachIndexed { index, bankAccountsClass ->
                    BankChips(
                        selectedBankAccount, bankAccountsClass
                    ) { setSelectedChip(it) }
                }
            }
            Spacer(Modifier.height(12.dp))
            ErrorRow(showErrorStatus)
            Spacer(Modifier.height(12.dp))
            FilledTonalButton(
                enabled = !showErrorStatus,
                onClick = { saveButtonClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Apply",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

            }
        }
    }
    if (showStartDateDayPicker) {
        DatePickerModal(startDatePickerState, { setStartDate(it ?: LocalDate.now().toLong()) }) {
            showStartDateDayPicker = !showStartDateDayPicker
        }
    }
    if (showEndDateDayPicker) {
        DatePickerModal(endDatePickerState, { setEndDate(it ?: LocalDate.now().toLong()) }) {
            showEndDateDayPicker = !showEndDateDayPicker
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBottomSheetContentPreview() {
    FilterBottomSheetContent(
        Modifier.padding(horizontal = 16.dp),
        { },
        listOf(),
        emptyBank,
        {},
        0L,
        {},
        0L,
        {}, 3L,
        false, {}
    )
}

//Bank Analysis Bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAnalysisBottomSheet(
    modifier: Modifier = Modifier,
    dismissBottomSheet: () -> Unit,
    bankAccountList: List<BankAccountsClass>,
    transactionsForTimePeriodFromRoom: List<TransactionWithDetails>
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedBankAccount by remember { mutableStateOf(bankAccountList.get(0)) }
    ModalBottomSheet(sheetState = bottomSheetState, onDismissRequest = { dismissBottomSheet() }) {
        BankAnalysisBottomSheetContent(
            Modifier.padding(horizontal = 16.dp),
            bankAccountList, transactionsForTimePeriodFromRoom,
            selectedBankAccount,
        ) {
            selectedBankAccount = it
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BankAnalysisBottomSheetContent(
    modifier: Modifier = Modifier,
    bankAccountList: List<BankAccountsClass>,
    transactionsForTimePeriodFromRoom: List<TransactionWithDetails>,
    selectedBankAccount: BankAccountsClass,
    setSelectedBankAccount: (BankAccountsClass) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier) {
            Text(
                modifier = Modifier,
                text = "Account",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.fillMaxWidth()
        ) {
            bankAccountList.forEachIndexed { index, bankAccountsClass ->
                BankChips(
                    selectedBankAccount, bankAccountsClass
                ) { setSelectedBankAccount(it) }
            }
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(
            modifier = modifier,
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn() {
            item {
                Column(
                    Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SingleInfoBox(
                            Modifier.weight(1f),
                            "Minimum Spend",
                            "${LocalCurrency.current.currencySymbol} 3000",
                        )
                        SingleInfoBox(
                            Modifier.weight(1f),
                            "Maximum Spend",
                            "${LocalCurrency.current.currencySymbol} 3000",
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SingleInfoBox(
                            Modifier.weight(1f),
                            "Total Transactions",
                            "39",
                        )
                    }
                }
                BarChartTest(Modifier, palette = toPalette(orange))
            }
            item {
                if (transactionsForTimePeriodFromRoom.isNotEmpty()) {
                    Text(
                        modifier = modifier.padding(vertical = 8.dp),
                        text = "Transactions",
                        style = typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            items(transactionsForTimePeriodFromRoom, key = { it.transaction.id }) {
                SingleTransaction(it) {
//                    singleTransaction = it
//                    bottomSheet = !bottomSheet
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BankAnalysisBottomSheetContentPreview() {
    BankAnalysisBottomSheetContent(
        modifier = Modifier.padding(horizontal = 16.dp),
        transactionsForTimePeriodFromRoom = listOf(),
        bankAccountList = listOf(emptyBank, emptyBank, emptyBank),
        selectedBankAccount = emptyBank,
        setSelectedBankAccount = { })
}

