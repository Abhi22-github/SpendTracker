package com.roaa.expensetracker.composable.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.CustomFonts
import com.roaa.expensetracker.composable.greenColor
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.utilities.Constants.MAX_PAGES
import com.roaa.expensetracker.utilities.calculateEffectivePage
import com.roaa.expensetracker.utilities.convertTotalExpenseIncomeClassToMap
import com.roaa.expensetracker.utilities.getCalendarForMonthFromDate
import com.roaa.expensetracker.utilities.getFirstAndLastDayOfGivenMonthDate
import com.roaa.expensetracker.utilities.getMonthFromLocalDate
import com.roaa.expensetracker.utilities.parseAmount
import com.roaa.expensetracker.utilities.toLong
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun MonthViewScreen(
    navController: NavController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = MAX_PAGES / 2, pageCount = { MAX_PAGES })
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val configuration = LocalConfiguration.current
    val firstDayOfWeek = WeekFields.of(configuration.locale).firstDayOfWeek
    var sendUserToDayView by remember { mutableStateOf(false) }
    val month by viewModel.uiViewModel.selectedMonth.collectAsState()

    var monthChipFlag by remember { mutableStateOf(true) }
    var pagerFlag by remember { mutableStateOf(false) }

    LaunchedEffect(month, monthChipFlag) {
        Log.d("month", month)
        Log.d("month", "${calculateEffectivePage(month).toInt()}")
        pagerState.animateScrollToPage(250 + calculateEffectivePage(month).toInt())
        pagerFlag = true
        monthChipFlag = false
    }
    LaunchedEffect(pagerState.targetPage, pagerFlag) {
        scope.launch {
            viewModel.uiViewModel.selectedMonth.emit(
                getMonthFromLocalDate(
                    calculateMonthStartDate(
                        pagerState.targetPage,
                        MAX_PAGES / 2
                    )
                )
            )
        }
        pagerFlag = false
        monthChipFlag = true
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
    ) { page ->

        val currentMonthStart = calculateMonthStartDate(page, MAX_PAGES / 2)
        val currentMonthFirstAndLastDate = getFirstAndLastDayOfGivenMonthDate(currentMonthStart)
        val allDays = remember(currentMonthStart) {
            getCalendarForMonthFromDate(currentMonthStart)
        }
        val totalExpenseList by viewModel.transactionsViewModel.getListOfTotalAmountPerDayForRangeForCompose(
            allDays[0].toLong(),
            allDays[41].toLong()
        ).collectAsState(listOf())
        var totalExpense by remember { mutableStateOf(BigDecimal.ZERO) }
        var totalIncome by remember { mutableStateOf(BigDecimal.ZERO) }
        LaunchedEffect(totalExpenseList) {
            totalExpenseList.forEach {
                if (it.date >= currentMonthFirstAndLastDate.first && it.date <= currentMonthFirstAndLastDate.second) {
                    totalExpense += it.totalExpense
                    totalIncome += it.totalIncome
                }
            }
        }
        val totalValuesPerDayForMonthMap = remember(totalExpenseList) {
            convertTotalExpenseIncomeClassToMap(totalExpenseList)
        }
        Column {
            HorizontalDivider(
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                modifier = Modifier
                    .padding()
            )
            Row(Modifier) {
                MonthStatCard(Modifier.weight(1f), parseAmount(totalExpense), "Total Expense")
                MonthStatCard(Modifier.weight(1f), parseAmount(totalIncome), "Total Income")
            }
            HorizontalDivider(
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            MonthView(
                modifier = Modifier,
                monthStart = currentMonthStart,
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    sendUserToDayView = !sendUserToDayView
                },
                firstDayOfWeek = firstDayOfWeek,
                allDays,
                totalValuesPerDayForMonthMap
            )
        }
    }
    LaunchedEffect(sendUserToDayView, selectedDate) {
        if (sendUserToDayView && selectedDate != null) {
            navController.navigate(
                Destinations.DayScreen(
                    selectedDate!!.toLong()
                )
            )
        }
    }
}

private fun calculateMonthStartDate(page: Int, initialPage: Int): LocalDate {
    val initialDate = LocalDate.now().withDayOfMonth(1)
    val monthsOffset = (page - initialPage).toLong()
    return initialDate.plusMonths(monthsOffset)
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MonthView(
    modifier: Modifier,
    monthStart: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    firstDayOfWeek: DayOfWeek,
    allDays: List<LocalDate>,
    totalValuesPerDayForMonthMap: HashMap<Long, Pair<BigDecimal, BigDecimal>>,
) {
    Column(modifier = modifier) {
        // Month header
//        Text(
//            text = monthStart.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(8.dp)
//        )

        // Day names row
        Row(modifier = Modifier.fillMaxWidth()) {
            (0..6).forEach { offset ->
                val dayOfWeek = firstDayOfWeek.plus(offset.toLong())
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = typography.labelMedium
                )
            }
        }
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Dates grid
            val itemHeight = maxHeight / 6
            LazyVerticalGrid(
                columns = GridCells.Fixed(7), modifier = Modifier.fillMaxHeight(1f)
            ) {
                items(allDays.size) { index ->
                    val date = allDays[index]
                    var position by remember { mutableStateOf(Position.FIRSTROW) }
                    if (index <= 6) {
                        position = Position.FIRSTROW
                    } else if (index == 7 || index == 14 || index == 21 || index == 28 || index == 35) {
                        position = Position.FIRSTCOLUMN
                    } else {
                        position = Position.REST
                    }
                    if (date != null) {
                        DayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            isCurrentMonth = date.month == monthStart.month,
                            onDateSelected = onDateSelected,
                            singleCellHeight = itemHeight,
                            position = position,
                            totalValuesPerDayForMonthMap[date.toLong()]
                        )
                    } else {
                        //empty
                        Spacer(modifier = Modifier.aspectRatio(1f))
                    }
                }
            }
        }
    }
}

enum class Position { FIRSTROW, FIRSTCOLUMN, REST; }

@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    singleCellHeight: Dp,
    position: Position,
    pair: Pair<BigDecimal, BigDecimal>?
) {
    val isToday = date == LocalDate.now()
    val textColor = when {
        isSelected -> Color.White
        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    val strokeColor = MaterialTheme.colorScheme.surfaceContainer
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(singleCellHeight)
            .drawBehind {
                // Draw borders selectively
                val borderWidth = 1.dp.toPx()
                val paint = Paint().apply {
                    color = strokeColor
                    strokeWidth = borderWidth
                    isAntiAlias = true
                }
                drawIntoCanvas { canvas ->
                    if (Position.REST == position) {
                        canvas.drawLine(
                            Offset(size.width, 0f), Offset(size.width, size.height), paint
                        )
                        canvas.drawLine(
                            Offset(0f, size.height), Offset(size.width, size.height), paint
                        )
                    }

                    if (Position.FIRSTROW == position) {
                        canvas.drawLine(
                            Offset(size.width, 0f), Offset(size.width, size.height), paint
                        )
                        canvas.drawLine(
                            Offset(0f, size.height), Offset(size.width, size.height), paint
                        )
                        canvas.drawLine(
                            Offset(0f, 0f), Offset(size.width, 0f), paint
                        )
                    }

                    if (Position.FIRSTCOLUMN == position) {
                        canvas.drawLine(
                            Offset(size.width, 0f), Offset(size.width, size.height), paint
                        )
                        canvas.drawLine(
                            Offset(0f, size.height), Offset(size.width, size.height), paint
                        )
                        canvas.drawLine(
                            Offset(0f, 0f), Offset(0f, size.height), paint
                        )
                    }
                }
            }
            .clickable { onDateSelected(date) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp, 10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(24.dp)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = if (isToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = textColor,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        style = typography.labelMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            pair?.let {
                val colorPalletOrange = toPalette(orange)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .background(
                            colorPalletOrange.container, shape = RoundedCornerShape(3.dp)
                        )

                ) {
                    Text(
                        text = "-${LocalCurrency.current.currencySymbol}${parseAmount(it.first)}",
                        color = colorPalletOrange.main,
                        style = typography.labelSmall.copy(fontFamily = CustomFonts.numberFont),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp, 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )
                }

                val colorPalletGreen = toPalette(greenColor)
                Box(
                    Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .background(
                            colorPalletGreen.container, shape = RoundedCornerShape(3.dp)
                        )

                ) {
                    Text(
                        text = "+${LocalCurrency.current.currencySymbol}${parseAmount(it.second)}",
                        color = colorPalletGreen.main,
                        style = typography.labelSmall.copy(fontFamily = CustomFonts.numberFont),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(3.dp, 2.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            // Add event indicators/dots here if needed
        }
    }
}

@Composable
fun MonthStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
) {

    ConstraintLayout(
        modifier = modifier
    ) {
        val (content, decoration1) = createRefs()
        Column(
            modifier = Modifier
                .padding(0.dp, 8.dp)
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
                    fontSize = 18.sp
                ),
            )
            Text(
                text = label,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }

}

@Preview
@Composable
private fun MonthStatCardPreview() {
    MonthStatCard(Modifier, "3230", "Total Expense")
}

