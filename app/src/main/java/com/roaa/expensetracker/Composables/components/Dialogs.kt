package com.roaa.expensetracker.Composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.R


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

@Composable
fun ConfirmationAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun AlertDialogPreview() {
    ExpenseTrackerTheme {
        Surface {
            ConfirmationAlertDialog(
                {},
                {},
                "Change Budget",
                "Are you sure, you want to change the current budget?",
                ImageVector.vectorResource(R.drawable.icon_expense)
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