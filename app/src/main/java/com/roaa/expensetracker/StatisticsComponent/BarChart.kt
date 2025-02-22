package com.roaa.expensetracker.StatisticsComponent

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel
import com.roaa.expensetracker.Utilities.calculateBarPercentageHeight
import com.roaa.expensetracker.Utilities.currentDay
import com.roaa.expensetracker.Utilities.currentMonth
import com.roaa.expensetracker.Utilities.parseAmount

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    currentSelectedMonth: String,
    maxWidth: Dp,
    maxHeight: Dp,
    currentMonthAllDayAndDates: List<BarChartExpenseModel>,
    maxExpense: Float,
    palette: HarmonizedColorPalette
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(currentSelectedMonth) {
        if (currentSelectedMonth == currentMonth) {
            val currentDate = Integer.parseInt(currentDay)
            lazyListState.animateScrollToItem(currentDate-4)
        }else{
            lazyListState.animateScrollToItem(0)
        }
    }
    LazyRow(
        state = lazyListState,
        modifier = modifier.padding(horizontal = 5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        items(currentMonthAllDayAndDates) {
            SingleBar(
                Modifier,
                it.dayName,
                maxWidth / 7,
                calculateBarPercentageHeight(170.dp, maxExpense, it.expenseAmount),
                it,
                palette
            )
        }
    }
}

@Composable
fun SingleBar(
    modifier: Modifier = Modifier,
    title: String,
    columnWidth: Dp,
    columnHeight: Dp,
    barChartExpenseModel: BarChartExpenseModel,
    palette: HarmonizedColorPalette
) {
    val context = LocalContext.current
    var showTooltip by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .width(columnWidth)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxHeight()
            .clickable {
                showTooltip = !showTooltip

                Toast.makeText(
                    context,
                    barChartExpenseModel.expenseAmount.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            },
    ) {
        val (bar, text) = createRefs()
        var isDataPresent by remember { mutableStateOf(false) }
        if (columnHeight != 0.dp) {
            isDataPresent = true
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(if (isDataPresent) columnHeight else 170.dp)
                .padding(horizontal = 5.dp)
                .background(
                    if (isDataPresent) palette.main else palette.main.copy(alpha = 0.05f),
                    RoundedCornerShape(12.dp)
                )
                .constrainAs(bar) {
                    bottom.linkTo(text.top)
                }
        )
        Column(
            modifier = Modifier
                .constrainAs(text) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(top = 4.dp)
        ) {
            Text(
                text = title.split(" ").get(1), modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = title.split(" ").get(0), modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

    }
    if (showTooltip) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { showTooltip = false },
            properties = PopupProperties(focusable = true)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "₹ ${parseAmount(barChartExpenseModel.expenseAmount)}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun BarChartPreview() {
//    BarChart(
//        Modifier.fillMaxSize(),
//        200.dp,
//        50.dp,
//        listOf("01 Mon", "02 Tue", "03 Wed", "04 Thur", "05 Fri", "06 Sat", "07 Sun"),
//        toPalette(orange)
//    )
//}

//@Preview
//@Composable
//private fun SingleBarPreview() {
//    SingleBar(Modifier, "Mon", columnWidth = 20.dp, columnHeight = 100.dp, toPalette(orange))
//}