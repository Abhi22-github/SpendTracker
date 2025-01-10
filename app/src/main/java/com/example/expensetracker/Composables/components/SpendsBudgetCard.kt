package com.example.expensetracker.Composables.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.Composables.WavyShape
import com.example.expensetracker.Composables.colorBad
import com.example.expensetracker.Composables.colorGood
import com.example.expensetracker.Composables.colorNotGood
import com.example.expensetracker.Composables.numberFormat
import com.example.expensetracker.Composables.ui.StatCard
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Composables.utils.harmonize
import com.example.expensetracker.Composables.utils.toPalette
import com.example.expensetracker.R
import com.example.expensetracker.ViewModels.AddActivityViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SpendsBudgetCard(
    modifier: Modifier = Modifier,
    budget: Float,
    spend: Float,
    viewModel: AddActivityViewModel
) {
    val context = LocalContext.current
    val spend by viewModel.newTotal.observeAsState()
    
    val percent = remember { 1.minus(spend!!.div(budget)) }

    val percentFormatted = remember {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0

        formatter.format(1.minus(percent).times(100))
    }

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

    val harmonizedColor = toPalette(harmonize(
        combineColors(
            listOf(
                colorBad,
                colorNotGood,
                colorGood,
            ),
            percentWithNewSpentAnimated.coerceIn(0f,1f).toFloat(),
        )
    ))

    StatCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = harmonizedColor.container,
            contentColor = harmonizedColor.onContainer,
        ),
        value = numberFormat(
            context,
            BigDecimal(spend!!.toDouble()),
        ),
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

//@Preview(name = "The budget is almost completely spent")
//@Composable
//private fun Preview() {
//    BuckwheatTheme {
//        SpendsBudgetCard(
//            modifier = Modifier.height(IntrinsicSize.Min),
//            spend = BigDecimal(3740),
//            budget = BigDecimal(60000),
//            currency = ExtendCurrency.none(),
//
//        )
//    }
//}

//@Preview(name = "Budget half spent")
//@Composable
//private fun PreviewHalf() {
//    BuckwheatTheme {
//        SpendsBudgetCard(
//            modifier = Modifier.height(IntrinsicSize.Min),
//            spend = BigDecimal(30740),
//            budget = BigDecimal(60000),
//            currency = ExtendCurrency.none(),
//        )
//    }
//}
//
//@Preview(name = "Almost no budget")
//@Composable
//private fun PreviewFull() {
//    BuckwheatTheme {
//        SpendsBudgetCard(
//            modifier = Modifier.height(IntrinsicSize.Min),
//            spend = BigDecimal(45740),
//            budget = BigDecimal(60000),
//            currency = ExtendCurrency.none(),
//        )
//    }
//}
//
//@Preview(name = "Overspending budget")
//@Composable
//private fun PreviewOverspending() {
//    BuckwheatTheme {
//        SpendsBudgetCard(
//            modifier = Modifier.height(IntrinsicSize.Min),
//            spend = BigDecimal.ZERO,
//            budget = BigDecimal(60000),
//            currency = ExtendCurrency.none(),
//        )
//    }
//}
//
//@Preview(name = "Might mode", uiMode = UI_MODE_NIGHT_YES)
//@Composable
//private fun PreviewNightMode() {
//    BuckwheatTheme {
//        SpendsBudgetCard(
//            modifier = Modifier.height(IntrinsicSize.Min),
//            spend = BigDecimal(14740),
//            budget = BigDecimal(60000),
//            currency = ExtendCurrency.none(),
//        )
//    }
//}