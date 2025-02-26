package com.roaa.expensetracker.Composables.StatisticsComponent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette


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
                                    arrayOf(palette.main.copy(alpha = 0.4f), Color.Transparent)
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
                label = TextComponent(lineCount = 2)
            ),
        ),
        modelProducer,
        zoomState = rememberVicoZoomState(zoomEnabled = true, initialZoom = Zoom.x(6.0)),
    )
}