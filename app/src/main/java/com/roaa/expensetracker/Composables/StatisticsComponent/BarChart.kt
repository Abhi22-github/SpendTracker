package com.roaa.expensetracker.Composables.StatisticsComponent

import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
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
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
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
    currentMonthAllDayAndDates: List<BarChartExpenseModel>,
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
                        shape = CorneredShape.rounded(topLeftPercent = 60, topRightPercent = 60)
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
        modifier = modifier.height(224.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(7.0))
        //scrollState = rememberVicoScrollState(scrollEnabled = false),
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
            startAxis = VerticalAxis.rememberStart(itemPlacer = VerticalAxis.ItemPlacer.count({ 3 })),
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
            startAxis = VerticalAxis.rememberStart(itemPlacer = VerticalAxis.ItemPlacer.count({ 3 }), line = LineComponent(
                fill(Color.Transparent)
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
            startAxis = VerticalAxis.rememberStart(itemPlacer = VerticalAxis.ItemPlacer.count({ 3 }), line = LineComponent(
                fill(Color.Transparent)
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