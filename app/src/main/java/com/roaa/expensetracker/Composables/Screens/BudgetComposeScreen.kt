package com.roaa.expensetracker.Composables.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.components.SpendsBudgetCard
import com.roaa.expensetracker.Composables.utils.toPalette


val horizontalPadding = 16.dp
val verticalPadding = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(navigationManager: NavigationManager, modifier: Modifier = Modifier) {
    Column {
        Row(
            Modifier
                .height(150.dp)
                .padding(horizontalPadding, verticalPadding)
        ) {
            SpendsBudgetCard(Modifier, 10000f, 2000f)
        }
        SpendCalender(Modifier)
    }
}

@Composable
fun SpendCalender(modifier: Modifier = Modifier) {
    val color = toPalette(blueColor)
    Card(
        Modifier
            .padding(horizontalPadding, verticalPadding)
            .height(200.dp)
            .fillMaxWidth(),
        colors = CardColors(
            containerColor = color.container.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(Modifier.padding(horizontalPadding, verticalPadding)) {

            Text(
                text = "This table shows how much you spent every day relative to your daily budge",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "February",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                Modifier
                    .fillMaxWidth().padding(top = 10.dp)
            ) {
                for (i in 1..7) {
                    DayName(Modifier.weight(1f), "Mon")
                }
            }
        }
    }
}

@Composable
fun DayName(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SpendCalenderPreview() {
    SpendCalender(Modifier)
}