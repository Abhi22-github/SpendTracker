package com.example.expensetracker.Composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.R


@Composable
fun EmptyScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_screen_animation))

    // Box for centering content on the screen
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Display the Lottie animation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(composition = composition, modifier = Modifier.wrapContentSize())
            Text(
                text = "No Transactions found", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
fun EmptyScreenPreview() {
    ExpenseTrackerTheme {
        EmptyScreen()
    }
}