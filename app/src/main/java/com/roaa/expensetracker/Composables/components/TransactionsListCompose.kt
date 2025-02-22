package com.roaa.expensetracker.Composables.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.failureColor
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.successColor
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Converters.TransactionConverter
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel
import com.roaa.expensetracker.Model.emptyBank
import com.roaa.expensetracker.Model.emptyCategoryClass
import com.roaa.expensetracker.Model.emptyTotalExpenseIncomeClass
import com.roaa.expensetracker.Model.emptyTransactionClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.StatisticsComponent.BarChart
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.LongMillisToNoralLong
import com.roaa.expensetracker.Utilities.convertMonthShortToFullName
import com.roaa.expensetracker.Utilities.createListForBarGraph
import com.roaa.expensetracker.Utilities.currentYear
import com.roaa.expensetracker.Utilities.getAllDatesWithDayNameForMonth
import com.roaa.expensetracker.Utilities.getFirstAndLastMonth
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toDisplayDate
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListCompose(
    navController: NavigationManager,
    modifier: Modifier,
    showSingleDateTransactions: Boolean,
    date: Long,
    uiViewModel: UiViewModel,
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    var showAddBottomSheet by remember { mutableStateOf(false) }
    val currentSelectedMonth by uiViewModel.selectedMonth.collectAsState()
    val monthName =
        convertMonthShortToFullName(currentSelectedMonth)
    var selectedMonthString by remember { mutableStateOf(monthName) }

    var firstAndLastDates = getFirstAndLastMonth(currentSelectedMonth)
    val totalAmountList by transactionViewModel.getListOfTotalAmountPerDayForRangeForCompose(
        firstAndLastDates.first,
        firstAndLastDates.second
    ).collectAsState(listOf(emptyTotalExpenseIncomeClass))

    val totalAmountMap = totalAmountList.associateBy { it.date }

    var currentMonthAllDayAndDatesListAndMaxValue =
        createListForBarGraph(
            getAllDatesWithDayNameForMonth(
                currentSelectedMonth
            ),
            totalAmountMap
        )

    LaunchedEffect(currentSelectedMonth) {
        if (monthName.split(" ").get(1) == currentYear) {
            selectedMonthString = monthName.split(" ").get(0)
        } else {
            selectedMonthString = monthName
        }
        currentMonthAllDayAndDatesListAndMaxValue = createListForBarGraph(
            getAllDatesWithDayNameForMonth(
                currentSelectedMonth
            ),
            totalAmountMap
        )
    }

    var bottomSheet by remember { mutableStateOf(false) }
    val showNewLayouts by preferencesViewModel.showForecastBar.collectAsState(false)
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                showAddBottomSheet = !showAddBottomSheet
            },
            icon = { Icon(Icons.Filled.Add, "Localized description") },
            text = { Text(text = "Add") },
        )
    }) {

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var singleTransaction by remember {
            mutableStateOf(
                TransactionWithDetails(
                    emptyTransactionClass,
                    emptyCategoryClass,
                    emptyBank
                )
            )
        }
        val scope = rememberCoroutineScope()
        val showForecast by preferencesViewModel.showForecastBar.collectAsState(false)
        val orangePalette = toPalette(orange)
        val greenPalette = toPalette(greenColor)


        val selectedMonth by uiViewModel.selectedMonth.collectAsState()
        val pagerState = rememberPagerState(initialPage = 500 / 2, pageCount = { 500 })

        LaunchedEffect(selectedMonth) {
            val (firstDate, lastDate) = getFirstAndLastMonth(selectedMonth)
            transactionViewModel.getTotalExpenseForRange(firstDate, lastDate)
            transactionViewModel.getTotalIncomeForRange(firstDate, lastDate)
        }

        Column {
            if (!showSingleDateTransactions) {
                HorizontalPager(state = pagerState, userScrollEnabled = false) {
                    val (firstDate, lastDate) = getFirstAndLastMonth(selectedMonth)
                    val transactionListOfMonth by transactionViewModel.getTotalTransactionForMonth(
                        firstDate,
                        lastDate
                    ).collectAsState(emptyList())

                    val totalExpenseForMonth by transactionViewModel.getTotalExpenseAmountForRangeFlow.collectAsState()
                    val totalIncomeForMonth by transactionViewModel.getTotalIncomeAmountForRangeFlow.collectAsState()

                    val transactionsMap =
                        transactionListOfMonth.sortedByDescending { it.transaction.date }
                            .groupBy { it.transaction.date }
                            .toSortedMap()

                    val transactionConverterList = transactionsMap.map {
                        TransactionConverter(it.key.toString(), it.value)
                    }.reversed()
                    val lazyList = rememberLazyListState()
                    Surface(color = MaterialTheme.colorScheme.surface) {
                        if (!transactionConverterList.isEmpty())
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(), state = lazyList
                            ) {

                                item {
                                    // SummaryCard(blueColor)
                                    Row(modifier = Modifier.padding(top = 8.dp)) {
//                                        Spacer(Modifier.width(16.dp))
//                                        HomeStatCard(
//                                            Modifier.weight(1f),
//                                            parseAmount(totalExpenseForMonth.totalAmount),
//                                            "Total Expense",
//                                            toPalette(orange)
//                                        )
//                                        Spacer(Modifier.width(8.dp))
//                                        HomeStatCard(
//                                            Modifier.weight(1f),
//                                            parseAmount(totalIncomeForMonth.totalAmount),
//                                            "Total Income",
//                                            toPalette(greenColor)
//                                        )
//                                        Spacer(Modifier.width(8.dp))
                                        HomeStatCardNew(
                                            Modifier,
                                            parseAmount(totalIncomeForMonth.totalAmount),
                                            parseAmount(totalExpenseForMonth.totalAmount),
                                            currentSelectedMonth,
                                            selectedMonthString,
                                            currentMonthAllDayAndDatesListAndMaxValue,
                                        )
//
                                    }
                                }
                                transactionConverterList.forEach { (date, transactionList) ->
                                    val date = transactionList.get(0).transaction.date
                                    item {
                                        Header(
                                            if (date == System.currentTimeMillis()
                                                    .LongMillisToNoralLong()
                                            ) "Today" else date.toLocalDate().toDisplayDate()
                                        )
                                    }
                                    items(transactionList, key = { it.transaction.id }) { item ->
                                        SingleTransaction(item, onSingleItemClick = {
                                            singleTransaction = (item)
                                            scope.launch {
                                                uiViewModel.transactionDetailsWithViewModelFlow.emit(
                                                    singleTransaction
                                                )
                                            }
                                            if (showNewLayouts)
                                                navController.navigateTo(
                                                    Destinations.DetailsScreen(
                                                        it.transaction.amount,
                                                        it.category.categoryName
                                                    )
                                                )
                                            else
                                                bottomSheet = true

                                        })
                                    }
                                }
                            }
                        else EmptyScreen()
                    }
                }
            } else {
                transactionViewModel.getAllTransactionsForDate(date)
                val transactionList by transactionViewModel.getAllTransactionsForDateCompose(
                    date
                ).collectAsState(listOf())
                val lazyList = rememberLazyListState()
                Surface(color = MaterialTheme.colorScheme.surface) {
                    if (!transactionList.isEmpty()) LazyColumn(
                        modifier = Modifier.fillMaxWidth(), state = lazyList
                    ) {
                        items(transactionList, key = { it.transaction.id }) { item ->
                            SingleTransaction(item, onSingleItemClick = {
                                singleTransaction = (item)
                                scope.launch {
                                    uiViewModel.transactionDetailsWithViewModelFlow.emit(
                                        singleTransaction
                                    )
                                }
                                if (showNewLayouts)
                                    navController.navigateTo(
                                        Destinations.DetailsScreen(
                                            it.transaction.amount,
                                            it.category.categoryName
                                        )
                                    )
                                else
                                    bottomSheet = true
                            })
                        }
                    }
                    else EmptyScreen()
                }
            }

            if (bottomSheet) {
                BottomSheetContentItemDetails(
                    bottomSheetState,
                    singleTransaction,
                    { bottomSheet = !bottomSheet })
            }
            if (showAddBottomSheet) {
                AddBottomSheet(date, { showAddBottomSheet = !showAddBottomSheet })
            }
        }
    }
}

@Composable
fun SingleTransaction(
    item: TransactionWithDetails,
    onSingleItemClick: (TransactionWithDetails) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onSingleItemClick(item)
            }, colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                angle = 0.3f,
            )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 12.dp)
        ) {
            var amount = parseAmount(item.transaction.amount)
            var amountColor = successColor
            if (item.category.categoryType.equals(EXPENSE)) {
                amount = "-₹" + amount
                amountColor = failureColor
            } else {
                amount = "+₹" + amount
                amountColor = successColor
            }
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
                        rememberAsyncImagePainter(IconState.fromNumber(item.category.categoryIconNumber))
                    Image(
                        painter = image,
                        contentDescription = "Test Image",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }


            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.60f)

            ) {
                Text(
                    text = item.transaction.note.replaceFirstChar { it.uppercase() },
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (true) {
                    Text(
                        text = item.category.categoryName,
                        style = typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = amount,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                style = typography.titleMedium,
                fontFamily = CustomFonts.numberFont,
                color = amountColor,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeStatCardNew(
    modifier: Modifier = Modifier,
    income: String,
    expense: String,
    currentSelectedMonth: String,
    selectedMonthShort:String,
    currentMonthAllDayAndDatesListAndMaxValue: Pair<List<BarChartExpenseModel>, Float>
) {
    val palette =
        toPalette(orange)
    val cardColor = combineColors(
        MaterialTheme.colorScheme.surface,
        palette.container,
        angle = 0.7f,
    )
    var mainContentVisibility by remember { mutableStateOf(false) }
    val currentMontAllDayList = currentMonthAllDayAndDatesListAndMaxValue.first
    val maxExpense = currentMonthAllDayAndDatesListAndMaxValue.second

    Card(
        modifier = modifier.padding(horizontal = 12.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
        ) {
            val (content, decoration1, decoration2) = createRefs()
            Column(Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .zIndex(1f)) {
                Box(Modifier.clickable { mainContentVisibility = !mainContentVisibility }) {
                    Row(
                        Modifier
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(24.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    palette.container,
                                    shape = RoundedCornerShape(50)
                                )
                                .weight(0.1f)
                                .aspectRatio(1f)
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.PieChart,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Pie Icon"
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(0.8f)) {
                            Text(
                                text = selectedMonthShort,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                style = typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "234 transactions",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                style = typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        IconButton(
                            onClick = { mainContentVisibility = !mainContentVisibility },
                            modifier = Modifier.weight(0.1f)
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
                AnimatedVisibility(mainContentVisibility) {
                    Column(
                        Modifier
                            .height(240.dp)
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                    ) {
                        BoxWithConstraints {
                            BarChart(
                                Modifier.fillMaxSize(),
                                currentSelectedMonth,
                                maxWidth,
                                maxHeight,
                                currentMontAllDayList,
                                maxExpense,
                                palette
                            )
                        }
                    }
                }
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )
                Row(
                    Modifier
                ) {
                    HomeStatCardSingleNew(Modifier.weight(1f), expense, "Expense")
//                VerticalDivider(
//                    thickness = 5.dp,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                    modifier = Modifier
//                        .zIndex(1f)
//                        .weight(0.2f)
//                        .padding(horizontal = 12.dp)
//                )
                    HomeStatCardSingleNew(Modifier.weight(1f), income, "Income")
                }
            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(128.dp)
                    .constrainAs(decoration1) {
                        top.linkTo(parent.top, margin = -50.dp)
                        end.linkTo(parent.end, margin = -50.dp)
                    }, colorFilter = ColorFilter.tint(palette.container)
            )

            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(128.dp)
                    .constrainAs(decoration2) {
                        bottom.linkTo(parent.bottom, margin = -80.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }, colorFilter = ColorFilter.tint(palette.container)
            )
        }
    }
}


@Composable
fun HomeStatCardSingleNew(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
) {
    Box(
        modifier = modifier
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
        ) {
            val (content) = createRefs()
            Column(
                modifier = Modifier
                    .padding(0.dp, 16.dp)
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    text = "₹ $value",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.bodySmall.copy(
                        fontFamily = CustomFonts.numberFont,
                        fontSize = 21.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun HomeStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    palette: HarmonizedColorPalette
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                palette.container,
                angle = 0.5f,
            )
        )
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
        ) {
            val (content, decoration1) = createRefs()
            Column(
                modifier = Modifier
                    .padding(0.dp, 16.dp)
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    text = "₹ $value",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.bodyMedium.copy(
                        fontFamily = CustomFonts.numberFont,
                        fontSize = 21.sp
                    ),
                    color = palette.onContainer
                )
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(64.dp)
                    .constrainAs(decoration1) {
                        top.linkTo(parent.top, margin = -10.dp)
                        start.linkTo(parent.start, margin = -10.dp)
                    }, alpha = 0.6f, colorFilter = ColorFilter.tint(palette.container)
            )
        }
    }
}


@Composable
fun SingleTransactionNew(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {

    Card(
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                angle = 0.3f,
            ),
        ), modifier = Modifier.padding(16.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = item.dateWithTime.toDisplayDate(), style = typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = combineColors(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant,
                        angle = 0.3f,
                    ),
                ),
            ) {
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L,1L,1L),
//                    {})
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
//                    {})
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
//                    {})
            }

        }
    }


}

@Composable
fun Header(date: String) {
    Text(
        text = date,
        style = typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 14.dp, 8.dp, 4.dp)
    )
}


@Preview
@Composable
fun SingleTransactionPreview() {
    //  SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L), {})
}


@Preview
@Composable
fun SingleTransactionNewPreview() {
    // SingleTransactionNew(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L)) {}
}

@Preview
@Composable
fun HeaderPreview() {
    Header("Today")
}