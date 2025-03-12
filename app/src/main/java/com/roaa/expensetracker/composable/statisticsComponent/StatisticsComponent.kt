package com.roaa.expensetracker.composable.statisticsComponent

import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.color1
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.utilities.colorList
import com.roaa.expensetracker.utilities.utilityModalClass.CategorySummaryClass
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.launch

data class ArcsData(
    val animation: Animatable<Float, AnimationVector1D>,
    val sweepAngle: Float, val color: Color
)

@Composable
fun AnimatedPieChart(modifier: Modifier = Modifier, pieDatePoints: List<PieData>) {
    if (pieDatePoints.isEmpty())
        return
    val total = pieDatePoints.fold(0f) { acc, pieData ->
        acc + pieData.value
    }.div(360)
    var currentSum = 0f

    val arcs = pieDatePoints.map {
        currentSum += it.value
        ArcsData(
            animation = Animatable(0f),
            sweepAngle = currentSum.div(total),
            color = it.color
        )
    }

    LaunchedEffect(arcs) {
        arcs.map {
            launch {
                it.animation.animateTo(
                    targetValue = it.sweepAngle,
                    animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Canvas(modifier) {
        val stroke = Stroke(width = 90f, cap = StrokeCap.Round)
        arcs.reversed().map {
            drawArc(
                startAngle = -90f,
                sweepAngle = it.animation.value,
                color = it.color,
                useCenter = false,
                style = stroke,
                blendMode = BlendMode.Src
            )
        }
    }
}

data class GapArcsData(
    val animation: Animatable<Float, AnimationVector1D>,
    val sweepAngle: Float, val color: Color, val startAngle: Float
)

@Composable
fun AnimatedGapPieChart(modifier: Modifier = Modifier, pieDatePoints: List<PieData>) {
    if (pieDatePoints.isEmpty())
        return
    val gapDegree = 0
    val numberOfGaps = pieDatePoints.size
    val remainingDegree = 360 - (gapDegree * numberOfGaps)
    val localModifier = modifier.size(200.dp)
    val total = pieDatePoints.fold(0f) { acc, pieData ->
        acc + pieData.value
    }.div(remainingDegree)
    var currentSum = 0f

    val arcs = pieDatePoints.mapIndexed { index, pieDataPoint ->
        val startAngle = currentSum + (index * gapDegree)
        currentSum += pieDataPoint.value / total
        GapArcsData(
            animation = Animatable(0f),
            sweepAngle = pieDataPoint.value / total,
            color = pieDataPoint.color,
            startAngle = -90 + startAngle
        )
    }

    LaunchedEffect(arcs) {
        arcs.map {
            launch {
                it.animation.animateTo(
                    targetValue = it.sweepAngle,
                    animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Canvas(localModifier) {
        val stroke = Stroke(width = 30f, cap = StrokeCap.Round)
        arcs.reversed().map {
            drawArc(
                startAngle = it.startAngle,
                sweepAngle = it.animation.value,
                color = it.color,
                useCenter = false,
                style = stroke,
                //blendMode = BlendMode.Multiply
            )
        }
    }
}

fun generateDynamicColors(size: Int): List<Color> {
    return List(size) {
        val hsv = floatArrayOf((it * 360f / size) % 360, 0.7f, 1f) // Hue, Saturation, Value
        Color(android.graphics.Color.HSVToColor(hsv)) // Convert to Compose Color
    }
}

@Composable
fun Test(
    modifier: Modifier = Modifier,
    categoryListData: Map<CategoryClass, CategorySummaryClass>,
    onSurface: Color,
    surface: Color
) {


    var data by remember {
        mutableStateOf(
            categoryListData.map { (categoryClass, categorySummaryClass) ->
                Pie(
                    categoryClass.categoryName,
                    categorySummaryClass.totalAmount.toDouble(),
                    color1
                )
            }
        )
    }
    LaunchedEffect(categoryListData) {
        data = categoryListData.map { (categoryClass, categorySummaryClass) ->
            Pie(
                categoryClass.categoryName,
                categorySummaryClass.totalAmount.toDouble(),
                categorySummaryClass.color
            )
        }
    }
    val pieData = categoryListData.mapKeys {
        it.key.categoryName
    }.mapValues {
        it.value.percentage
    }
    val pieEntries = pieData.map { PieEntry(it.value.toFloat(), it.key) }

    val pieDataSet = PieDataSet(pieEntries, "Categories").apply {
        colors = colorList.map { it.toArgb() }
        valueTextColor = MaterialTheme.colors.onSurface.toArgb()
        valueTextSize = 14f
        setDrawValues(true)
    }


    Box(Modifier.size(350.dp)) {
        PieChartView(pieDataSet = pieDataSet,onSurface.toArgb())
    }

//    PieChart(
//        modifier = Modifier.size(250.dp),
//        data = data,
//        onPieClick = {
//            println("${it.label} Clicked")
//            val pieIndex = data.indexOf(it)
//            data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
//        },
//        selectedScale = 1.1f,
//        scaleAnimEnterSpec = spring<Float>(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        ),
//        spaceDegree = 3f,
//        selectedPaddingDegree = 1f,
//        colorAnimEnterSpec = tween(300),
//        colorAnimExitSpec = tween(300),
//        scaleAnimExitSpec = tween(300),
//        spaceDegreeAnimExitSpec = tween(300),
//        style = Pie.Style.Stroke(50.dp)
//    )
}

@Composable
fun PieChartView(pieDataSet: PieDataSet, onSurface: Int) {
    AndroidView(factory = { context ->
        com.github.mikephil.charting.charts.PieChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setUsePercentValues(true)
            description.isEnabled = false
            setDrawHoleEnabled(true)
            setDrawRoundedSlices(false)
            holeRadius = 50f
            setHoleColor(Color.Transparent.toArgb())
            setEntryLabelTextSize(14f)
            setEntryLabelColor(onSurface)
            legend.isEnabled = false
            setTransparentCircleRadius(60f)
            animateY(1000)
            setTransparentCircleColor(R.color.blue)

            val pieData = com.github.mikephil.charting.data.PieData(pieDataSet)
            this.data = pieData
            invalidate()
        }
    })
}

@Composable
fun DonutChartSample(pieDataList: List<PieChartData>) {

    DonutChart(
        modifier = Modifier.fillMaxSize(0.8f),
        pieChartData = pieDataList,
        centerTitle = "Category",
        centerTitleStyle = TextStyle(color = Color(0xFF071952)),
        outerCircularColor = Color.Transparent,
        innerCircularColor = Color.Transparent,
        ratioLineColor = Color.LightGray,
        textRatioStyle = TextStyle.Default.copy(fontSize = 12.sp),
        legendPosition = LegendPosition.DISAPPEAR
    )
}