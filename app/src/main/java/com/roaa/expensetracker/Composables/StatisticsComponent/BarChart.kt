package com.roaa.expensetracker.Composables.StatisticsComponent

import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel
import com.roaa.expensetracker.Utilities.parseAmount
import java.text.DecimalFormat


private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    val rawLabel = context.model.extraStore[BottomAxisLabelKey][x.toInt()]
    rawLabel.split(",").get(0).replace(" ", "\n")
}

private const val Y_DIVISOR = 1000
private val YDecimalFormat = DecimalFormat("#.##K")

private val MarkerValueFormatter =
    DefaultCartesianMarker.ValueFormatter { _, targets ->
        val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
        SpannableStringBuilder()
            .append(
                YDecimalFormat.format(column.entry.y / Y_DIVISOR),
                ForegroundColorSpan(column.color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
    }

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
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries { series(currentMonthAllDayAndDates.map { it.expenseAmount }) }
            extras {
                it[BottomAxisLabelKey] =
                    currentMonthAllDayAndDates.map { it.dayName }.toList()
            }
        }
    }

    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        fill = fill(palette.main),
                        thickness = 24.dp,
                        shape = CorneredShape.rounded(topLeftPercent = 40, topRightPercent = 40)
                    )
                )
            ),
            // startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = LineComponent(fill = Fill.Transparent),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
                size = BaseAxis.Size.Auto(),
                label = TextComponent(
                    lineCount = 2,
                    textAlignment = Layout.Alignment.ALIGN_CENTER,
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                ), line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
            ),
            marker = rememberDefaultCartesianMarker(
                TextComponent(
                    color = MaterialTheme.colorScheme.onSurface.toArgb(),
                    padding = Insets(8f),
                    background = ShapeComponent(
                        fill = fill(
                            MaterialTheme.colorScheme.surfaceContainer
                        ),
                        shape = CorneredShape.rounded(40)
                    )
                )
            ),
            layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(224.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(7.0))
        //scrollState = rememberVicoScrollState(scrollEnabled = false),
    )


//    val lazyListState = rememberLazyListState()
//    LaunchedEffect(currentSelectedMonth) {
//        if (currentSelectedMonth == currentMonth) {
//            val currentDate = Integer.parseInt(currentDay)
//            lazyListState.animateScrollToItem(currentDate-4)
//        }else{
//            lazyListState.animateScrollToItem(0)
//        }
//    }
//    LazyRow(
//        state = lazyListState,
//        modifier = modifier.padding(horizontal = 5.dp),
//        verticalAlignment = Alignment.Bottom,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        items(currentMonthAllDayAndDates) {
//            SingleBar(
//                Modifier,
//                it.dayName,
//                maxWidth / 7,
//                calculateBarPercentageHeight(170.dp, maxExpense, it.expenseAmount),
//                it,
//                palette
//            )
//        }
//    }
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
                    RoundedCornerShape(25.dp)
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