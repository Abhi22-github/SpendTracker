package com.roaa.expensetracker.composable.statisticsComponent

import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.utils.HarmonizedColorPalette
import com.roaa.expensetracker.model.uiDataModels.BarChartExpenseModel
import com.roaa.expensetracker.utilities.parseAmount
import com.roaa.expensetracker.utilities.toLong
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate


private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    val rawLabel = context.model.extraStore[BottomAxisLabelKey][x.toInt()]
    rawLabel.split(",").get(0).replace(" ", "\n")
}


@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    currentMonthAllDayAndDates: List<BarChartExpenseModel>,
    palette: HarmonizedColorPalette
) {
    val localCurrency = LocalCurrency.current.currencySymbol
    val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter { context, targets ->
        val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
        SpannableStringBuilder()
            .append(
                "$localCurrency${parseAmount(column.entry.y.toBigDecimal())}",
                ForegroundColorSpan(column.color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(currentMonthAllDayAndDates) {
        modelProducer.runTransaction {
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
                            shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
                        )
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = LineComponent(fill = Fill.Transparent),
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    valueFormatter = BottomAxisValueFormatter,
                    size = BaseAxis.Size.Auto(),
                    label = TextComponent(
                        lineCount = 2,
                        textAlignment = Layout.Alignment.ALIGN_CENTER,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    ),
                    line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
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
                    ),
                    valueFormatter = MarkerValueFormatter,
                ),
                layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
            ),
        modelProducer = modelProducer,
        modifier = modifier.height(224.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(7.0)),
        scrollState = rememberVicoScrollState(
            scrollEnabled = true,
            initialScroll = Scroll.Absolute.x(
                LocalDate.now().toLong().toString().takeLast(2).toDouble() - 1, 0.5f
            )
        ),
    )
}

@Composable
fun BarChartTest(
    modifier: Modifier = Modifier,
    palette: HarmonizedColorPalette
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries { series(1, 2, 3, 5, 6, 7, 8, 34, 2, 4, 6, 8, 9, 13, 1, 4, 5, 7) }
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
                            shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    itemPlacer = VerticalAxis.ItemPlacer.count({ 3 }), label = TextComponent(
                        lineCount = 2,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = LineComponent(
                        fill = fill(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                        shape = dashedShape(gapLength = 2.dp)
                    ),
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    size = BaseAxis.Size.Auto(),
                    label = TextComponent(
                        lineCount = 2,
                        textAlignment = Layout.Alignment.ALIGN_CENTER,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    ),
                    line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
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
        modifier = modifier.height(300.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(15.0))
        //scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}


@Composable
fun BarChartStatisticsScreen(
    modifier: Modifier = Modifier,
    currentMonthAllDayAndDates: List<BarChartExpenseModel>,
    palette: HarmonizedColorPalette,
    selectedIndex: Int
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(selectedIndex) {
        modelProducer.runTransaction {
            columnSeries { series(currentMonthAllDayAndDates.map { if (selectedIndex == 0) it.expenseAmount else it.incomeAmount }) }
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
                            fill = fill(if (selectedIndex == 0) palette.main else MaterialTheme.colorScheme.primary),
                            thickness = 24.dp,
                            shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    itemPlacer = VerticalAxis.ItemPlacer.count({ 3 }), line = LineComponent(
                        fill(Color.Transparent)
                    ),
                    label = TextComponent(
                        lineCount = 2,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = LineComponent(
                        fill = fill(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                        shape = dashedShape(gapLength = 2.dp)
                    ),
                    valueFormatter = BottomAxisValueFormatter,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    size = BaseAxis.Size.Auto(),
                    label = TextComponent(
                        lineCount = 2,
                        textAlignment = Layout.Alignment.ALIGN_CENTER,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    ),
                    line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
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
        modifier = modifier.height(300.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(9.0))
        //scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}


@Composable
private fun rememberHorizontalLine(
    budgetAmount: BigDecimal,
    palette: HarmonizedColorPalette
): HorizontalLine {
    val currency = LocalCurrency.current.currencySymbol
    val fill = fill(palette.main.copy(alpha = 0.1f))
    val line = rememberLineComponent(fill = fill(palette.main), thickness = 2.dp)
    val labelComponent =
        rememberTextComponent(
            color = palette.onMain,
            margins = insets(start = 6.dp),
            padding = insets(start = 8.dp, end = 8.dp, bottom = 2.dp),
            background =
                shapeComponent(fill, CorneredShape.rounded(bottomLeft = 4.dp, bottomRight = 4.dp)),
        )
    return remember {
        HorizontalLine(
            y = { budgetAmount.toDouble() },
            line = line,
            labelComponent = labelComponent,
            label = { "Daily Budget ${currency}$budgetAmount" },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}

@Composable
fun BarChartBudgetUsage(
    modifier: Modifier = Modifier,
    palette: HarmonizedColorPalette,
    chartDataList: List<BarChartExpenseModel>,
    budgetAmount: BigDecimal,
    dayDifferenceForCharts: Long
) {
    val localCurrency = LocalCurrency.current.currencySymbol
    val MarkerValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(DecimalFormat("$localCurrency#,##,##0.00"))

    val maxY = maxOf(
        budgetAmount.multiply(BigDecimal(1.2)), // Add 20% buffer above budget
        chartDataList.maxOfOrNull { it.expenseAmount } ?: BigDecimal.ZERO
    ).toDouble()

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(chartDataList,dayDifferenceForCharts) {
        modelProducer.runTransaction {
            columnSeries { series(chartDataList.map { it.expenseAmount }) }
            extras {
                it[BottomAxisLabelKey] =
                    chartDataList.map { it.dayName }.toList()
            }
        }
    }

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            fill = fill(palette.container),
                            thickness = 24.dp,
                            shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
                        )
                    ),
                    rangeProvider = remember {
                        CartesianLayerRangeProvider.fixed(
                            minY = 0.0,
                            maxY = maxY
                        )
                    }
                ),
                startAxis = VerticalAxis.rememberStart(
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                    itemPlacer = remember { VerticalAxis.ItemPlacer.count({ 3 }) },
                    size = BaseAxis.Size.Auto(),
                    line = LineComponent(fill = fill(Color.Transparent)),
                    label = TextComponent(
                        lineCount = 2,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = LineComponent(
                        fill = fill(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                        shape = dashedShape(gapLength = 2.dp)
                    ),
                    valueFormatter = BottomAxisValueFormatter,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    size = BaseAxis.Size.Auto(),
                    label = TextComponent(
                        lineCount = 2,
                        textAlignment = Layout.Alignment.ALIGN_CENTER,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    ),
                    line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                ),
                marker = rememberDefaultCartesianMarker(
                    labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
                    label = TextComponent(
                        color = palette.onMain.toArgb(),
                        padding = Insets(8f),
                        background = ShapeComponent(
                            fill = fill(
                                palette.main
                            ),
                            shape = CorneredShape.rounded(40)
                        )
                    ),
                    valueFormatter = MarkerValueFormatter
                ),
                layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
                decorations = listOf(rememberHorizontalLine(budgetAmount, palette))
            ),
        modelProducer = modelProducer,
        modifier = modifier
            .height(300.dp)
            .fillMaxWidth(),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(7.0)),
        scrollState = rememberVicoScrollState(
            scrollEnabled = true,
            initialScroll = Scroll.Absolute.x(dayDifferenceForCharts.toDouble(), 0.5f)
        ),
    )
}

@Composable
fun BarChartStatisticsScreenBanks(
    modifier: Modifier = Modifier,
    currentMonthAllDayAndDates: List<BarChartExpenseModel>,
    palette: HarmonizedColorPalette,
    selectedIndex: Int
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(selectedIndex) {
        modelProducer.runTransaction {
            columnSeries { series(currentMonthAllDayAndDates.map { if (selectedIndex == 0) it.expenseAmount else it.incomeAmount }) }
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
                            fill = fill(if (selectedIndex == 0) palette.main else MaterialTheme.colorScheme.primary),
                            thickness = 24.dp,
                            shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    itemPlacer = VerticalAxis.ItemPlacer.count({ 3 }), line = LineComponent(
                        fill(Color.Transparent)
                    ), label = TextComponent(
                        lineCount = 2,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = LineComponent(
                        fill = fill(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                        shape = dashedShape(gapLength = 2.dp)
                    ),
                    valueFormatter = BottomAxisValueFormatter,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    size = BaseAxis.Size.Auto(),
                    label = TextComponent(
                        lineCount = 2,
                        textAlignment = Layout.Alignment.ALIGN_CENTER,
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                    ),
                    line = LineComponent(fill = fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
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
        modifier = modifier.height(300.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(9.0))
        //scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}