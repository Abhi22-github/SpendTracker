package com.roaa.expensetracker.StatisticsComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Composables.utils.toPalette

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    maxWidth: Dp,
    maxHeight: Dp,
    currentMonthAllDayAndDates: List<String>,
    palette: HarmonizedColorPalette
) {
    val list = List(31) { "Hello" }
    LazyRow(
        modifier.padding(horizontal = 5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        items(currentMonthAllDayAndDates) {
            SingleBar(Modifier, it, 50.dp, 200.dp,palette)
        }
    }
}

@Composable
fun SingleBar(modifier: Modifier = Modifier, title: String, columnWidth: Dp, columnHeight: Dp,palette: HarmonizedColorPalette) {
    Column(
        modifier = Modifier
            .width(columnWidth)
            .height(columnHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .background(palette.main, RoundedCornerShape(12.dp))
                .weight(0.8f)
        ) {}
        Column(modifier = Modifier.weight(0.2f).padding(top = 4.dp)) {
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
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    BarChart(
        Modifier.fillMaxSize(),
        200.dp,
        50.dp,
        listOf("01 Mon", "02 Tue", "03 Wed", "04 Thur", "05 Fri", "06 Sat", "07 Sun"),
        toPalette(orange)
    )
}

@Preview
@Composable
private fun SingleBarPreview() {
    SingleBar(Modifier, "Mon", columnWidth = 20.dp, columnHeight = 100.dp,toPalette(orange))
}