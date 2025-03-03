package com.roaa.expensetracker.Composables.StatisticsComponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class ArcsData(
    val animation: Animatable<Float, AnimationVector1D>,
    val sweepAngle: Float, val color: Color
)

@Composable
fun AnimatedPieChart(modifier: Modifier = Modifier, pieDatePoints: List<PieData>) {
    val localModifier = modifier.size(200.dp)
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

    Canvas(localModifier) {
        val stroke = Stroke(width = 30f)
        arcs.reversed().map {
            drawArc(
                startAngle = -90f,
                sweepAngle = it.animation.value,
                color = it.color,
                useCenter = false,
                style = stroke
            )
        }
    }
}