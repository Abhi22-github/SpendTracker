package com.roaa.expensetracker.Composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.roaa.expensetracker.Composables.WavyShape
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Utilities.clamp
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.ceil


@Composable
fun BackgroundProgress(
    harmonizedColor: HarmonizedColorPalette,
    oldPercent: BigDecimal,
    percent: BigDecimal,
) {

    val percentWithoutNewSpentAnimated by animateFloatAsState(
        label = "percentRealAnim",
        targetValue = oldPercent.toFloat(),
        animationSpec = TweenSpec(250),
    )
    val percentWithNewSpentAnimated = animateFloatAsState(
        label = "percentWithNewSpentAnimated",
        targetValue = percent.toFloat(),
        animationSpec = TweenSpec(300),
    ).value

    val shift = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        fun anim() {
            coroutineScope.launch {
                shift.animateTo(
                    1f,
                    animationSpec = FloatTweenSpec(4000, 0, LinearEasing)
                )
                shift.snapTo(0f)
                anim()
            }
        }

        anim()
    }

    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = percent != BigDecimal.ZERO,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .drawBehind {
                        drawPath(
                            path = Path().apply {
                                val halfPeriod = 30.dp.toPx() / 2
                                val amplitude =
                                    (percentWithNewSpentAnimated.clamp(0.96f, 1f) * 2.dp).toPx()

                                moveTo(
                                    size.width - amplitude,
                                    -halfPeriod * 2.5f + halfPeriod * 2 * shift.value
                                )
                                repeat(ceil(size.height / halfPeriod + 3).toInt()) { i ->
                                    relativeQuadraticBezierTo(
                                        dx1 = 2 * amplitude * (if (i % 2 == 0) 1 else -1),
                                        dy1 = halfPeriod / 2,
                                        dx2 = 0f,
                                        dy2 = halfPeriod,
                                    )
                                }
                            },
                            color = harmonizedColor.main.copy(alpha = 0.5f),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(
                                        15f,
                                        15f
                                    )
                                )
                            )
                        )
                    }
                    .fillMaxHeight()
                    .fillMaxWidth(percentWithoutNewSpentAnimated),
            )
        }
    }
    Box(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(
                    harmonizedColor.main.copy(alpha = 0.50f),
                    shape = WavyShape(
                        period = 40.dp,
                        amplitude = percentWithNewSpentAnimated.clamp(0.96f, 1f) * 2.dp,
                        shift = shift.value,
                    ),
                )
                .fillMaxHeight()
                .fillMaxWidth(percentWithNewSpentAnimated),
        )
    }
}


