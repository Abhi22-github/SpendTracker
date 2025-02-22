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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(modifier: Modifier = Modifier) {
    val list = List(31){"Hello"}
    LazyRow (modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center){
        items(list){
            SingleBar(Modifier, 50.dp, 200.dp)
        }
    }
}

@Composable
fun SingleBar(modifier: Modifier = Modifier, columnWidth: Dp,columnHeight:Dp) {
    Column(
        modifier = Modifier
            .width(columnWidth)
            .height(columnHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier
            .fillMaxHeight()
            .fillMaxWidth().padding(horizontal = 5.dp)
            .background(Color.Red, RoundedCornerShape(12.dp))
            .weight(0.8f)) {}
        Text(text = "Mon",modifier = Modifier
            .weight(0.2f)
            .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
private fun BarChart() {
    BarChart(Modifier.fillMaxSize())
}

@Preview
@Composable
private fun SingleBarPreview() {
    SingleBar(Modifier,columnWidth = 20.dp,columnHeight = 100.dp)
}