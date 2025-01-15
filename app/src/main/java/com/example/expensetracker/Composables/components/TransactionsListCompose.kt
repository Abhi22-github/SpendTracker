package com.example.expensetracker.Composables.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.Composables.CustomFonts
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.failureColor
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Converters.TransactionConverter
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.convertLocalDateToLong
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.ViewModels.TransactionsViewModel
import java.time.LocalDate
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListCompose(
    showSingleDateTransactions: Boolean,
    date: LocalDate,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState()
    var bottomSheet by remember { mutableStateOf(false) }
    //  val uiState by viewModel.uiState.collectAsState()
//    LaunchedEffect(uiState) {
//        when(uiState){
//            is UiState.Loading -> viewModel.getAllTransactionsForDate(LocalDateToLong(date))
//            is UiState.Error -> {}
//            UiState.Success -> {}
//        }
//    }

    if (!showSingleDateTransactions) {
        val transactionList by viewModel.allTransactions.collectAsState(emptyList<TransactionClass>())
        val transactionsMap =
            transactionList.sortedByDescending { it.dateWithTime }.groupBy { it.date }.toSortedMap()

        val transactionConverterList = transactionsMap.map {
            TransactionConverter(it.key.toString(), it.value)
        }.reversed()
        val lazyList = rememberLazyListState()

        Surface(color = MaterialTheme.colorScheme.surface) {
            if (!transactionConverterList.isEmpty())
                LazyColumn(modifier = Modifier.fillMaxWidth(), state = lazyList) {
                    transactionConverterList.forEach { (date, transactionList) ->
                        item { Header(transactionList.get(0).dateWithTime) }
                        items(transactionList, key = { it.id }) { item ->
                            SingleTransaction(item, onSingleItemClick = {
                                onSingleItemClick(item)
                                bottomSheet = true
                            })
                        }
                    }
                }
            else
                EmptyScreen()
        }
    } else {
        viewModel.getAllTransactionsForDate(convertLocalDateToLong(date))
        val transactionList by viewModel.getAllTransactionsForDateFlow.collectAsState()
        val lazyList = rememberLazyListState()
        Surface(color = MaterialTheme.colorScheme.surface) {
            if (!transactionList.isEmpty())
                LazyColumn(modifier = Modifier.fillMaxWidth(), state = lazyList) {
                    items(transactionList, key = { it.id }) { item ->
                        SingleTransaction(item, onSingleItemClick = { onSingleItemClick(item) })
                    }
                }
            else
                EmptyScreen()
        }
    }

    if (bottomSheet) {
        BottomSheetContentItemDetails(bottomSheetState) { bottomSheet = false }
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
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(16.dp, 4.dp)
            .clickable {
                onSingleItemClick(item)
            }, colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                angle = 0.3f,
            )
        )
    ) {

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


            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .size(36.dp)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_category_1),
                        contentDescription = "Test Image",
                        modifier = Modifier.size(24.dp),
                    )
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
                if (false) {
                    Text(
                        text = item.type,
                        style = typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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

fun onSingleItemClick(item: TransactionClass) {

}

@Composable
fun SingleTransactionNew(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {

    Card(
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                angle = 0.3f,
            ),
        ), modifier = Modifier.padding(16.dp)
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

@Composable
fun Header(date: Long) {
    Text(
        text = getDateFromMillis(date),
        style = typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 14.dp, 8.dp, 4.dp)
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentItemDetails(sheetState: SheetState, closeBottomSheet: () -> Unit) {
    ModalBottomSheet(onDismissRequest = { closeBottomSheet() }, sheetState = sheetState) {
        BottomSheetContentItemDetailsContent(modifier = Modifier)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContentItemDetailsContent(
    modifier: Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = "Set up a budget",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Daily Budget", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Enter your daily budget amount",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
        )

        Spacer(Modifier.height(24.dp))

        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { },
            ) {
                Icon(painter = painterResource(R.drawable.round_edit), contentDescription = null)
                Text(text = "Edit")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { },
            ) {
                Icon(
                    painter = painterResource(R.drawable.round_delete_outline_24),
                    contentDescription = null
                )
                Text(text = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    ExpenseTrackerTheme {
        Surface {
            BottomSheetContentItemDetailsContent(Modifier)
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

@Preview
@Composable
fun HeaderPreview() {
    Header(11L)
}