package com.roaa.expensetracker.Composables.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme

@Composable
fun PaymentMethodScreen(modifier: Modifier = Modifier) {
Text("Hello")
}


@Preview
@Composable
private fun PaymentMethodScreenPreview() {
    ExpenseTrackerTheme {
        PaymentMethodScreen()
    }
}