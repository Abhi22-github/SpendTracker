package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.StatisticsComponent.BarChartStatisticsScreen
import com.roaa.expensetracker.Composables.StatisticsComponent.BarChartTest
import com.roaa.expensetracker.Composables.StatisticsComponent.Test
import com.roaa.expensetracker.Composables.cardBackgroundColor
import com.roaa.expensetracker.Composables.color1
import com.roaa.expensetracker.Composables.color2
import com.roaa.expensetracker.Composables.color3
import com.roaa.expensetracker.Composables.color4
import com.roaa.expensetracker.Composables.components.FilterBottomSheet
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.UtilityModalClass.CategorySummaryClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTotalExpenseIncomeClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTransactionWithDetailsClass
import com.roaa.expensetracker.Utilities.createListForBarGraph
import com.roaa.expensetracker.Utilities.currentYear
import com.roaa.expensetracker.Utilities.getDatesBetween
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Months
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Weeks
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.Utilities.getPreviousAndNext500DaysForFilter
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toDisplayStringForMonthWithYear
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.BankAccountsViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
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
            TopBar(title = "Statistics",
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
                            text = "last 1 month", style = MaterialTheme.typography.titleMedium
                        )
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Settings")

                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Column {
                    ThreeOptionTextSwitch(selectedIndex = selectedIndex,
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
                            style = MaterialTheme.typography.titleMedium
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
//                AnimatedPieChart(
//                    Modifier.size(240.dp), listOf(
//                        PieData("Food", 40, color1),
//                        PieData("Transportation", 62, color2),
//                        PieData("Fuel", 50, color3),
//                        PieData("Other", 100, color4),
//                        PieData("Food", 40, color5),
//                        PieData("Transportation", 62, color6),
//                        PieData("Fuel", 50, color7),
//                        PieData("Other", 100, color8)
//                    )
//                )
                                Test(Modifier)
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
                    ThreeOptionTextSwitch(selectedIndex = selectedIndex,
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
                            style = MaterialTheme.typography.titleMedium
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
                            style = MaterialTheme.typography.titleMedium
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
    FilterChip(onClick = { selectChip(index) },
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
    index: Int,
    selectedBankAccountsClass: BankAccountsClass,
    bankAccountsClass: BankAccountsClass,
    selectChip: (BankAccountsClass) -> Unit
) {
    FilterChip(onClick = { selectChip(bankAccountsClass) },
        label = {
            Text(
                text = if (bankAccountsClass.bankName == "Cash") "All accounts" else bankAccountsClass.bankName,
                modifier = Modifier.padding(vertical = 8.dp)
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
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline),
        colors = FilterChipDefaults.filterChipColors())
}

@Composable
fun CategoryStatEntry(modifier: Modifier = Modifier, color: Color) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(Modifier
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
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "(60%)",
                        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = CustomFonts.numberFont),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }


//                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = parseAmount(34735f),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = CustomFonts.numberFont)
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreenTest(
    navHostController: NavHostController,
    navigationManager: NavigationManager,
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel()
) {
    val options = listOf("Expense", "Income")
    var selectedIndex by remember { mutableIntStateOf(0) }
    var startDate by remember { mutableStateOf<Long>(LocalDate.now().minusMonths(1).toLong()) }
    var endDate by remember { mutableStateOf<Long>(LocalDate.now().toLong()) }
    val scrollState = rememberScrollState()
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    //Flows
    val transactionsForTimePeriodFromRoom by transactionsViewModel.getTotalTransactionForPeriod(
        startDate, endDate
    ).collectAsState(
        listOf(emptyTransactionWithDetailsClass)
    )
    val bankAccountList by bankAccountsViewModel.allBankAccountList.collectAsState()
    var selectedBankAccountClass by remember { mutableStateOf(emptyBank) }
    val totalAmountListForTimePeriodFromRoom by transactionsViewModel.getListOfTotalAmountPerDayForRangeForCompose(
        startDate, endDate
    ).collectAsState(listOf(emptyTotalExpenseIncomeClass))

    val totalAmountMap = totalAmountListForTimePeriodFromRoom.associateBy { it.date }

    val expenseTransactions =
        transactionsForTimePeriodFromRoom.filter { it.transaction.type == EXPENSE }
    val totalExpense = expenseTransactions.sumOf { it.transaction.amount.toDouble() }.toFloat()
    val incomeTransaction =
        transactionsForTimePeriodFromRoom.filter { it.transaction.type == INCOME }
    val totalIncome = incomeTransaction.sumOf { it.transaction.amount.toDouble() }.toFloat()

    val title = if (selectedIndex == 0) "Total Expense" else "Total Income"
    val amount = if (selectedIndex == 0) totalExpense else totalIncome
    val transactionCount =
        if (selectedIndex == 0) expenseTransactions.size else incomeTransaction.size


    var currentTimePeriodExpenseAllDayAndDatesListAndMaxValue = createListForBarGraph(
        getDatesBetween(
            startDate.toLocalDate(), endDate.toLocalDate()
        ), totalAmountMap
    )


    val categoryListData =
        (if (selectedIndex == 0) expenseTransactions else incomeTransaction).groupBy { it.category }
            .mapValues { (category, list) ->
                CategorySummaryClass(
                    category,
                    list.size,
                    ((list.size.toFloat() / transactionCount.toFloat()) * 100),
                    list.sumOf { it.transaction.amount.toDouble() }.toFloat()
                )
            }


    BackHandler() {
        handleBackNavigation(navigationManager)
    }
    Scaffold(
        topBar = {
            TopBar(title = "Statistics",
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
                            style = MaterialTheme.typography.titleMedium
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
//                        "₹ ${parseAmount(totalExpense)}"
//                    )
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Total Income",
//                        "₹ ${parseAmount(totalIncome)}"
//                    )
//                }
//                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Average Expense",
//                        "₹ ${
//                            parseAmount(
//                                totalExpense /
//                                        expenseTransactions.size
//                            )
//                        }"
//                    )
//                    SingleInfoBox(
//                        Modifier.weight(1f),
//                        "Average Income",
//                        "₹ ${
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        AnimatedContent(targetState = amount) {
                            Text(
                                text = "₹ ${parseAmount(it)}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = CustomFonts.numberFont)
                            )
                        }
                        AnimatedContent(targetState = transactionCount) {
                            Text(
                                text = "${it} Transaction",
                                modifier = Modifier.padding(
                                    horizontal = 16.dp, vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.labelLarge,
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
                            text = "Daily", style = MaterialTheme.typography.bodyMedium
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
                    text = "Category",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Column {
                    categoryListData.values.toList().forEach {
                        CategoryStatEntryTest(Modifier, it)

                    }
                }

            }
        }
    }

    if (showFilterBottomSheet) {
        FilterBottomSheet(Modifier,
            { showFilterBottomSheet = !showFilterBottomSheet },
            bankAccountList,
            { startDateFinal, endDateFinal, DurationFinal, bankAccountClassFinal ->
                startDate = startDateFinal
                endDate = endDateFinal
                showFilterBottomSheet = !showFilterBottomSheet
                selectedBankAccountClass = bankAccountClassFinal
            })
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
        Row(Modifier
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
                    Text(
                        text = "${categorySummaryClass.categoryClass.categoryName} X${categorySummaryClass.transactionCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
//                    Text(
//                        text = "(${categorySummaryClass.percentage.toInt()}%)",
//                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CustomFonts.numberFont),
//                        color = MaterialTheme.colorScheme.onSurface,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                    )
                }

            }
            Text(
                text = "₹ ${parseAmount(categorySummaryClass.totalAmount)}",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CustomFonts.numberFont)
            )
        }
    }
}

