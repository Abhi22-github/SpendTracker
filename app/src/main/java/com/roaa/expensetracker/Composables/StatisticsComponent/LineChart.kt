package com.roaa.expensetracker.Composables.StatisticsComponent

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
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import java.math.BigDecimal


private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    val rawLabel = context.model.extraStore[BottomAxisLabelKey][x.toInt()]
    rawLabel.split(",").get(0).replace(" ", "\n")
}
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default()
private val RangeProvider = CartesianLayerRangeProvider.auto()

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    palette: HarmonizedColorPalette,
    listPerDay: LinkedHashMap<String, Int>,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(listPerDay) {
        modelProducer.runTransaction {
            lineSeries {
                series(listPerDay.values.map {
                    it.takeIf { it > 0 }?.let { Math.log10(it.toDouble()).toFloat() } ?: 0f
                })
            }
            extras { it[BottomAxisLabelKey] = listPerDay.keys.toList() }
        }
    }
    //  val RangeProvider = CartesianLayerRangeProvider.auto()
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            pointConnector = LineCartesianLayer.PointConnector.cubic(0.6f),
                            fill = LineCartesianLayer.LineFill.single(fill(palette.main)),
                            areaFill =
                                LineCartesianLayer.AreaFill.single(
                                    fill(
                                        ShaderProvider.verticalGradient(
                                            arrayOf(
                                                palette.main.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                ),
                        )
                    ),
                rangeProvider = RangeProvider,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = LineComponent(fill = Fill.Transparent),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
                size = BaseAxis.Size.Auto(),
                label = TextComponent(
                    lineCount = 2,
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                )
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
        ),
        modelProducer,
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(6.0)),
    )
}

@Composable
fun LineChartTest(
    modifier: Modifier = Modifier,
    palette: HarmonizedColorPalette,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(0, 122, 4, 5, 6, 78, 9, 12, 12, 314, 15, 5, 6, 7)
            }
            // extras { it[BottomAxisLabelKey] = listPerDay.keys.toList() }
        }
    }
    //  val RangeProvider = CartesianLayerRangeProvider.auto()
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            pointConnector = LineCartesianLayer.PointConnector.cubic(0.6f),
                            fill = LineCartesianLayer.LineFill.single(fill(palette.main)),
                            areaFill =
                                LineCartesianLayer.AreaFill.single(
                                    fill(
                                        ShaderProvider.verticalGradient(
                                            arrayOf(
                                                palette.main.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                ),
                        )
                    ),
                rangeProvider = RangeProvider,
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = LineComponent(fill = Fill.Transparent),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                //  valueFormatter = BottomAxisValueFormatter,
                size = BaseAxis.Size.Auto(),
                label = TextComponent(
                    lineCount = 2,
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                )
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
        ),
        modelProducer,
        modifier = modifier
            .height(300.dp)
            .fillMaxWidth(),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(6.0)),
    )
}

@Composable
private fun rememberHorizontalLine(
    budgetAmount: BigDecimal,
    palette: HarmonizedColorPalette
): HorizontalLine {
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
            label = { "Total Budget ₹$budgetAmount" },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}

@Composable
fun LineChartBudgetTotalUsage(
    modifier: Modifier = Modifier,
    palette: HarmonizedColorPalette,
    lineChartDataList: Map<String, BigDecimal>,
    totalBudgetAmount: BigDecimal,

    ) {
    val maxY = maxOf(
        totalBudgetAmount.multiply(BigDecimal(1.2)), // Add 20% buffer above budget
        lineChartDataList.values.max() ?: BigDecimal.ZERO
    ).toDouble()

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(lineChartDataList.values)
            }
            extras { it[BottomAxisLabelKey] = lineChartDataList.keys.toList() }
        }
    }
    val RangeProvider = CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = maxY)
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.Point(
                                    sizeDp = 12f, component = ShapeComponent(
                                        shape = CorneredShape.rounded(50),
                                        fill = fill(Color.Transparent),
                                        strokeFill = fill(palette.main),
                                        strokeThicknessDp = 5f
                                    )
                                )
                            ),
                            pointConnector = LineCartesianLayer.PointConnector.cubic(0.9f),
                            fill = LineCartesianLayer.LineFill.single(fill(palette.container)),
                            areaFill =
                                LineCartesianLayer.AreaFill.single(
                                    fill(
                                        ShaderProvider.verticalGradient(
                                            arrayOf(
                                                palette.main.copy(alpha = 0.4f),
                                                palette.main.copy(alpha = 0.2f),
                                                palette.main.copy(alpha = 0.1f),
                                            )
                                        )
                                    )
                                ),
                        )
                    ),
                rangeProvider = RangeProvider,
            ),
            startAxis = VerticalAxis.rememberStart(
                guideline = LineComponent(fill = fill(Color.Transparent)),
                itemPlacer = remember { VerticalAxis.ItemPlacer.count({ 5 }) },
                line = LineComponent(fill = fill(Color.Transparent)),
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = LineComponent(fill = Fill.Transparent),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.aligned() },
                valueFormatter = BottomAxisValueFormatter,
                label = TextComponent(
                    lineCount = 2,
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                )
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
                )
            ),
            decorations = listOf(rememberHorizontalLine(totalBudgetAmount, palette))
        ),
        modelProducer,
        modifier = modifier
            .height(300.dp)
            .fillMaxWidth(),
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(6.0)),
    )
}