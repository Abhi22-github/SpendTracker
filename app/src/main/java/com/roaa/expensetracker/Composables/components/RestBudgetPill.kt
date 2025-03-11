package com.roaa.expensetracker.Composables.components

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Activity.LocalCurrency
import com.roaa.expensetracker.Composables.colorBad
import com.roaa.expensetracker.Composables.colorEditor
import com.roaa.expensetracker.Composables.colorGood
import com.roaa.expensetracker.Composables.colorNotGood
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.harmonize
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.ViewModels.DaileBudgetState
import java.math.BigDecimal

@Composable
fun RowScope.RestBudgetPill(
    totalExpenseAmountForDate: BigDecimal,
    totalAmountPerDay: BigDecimal,
    oldPercent: BigDecimal,
    percent: BigDecimal,
) {
    val percentWithNewSpentAnimated = animateFloatAsState(
        label = "percentWithNewSpentAnimated",
        targetValue = percent.toFloat(),
        animationSpec = TweenSpec(300),
    ).value
    val currency = LocalCurrency.current.currencySymbol

    val harmonizedColor = toPalette(
        harmonize(
            combineColors(
                listOf(
                    colorBad,
                    colorNotGood,
                    colorGood,
                ),
                percentWithNewSpentAnimated.coerceIn(0f, 1f),
            ),
            colorEditor
        )
    )
    Card(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = harmonizedColor.container,
            contentColor = harmonizedColor.onContainer,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            BackgroundProgress(harmonizedColor = harmonizedColor, oldPercent, percent)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithLayer {
                        drawContent()
                        val leftOffset = size.width - 20.dp.toPx()
                        drawRect(
                            topLeft = Offset(leftOffset, 0f),
                            size = Size(
                                20.dp.toPx(),
                                size.height,
                            ),
                            blendMode = BlendMode.SrcIn,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Black.copy(alpha = 0f),
                                ),
                                startX = leftOffset,
                                endX = leftOffset + 14.dp.toPx()
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                StatusLabel(harmonizedColor, budgetState = DaileBudgetState.END)
                Spacer(modifier = Modifier.weight(1f))
                AnimatedNumber(
                    value = currency + totalExpenseAmountForDate.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    ),
                )
                Spacer(modifier = Modifier.padding(end = 10.dp))
            }
        }
    }
}

fun ContentDrawScope.drawWithLayer(block: ContentDrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

fun Modifier.drawWithLayer(block: ContentDrawScope.() -> Unit) = this.then(
    Modifier.drawWithContent {
        drawWithLayer {
            block()
        }
    }
)