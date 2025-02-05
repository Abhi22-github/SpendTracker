package com.roaa.expensetracker.Composables.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.R

@Composable
fun TransactionDetailsScreen(navigationManager: NavigationManager, modifier: Modifier = Modifier) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val orangePalette = toPalette(orange)
        Spacer(Modifier.height(64.dp))
        Card(
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(128.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.ic_category_1),
                    modifier = Modifier.size(96.dp),
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(text = "Food & Drink", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Sharma World and Sun cafe Coffee",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text = "₹ 3,892.00",
            style = MaterialTheme.typography.displayMedium.copy(fontFamily = numberFont),
            color = orangePalette.main
        )
        Spacer(Modifier.height(48.dp))
        Box(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()

                ) {
                    SingleInfoBox(
                        Modifier.weight(1f),
                        "Budget Category",
                        "Food & Expenses"
                    )
                    Spacer(Modifier.width(12.dp))
                    SingleInfoBox(
                        Modifier.weight(1f),
                        "Transaction Type",
                        "Expense"
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    SingleInfoBox(
                        Modifier.weight(1f),
                        "Date",
                        "30 July 2025"
                    )
                    Spacer(Modifier.width(12.dp))
                    SingleInfoBox(
                        Modifier.weight(1f),
                        "Paid By",
                        "HDFC Bank"
                    )
                }
            }

        }

    }

}