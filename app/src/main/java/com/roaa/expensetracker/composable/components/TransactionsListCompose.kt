package com.roaa.expensetracker.composable.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.CustomFonts
import com.roaa.expensetracker.composable.color4
import com.roaa.expensetracker.composable.failureColor
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.statisticsComponent.BarChart
import com.roaa.expensetracker.composable.statisticsComponent.BarChartTest
import com.roaa.expensetracker.composable.successColor
import com.roaa.expensetracker.composable.utils.HarmonizedColorPalette
import com.roaa.expensetracker.composable.utils.IconState
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.converters.TransactionConverter
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.model.uiDataModels.BarChartExpenseModel
import com.roaa.expensetracker.notification.budgetNotificationChannel
import com.roaa.expensetracker.notification.sendNotification
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.LongMillisToNormalLong
import com.roaa.expensetracker.utilities.convertMonthShortToFullName
import com.roaa.expensetracker.utilities.createListForBarGraph
import com.roaa.expensetracker.utilities.currentYear
import com.roaa.expensetracker.utilities.getAllDatesWithDayNameForMonth
import com.roaa.expensetracker.utilities.getFirstAndLastMonth
import com.roaa.expensetracker.utilities.parseAmount
import com.roaa.expensetracker.utilities.toDisplayDate
import com.roaa.expensetracker.utilities.toLocalDate
import com.roaa.expensetracker.utilities.utilityModalClass.defaultBank
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCategoryClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyTotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyTransactionClass
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListCompose(
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    modifier: Modifier,
    showSingleDateTransactions: Boolean,
    date: Long,
) {
    val context = LocalContext.current
    val categoryOrBankSpecificAdd by viewModel.uiViewModel.addCategorySpecificOrBankSpecificTransaction.collectAsState()
    var showAddBottomSheet by remember { mutableStateOf(categoryOrBankSpecificAdd) }
    val currentSelectedMonth by viewModel.uiViewModel.selectedMonth.collectAsState()
    val monthName =
        convertMonthShortToFullName(currentSelectedMonth)
    var selectedMonthString by remember { mutableStateOf(monthName) }
    val getCurrentBudget by viewModel.budgetViewModel.getCurrentBudget.collectAsState()
    var isCardExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(getCurrentBudget) {
        viewModel.budgetViewModel.getCurrentBudgetWithDetailsForCompose()
        getCurrentBudget?.let {
            val currentExpenseLocal =
                it.budgetAllDays.fold(BigDecimal.ZERO) { acc, i ->
                    acc + i.totalExpense
                }
            val effectivePercentageAmount = it.budgetSummary.totalBudgetAmount.divide(
                BigDecimal(100), 2,
                RoundingMode.HALF_UP
            ).multiply(it.budgetSummary.notificationForBudgetUsage.toDouble().toBigDecimal())
            if (it.budgetSummary.totalBudgetAmount < currentExpenseLocal) {
                sendNotification(
                    context,
                    budgetNotificationChannel,
                    "‼️Budget Alert: 100% Used!",
                    "You've already spent 100% of your budget. Keep track to stay on top of your expenses!"
                )
                viewModel.uiViewModel.isBudgetExceededNotificationIsSent.value = true
            } else if (effectivePercentageAmount < currentExpenseLocal) {
                if (!viewModel.uiViewModel.isBudgetExceededNotificationIsSent.value) {
                    sendNotification(
                        context,
                        budgetNotificationChannel,
                        "‼️Budget Alert: ${it.budgetSummary.notificationForBudgetUsage}% Used!",
                        "You've already spent ${it.budgetSummary.notificationForBudgetUsage}% of your budget. Keep track to stay on top of your expenses!"
                    )
                    viewModel.uiViewModel.isBudgetExceededNotificationIsSent.value = true
                }
            }
        }


    }

    var firstAndLastDates = getFirstAndLastMonth(currentSelectedMonth)
    val totalAmountList by viewModel.transactionsViewModel.getListOfTotalAmountPerDayForRangeForCompose(
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
    val showExperimentalComponents by viewModel.preferencesViewModel.showExperimentalComponent.collectAsState(
        false
    )
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showAddBottomSheet = !showAddBottomSheet
                },
                icon = { Icon(Icons.Filled.Add, "Localized description") },
                text = { Text(text = "Add") },
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) {

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var singleTransaction by remember {
            mutableStateOf(
                TransactionWithDetails(
                    emptyTransactionClass,
                    defaultCategoryClass,
                    defaultBank
                )
            )
        }
        val scope = rememberCoroutineScope()

        val selectedMonth by viewModel.uiViewModel.selectedMonth.collectAsState()
        val pagerState = rememberPagerState(initialPage = 500 / 2, pageCount = { 500 })
        val innerPagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })

        LaunchedEffect(selectedMonth) {
            val (firstDate, lastDate) = getFirstAndLastMonth(selectedMonth)
            viewModel.transactionsViewModel.getTotalExpenseForRange(firstDate, lastDate)
            viewModel.transactionsViewModel.getTotalIncomeForRange(firstDate, lastDate)
        }

        Column {
            if (!showSingleDateTransactions) {
                HorizontalPager(state = pagerState, userScrollEnabled = false) {
                    val (firstDate, lastDate) = getFirstAndLastMonth(selectedMonth)
                    val transactionListOfMonth by viewModel.transactionsViewModel.getTotalTransactionForPeriod(
                        firstDate,
                        lastDate
                    ).collectAsState(emptyList())

                    val totalExpenseForMonth by viewModel.transactionsViewModel.getTotalExpenseAmountForRangeFlow.collectAsState()
                    val totalIncomeForMonth by viewModel.transactionsViewModel.getTotalIncomeAmountForRangeFlow.collectAsState()

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
                                    Row(modifier = Modifier.padding(top = 8.dp)) {
                                        HorizontalPager(
                                            state = innerPagerState,
                                            userScrollEnabled = true,
                                            contentPadding = PaddingValues(
                                                top = 0.dp,
                                                end = 10.dp,
                                                start = 10.dp,
                                                bottom = 0.dp
                                            ),
                                            pageSpacing = -15.dp
                                        ) {
                                            when (it) {
                                                0 -> HomeStatCardBudget(
                                                    Modifier,
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalIncomeForMonth.totalAmount
                                                        )
                                                    }",
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalExpenseForMonth.totalAmount
                                                        )
                                                    }",
                                                    transactionListOfMonth.size,
                                                    selectedMonthString,
                                                    isCardExpanded,
                                                    { isCardExpanded = !isCardExpanded },
                                                    currentMonthAllDayAndDatesListAndMaxValue,
                                                )

                                                1 -> HomeStatCardNew(
                                                    Modifier,
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalIncomeForMonth.totalAmount
                                                        )
                                                    }",
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalExpenseForMonth.totalAmount
                                                        )
                                                    }",
                                                    transactionListOfMonth.size,
                                                    selectedMonthString,
                                                    isCardExpanded,
                                                    { isCardExpanded = !isCardExpanded },
                                                    currentMonthAllDayAndDatesListAndMaxValue,
                                                )

                                                2 -> HomeStatCardNew(
                                                    Modifier,
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalIncomeForMonth.totalAmount
                                                        )
                                                    }",
                                                    "${LocalCurrency.current.currencySymbol}${
                                                        parseAmount(
                                                            totalExpenseForMonth.totalAmount
                                                        )
                                                    }",
                                                    transactionListOfMonth.size,
                                                    selectedMonthString,
                                                    isCardExpanded,
                                                    { isCardExpanded = !isCardExpanded },
                                                    currentMonthAllDayAndDatesListAndMaxValue,
                                                )
                                            }
                                        }
                                    }
                                }
                                transactionConverterList.forEach { (date, transactionList) ->
                                    val newdate = transactionList.get(0).transaction.date
                                    item {
                                        Header(
                                            if (newdate == System.currentTimeMillis()
                                                    .LongMillisToNormalLong()
                                            ) "Today"
                                            else date.toLocalDate().toDisplayDate()
                                        )
                                    }
                                    items(transactionList, key = { it.transaction.id }) { item ->
                                        SingleTransaction(item, onSingleItemClick = {
                                            singleTransaction = (item)
                                            scope.launch {
                                                viewModel.uiViewModel.transactionDetailsWithViewModelFlow.emit(
                                                    singleTransaction
                                                )
                                            }
                                            if (showExperimentalComponents)
                                                navigationManager.navigateTo(
                                                    Destinations.DetailsScreen
                                                )
                                            else
                                                bottomSheet = true

                                        }, innerPagerState.settledPage == 0)
                                    }
                                }
                            }
                        else EmptyScreen()
                    }
                }
            } else {
                viewModel.transactionsViewModel.getAllTransactionsForDate(date)
                val transactionList by viewModel.transactionsViewModel.getAllTransactionsForDateCompose(
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
                                    viewModel.uiViewModel.transactionDetailsWithViewModelFlow.emit(
                                        singleTransaction
                                    )
                                }
                                if (showExperimentalComponents)
                                    navigationManager.navigateTo(
                                        Destinations.DetailsScreen
                                    )
                                else
                                    bottomSheet = true
                            }, false)
                        }
                    }
                    else EmptyScreen()
                }
            }

            if (bottomSheet) {
                BottomSheetContentItemDetails(
                    bottomSheetState,
                    viewModel,
                    singleTransaction,
                    { bottomSheet = !bottomSheet })
            }
            if (showAddBottomSheet) {
                AddBottomSheet(date, viewModel, {
                    showAddBottomSheet = !showAddBottomSheet
                    scope.launch {
                        viewModel.uiViewModel.addCategorySpecificOrBankSpecificTransaction.emit(
                            false
                        )
                    }
                })
            }
        }
    }
}

@Composable
fun SingleTransaction(
    item: TransactionWithDetails,
    onSingleItemClick: (TransactionWithDetails) -> Unit,
    showBudgetIndicator: Boolean
) {
    ConstraintLayout() {
        val (content, excludeFromBudgetStatus) = createRefs()
//        AnimatedVisibility(showBudgetIndicator) {
//        Box(
//            modifier = Modifier
//                .constrainAs(excludeFromBudgetStatus) {
//                    start.linkTo(parent.start, 16.dp)
//                    top
//                        .linkTo(parent.top, 4.dp)
//                }
//                .clip(   RoundedCornerShape(25.dp))
//                .background(
//                    successColor.copy(alpha = 0.5f),
//                    RoundedCornerShape(25.dp)
//                )
//                .zIndex(1f),
//            contentAlignment = Alignment.Center
//        ) {
//                Icon(
//                    Icons.Rounded.Check,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.size(16.dp)
//                )
////            Text(
////                text = "budget",
////                color = MaterialTheme.colorScheme.surfaceVariant,
////                style = MaterialTheme.typography.labelSmall,modifier = Modifier.padding(4.dp)
////            )
//        }
//        }
        Card(
            shape = RoundedCornerShape(12.dp), modifier = Modifier
                .padding(16.dp, 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onSingleItemClick(item)
                }
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
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
                if (item.transaction.type.equals(EXPENSE)) {
                    amount = "-${LocalCurrency.current.currencySymbol}" + amount
                    amountColor = failureColor
                } else {
                    amount = "+${LocalCurrency.current.currencySymbol}" + amount
                    amountColor = successColor
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .fillMaxSize()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
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
                    Row(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(
                            showBudgetIndicator,
                            enter = scaleIn(initialScale = 0.5f) + fadeIn(), // Scale from 50% to full size with fade-in
                            exit = scaleOut(targetScale = 0.5f) + fadeOut()
                        ) {
                            val image =
                                rememberAsyncImagePainter(R.drawable.ic_checkmark)
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = ColorFilter.tint(successColor),
                                alpha = 0.8f
                            )
                        }
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
                    Text(
                        text = item.category.categoryName,
                        style = typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.5f
                        ),
                    )
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
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeStatCardBudget(
    modifier: Modifier = Modifier,
    income: String,
    expense: String,
    totalTransactionsCount: Int,
    selectedMonthShort: String,
    isExpanded: Boolean,
    setExpanded: () -> Unit,
    currentMonthAllDayAndDatesListAndMaxValue: Pair<List<BarChartExpenseModel>, BigDecimal>
) {
    val palette =
        toPalette(color4)
    val cardColor = combineColors(
        MaterialTheme.colorScheme.surface,
        palette.container,
        angle = 0.7f,
    )
    val currentMontAllDayList = currentMonthAllDayAndDatesListAndMaxValue.first

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
            Column(
                Modifier
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f)) {
                Box(Modifier.clickable { setExpanded() }) {
                    Row(
                        Modifier
                            .padding(vertical = 4.dp),
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
                                Icons.Rounded.AccountBalanceWallet,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "wallet icon"
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(0.8f)) {
                            Text(
                                text = "Budget (12th Feb - 30th March)",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                style = typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
//                            Text(
//                                text = "${totalTransactionsCount} transactions",
//                                textAlign = TextAlign.Start,
//                                modifier = Modifier.fillMaxWidth(),
//                                style = typography.labelLarge,
//                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                            )
                        }
                        IconButton(
                            onClick = { setExpanded() },
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
                AnimatedVisibility(isExpanded) {
                    Column(
                        Modifier
                            .height(260.dp)
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            thickness = 0.7.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        BoxWithConstraints {
                            BarChartTest(
                                modifier = Modifier,
                                palette = toPalette(color4)
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
                    Modifier.padding(horizontal = 12.dp)
                ) {
                    SpendsBudgetCardForHomeScreen(
                        Modifier,
                        BigDecimal(1),
                        BigDecimal(0.5),
                    )
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeStatCardNew(
    modifier: Modifier = Modifier,
    income: String,
    expense: String,
    totalTransactionsCount: Int,
    selectedMonthShort: String,
    isExpanded: Boolean,
    setExpanded: () -> Unit,
    currentMonthAllDayAndDatesListAndMaxValue: Pair<List<BarChartExpenseModel>, BigDecimal>
) {
    val palette =
        toPalette(orange)
    val cardColor = combineColors(
        MaterialTheme.colorScheme.surface,
        palette.container,
        angle = 0.7f,
    )
    val currentMontAllDayList = currentMonthAllDayAndDatesListAndMaxValue.first

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
            Column(
                Modifier
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f)) {
                Box(Modifier.clickable { setExpanded() }) {
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
                                text = "${totalTransactionsCount} transactions",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                style = typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        IconButton(
                            onClick = { setExpanded() },
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
                AnimatedVisibility(isExpanded) {
                    Column(
                        Modifier
                            .height(260.dp)
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            thickness = 0.7.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        BoxWithConstraints {
                            BarChart(
                                Modifier.fillMaxSize(),
                                currentMontAllDayList,
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
                    HomeStatCardSingleNew(Modifier.weight(1f), expense, EXPENSE)
                    HomeStatCardSingleNew(Modifier.weight(1f), income, INCOME)
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
                    text = value,
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
                    text = "${LocalCurrency.current.currencySymbol} $value",
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
fun SingleTransactionNew(
    item: TransactionClass,
    onSingleItemClick: (TransactionClass) -> Unit
) {

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