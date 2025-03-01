package com.roaa.expensetracker.Composables.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.WavyShape
import com.roaa.expensetracker.Composables.colorBad
import com.roaa.expensetracker.Composables.colorGood
import com.roaa.expensetracker.Composables.colorNotGood
import com.roaa.expensetracker.Composables.numberFormat
import com.roaa.expensetracker.Composables.ui.StatCard
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.harmonize
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.R
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun SpendsBudgetCard(
    modifier: Modifier = Modifier,
    budget: Float,
    spend: Float,
) {
    val context = LocalContext.current
    val percent = 1.minus(spend.div(budget))

    val bigDecimal = BigDecimal((1 - percent.toDouble()) * 100).setScale(2, RoundingMode.HALF_UP)
    val percentFormatted = bigDecimal.toFloat()

//    val percentFormatted =  remember{
//        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
//        formatter.maximumFractionDigits = 2
//        formatter.minimumFractionDigits = 0
//
//        formatter.format(percent.times(100))
//    }

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

    val percentWithNewSpentAnimated = animateFloatAsState(
        label = "percentWithNewSpentAnimated",
        targetValue = percent.toFloat(),
        animationSpec = TweenSpec(300),
    ).value

    val harmonizedColor = toPalette(
        harmonize(
            combineColors(
                listOf(
                    colorBad,
                    colorNotGood,
                    colorGood,
                ),
                percentWithNewSpentAnimated.coerceIn(0f, 1f).toFloat(),
            )
        )
    )

    StatCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = harmonizedColor.container,
            contentColor = harmonizedColor.onContainer,
        ),
        value = numberFormat(
            context,
            BigDecimal(spend.toDouble()),
        ),
        budget = budget.toString(),
        label = stringResource(R.string.spent_budget),
        content = {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.rest_budget_percent, percentFormatted),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        backdropContent = {
            Box(
                modifier = Modifier
                    .background(
                        harmonizedColor.main,
                        shape = WavyShape(
                            period = 30.dp,
                            amplitude = 2.dp,
                            shift = shift.value,
                        ),
                    )
                    .fillMaxHeight()
                    .fillMaxWidth(percent),
            )
        }
    )
}

@Preview(name = "The budget is almost completely spent")
@Composable
private fun Preview() {
    ExpenseTrackerTheme {
        SpendsBudgetCard(
            modifier = Modifier.height(IntrinsicSize.Min),
            spend = 3740f,
            budget = 60000f,
        )
    }
}

@Preview(name = "Budget half spent")
@Composable
private fun PreviewHalf() {
    ExpenseTrackerTheme {
        SpendsBudgetCard(
            modifier = Modifier.height(IntrinsicSize.Min),
            spend = 30740f,
            budget = 60000f,
        )
    }
}

@Preview(name = "Almost no budget")
@Composable
private fun PreviewFull() {
    ExpenseTrackerTheme {
        SpendsBudgetCard(
            modifier = Modifier.height(IntrinsicSize.Min),
            spend = 45740f,
            budget = 60000f,
        )
    }
}

@Preview(name = "Overspending budget")
@Composable
private fun PreviewOverspending() {
    ExpenseTrackerTheme {
        SpendsBudgetCard(
            modifier = Modifier.height(IntrinsicSize.Min),
            spend = 0f,
            budget = 60000f,
        )
    }
}

@Preview(name = "Might mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNightMode() {
    ExpenseTrackerTheme {
        SpendsBudgetCard(
            modifier = Modifier.height(IntrinsicSize.Min),
            spend = 14740f,
            budget = 60000f,
        )
    }
}