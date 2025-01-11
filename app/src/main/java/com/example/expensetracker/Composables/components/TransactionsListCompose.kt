package com.example.expensetracker.Composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.Composables.CustomFonts
import com.example.expensetracker.Composables.failureColor
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.ViewModels.AddActivityViewModel

@Composable
fun TransactionsListCompose(viewModel: AddActivityViewModel) {
    val transactionList by viewModel.allTransactions.observeAsState(emptyList<TransactionClass>())
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(transactionList) { item ->
            SingleTransaction(item, onSingleItemClick = { onClick(item) })
        }
    }
}

@Composable
fun SingleTransaction(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onSingleItemClick(item) }
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .background(Color.White)
                .wrapContentHeight()
                .padding(12.dp)

        ) {
            var iconImage = R.drawable.icon_expense
            var amount = item.amount.toString()
            var amountColor = successColor
            if (item.type.equals(EXPENSE)) {
                iconImage =R.drawable.icon_expense
                amount = "-₹"+amount
                amountColor = failureColor
            } else {
                iconImage =R.drawable.icon_income
                amount = "+₹"+amount
                amountColor = successColor
            }

            Image(
                painter = painterResource(id = iconImage),
                contentDescription = "Test Image",
                modifier = Modifier
                    .fillMaxWidth(0.1f)
                    .height(46.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.75f)

            ) {
                Text(
                    text = item.note,
                    style = typography.titleMedium
                )
                Text(
                    text = getDateFromMillis(item.dateWithTime),
                    style = typography.labelMedium
                )
            }

            Text(
                text = amount,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                style = typography.titleMedium,
                fontFamily = CustomFonts.numberFont,
                color = amountColor,
                textAlign = TextAlign.End
            )

        }
    }
}

fun onClick(item: TransactionClass) {

}

@Preview
@Composable
fun SingleTransactionPreview() {
    SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L), {})
}
