package com.roaa.expensetracker.Composables.StatisticsComponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Composables.color1
import com.roaa.expensetracker.Composables.color2
import com.roaa.expensetracker.Composables.color3
import com.roaa.expensetracker.Composables.color4
import com.roaa.expensetracker.Composables.color5
import com.roaa.expensetracker.Composables.color6
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.launch

data class ArcsData(
    val animation: Animatable<Float, AnimationVector1D>,
    val sweepAngle: Float, val color: Color
)

@Composable
fun AnimatedPieChart(modifier: Modifier = Modifier, pieDatePoints: List<PieData>) {
    val total = pieDatePoints.fold(0f) { acc, pieData ->
        acc + pieData.value
    }.div(360)
    var currentSum = 0

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
        val stroke = Stroke(width = 90f, cap = StrokeCap.Butt)
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
    val gapDegree = 15
    val numberOfGaps = pieDatePoints.size
    val remainingDegree = 360 - (gapDegree * numberOfGaps)
    val localModifier = modifier.size(200.dp)
    val total = pieDatePoints.fold(0f) { acc, pieData ->
        acc + pieData.value
    }.div(remainingDegree)
    var currentSum = 0f

    val arcs = pieDatePoints.mapIndexed {index,pieDataPoint->
        val startAngle = currentSum +(index * gapDegree)
        currentSum += pieDataPoint.value/total
        GapArcsData(
            animation = Animatable(0f),
            sweepAngle = pieDataPoint.value/total,
            color = pieDataPoint.color,
            startAngle = -90+ startAngle
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
        val stroke = Stroke(width = 70f, cap = StrokeCap.Round)
        arcs.reversed().map {
            drawArc(
                startAngle = it.startAngle,
                sweepAngle = it.animation.value,
                color = it.color,
                useCenter = false,
                style = stroke,
                blendMode = BlendMode.Multiply
            )
        }
    }
}

@Composable
fun Test(modifier: Modifier = Modifier) {

    var data by remember {
        mutableStateOf(
            listOf(
                Pie(label = "Android", data = 20.0, color = color1),
                Pie(label = "Windows", data = 45.0, color = color2),
                Pie(label = "Linux", data = 35.0, color = color3),
                Pie(label = "Android", data = 20.0, color = color4),
                Pie(label = "Windows", data = 45.0, color = color5),
                Pie(label = "Linux", data = 35.0, color = color6),
            )
        )
    }
    PieChart(
        modifier = Modifier.size(250.dp),
        data = data,
        onPieClick = {
            println("${it.label} Clicked")
            val pieIndex = data.indexOf(it)
            data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
        },
        selectedScale = 1.1f,
        scaleAnimEnterSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        spaceDegree = 3f,
        selectedPaddingDegree = 1f,
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300),
        style = Pie.Style.Stroke(50.dp)
    )
}