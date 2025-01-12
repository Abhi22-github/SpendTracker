package com.example.expensetracker.Composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.Composables.CustomFonts
import com.example.expensetracker.Composables.failureColor
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.ViewModels.AddActivityViewModel
import kotlin.random.Random

@Composable
fun TransactionsListCompose(viewModel: AddActivityViewModel) {

    val transactionList by viewModel.allTransactionFlow.collectAsState(emptyList<TransactionClass>())
    val transactionsMap = transactionList.groupBy { it.date }.toSortedMap()
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        transactionsMap.forEach { (date, transactionList) ->
            items(transactionList) { item ->
                SingleTransactionNew(item, onSingleItemClick = { onClick(item) })
            }
        }

    }
}

val iconList = listOf(
    R.drawable.ic_category_1,
    R.drawable.ic_category_2,
    R.drawable.ic_category_3,
    R.drawable.ic_category_4,
    R.drawable.ic_category_5,
    R.drawable.ic_category_6,
    R.drawable.ic_category_7,
    R.drawable.ic_category_8,
    R.drawable.ic_category_9,
    R.drawable.ic_category_10,
)

@Composable
fun SingleTransaction(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .clickable {
                onSingleItemClick(item)
            }
    ) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp, 12.dp)
            ) {
                var amount = item.amount.toString()
                var amountColor = successColor
                if (item.type.equals(EXPENSE)) {
                    amount = "-₹" + amount
                    amountColor = failureColor
                } else {
                    amount = "+₹" + amount
                    amountColor = successColor
                }
                val randomNumber = Random.nextInt(0, 9)
                Box(
                    modifier = Modifier
                        .size(48.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Surface(
                        shape = CircleShape, modifier = Modifier
                            .fillMaxSize(), color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = iconList[randomNumber]),
                                contentDescription = "Test Image",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.70f)

                ) {
                    Text(
                        text = item.note.replaceFirstChar { it.uppercase() },
                        style = typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.type,
                        style = typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

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
}

fun onClick(item: TransactionClass) {
}

@Composable
fun SingleTransactionNew(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                angle = 0.3f,
            ),
        ),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = getDateFromMillis(item.dateWithTime), style = typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = combineColors(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant,
                        angle = 0.3f,
                    ),
                ),
            ) {
                SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L), {})
                SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L), {})
                SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L), {})
            }

        }
    }


}


@Preview
@Composable
fun SingleTransactionPreview() {
    SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L), {})
}

@Preview
@Composable
fun SingleTransactionNewPreview() {
    SingleTransactionNew(item = TransactionClass("Expesne", 20L, "Hello", "", 0L, 0L)) {}
}
