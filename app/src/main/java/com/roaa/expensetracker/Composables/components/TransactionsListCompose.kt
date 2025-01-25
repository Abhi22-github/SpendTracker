package com.roaa.expensetracker.Composables.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts
import com.roaa.expensetracker.Composables.Screens.PaymentCard
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.failureColor
import com.roaa.expensetracker.Composables.successColor
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Converters.TransactionConverter
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.convertLocalDateToLong
import com.roaa.expensetracker.Utilities.getDateFromMillis
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListCompose(
    showSingleDateTransactions: Boolean,
    date: LocalDate,
    viewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bottomSheet by uiViewModel.transactionDetailBottomSheetValue.collectAsState()
    var singleTransaction by remember { mutableStateOf(TransactionClass()) }
    val scope = rememberCoroutineScope()
    val showForecast by preferencesViewModel.showForecastBar.collectAsState(false)
    Column {
        if (!showSingleDateTransactions) {

            val transactionList by viewModel.allTransactions.collectAsState(emptyList<TransactionClass>())
            val transactionsMap =
                transactionList.sortedByDescending { it.dateWithTime }.groupBy { it.date }
                    .toSortedMap()

            val transactionConverterList = transactionsMap.map {
                TransactionConverter(it.key.toString(), it.value)
            }.reversed()
            val lazyList = rememberLazyListState()
            Surface(color = MaterialTheme.colorScheme.surface) {
                if (!transactionConverterList.isEmpty()) LazyColumn(
                    modifier = Modifier.fillMaxWidth(), state = lazyList
                ) {
                    item {
                        if (showForecast)
                            SummaryCard(blueColor)
                        else
                            PaymentCard(blueColor)
                    }
                    transactionConverterList.forEach { (date, transactionList) ->
                        val date = getDateFromMillis(transactionList.get(0).dateWithTime)
                        item { Header(if (date == getDateFromMillis(System.currentTimeMillis())) "Today" else date) }
                        items(transactionList, key = { it.id }) { item ->
                            SingleTransaction(item, onSingleItemClick = {
                                singleTransaction = (item)
                                scope.launch {
                                    uiViewModel.transactionDetailBottomSheetValue.emit(true)
                                }
                            })
                        }
                    }
                }
                else EmptyScreen()
            }
        } else {
            viewModel.getAllTransactionsForDate(convertLocalDateToLong(date))
            val transactionList by viewModel.getAllTransactionsForDateFlow.collectAsState()
            val lazyList = rememberLazyListState()
            Surface(color = MaterialTheme.colorScheme.surface) {
                if (!transactionList.isEmpty()) LazyColumn(
                    modifier = Modifier.fillMaxWidth(), state = lazyList
                ) {
                    items(transactionList, key = { it.id }) { item ->
                        SingleTransaction(item, onSingleItemClick = {
                            singleTransaction = (item)
                            scope.launch {
                                uiViewModel.transactionDetailBottomSheetValue.emit(true)
                            }
                        })
                    }
                }
                else EmptyScreen()
            }
        }

        if (bottomSheet) {
            BottomSheetContentItemDetails(bottomSheetState, singleTransaction)
        }
    }
}

@Composable
fun SingleTransaction(item: TransactionClass, onSingleItemClick: (TransactionClass) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
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
                    val image = rememberAsyncImagePainter(IconState.fromNumber(item.categoryIcon))
                    Image(
                        painter = image,
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
                if (true) {
                    Text(
                        text = item.category,
                        style = typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
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
                SingleTransaction(
                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
                    {})
                SingleTransaction(
                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
                    {})
                SingleTransaction(
                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
                    {})
            }

        }
    }


}

@Composable
fun Header(date: String) {
    Text(
        text = date,
        style = typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 14.dp, 8.dp, 4.dp)
    )
}


@Preview
@Composable
fun SingleTransactionPreview() {
    SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L), {})
}


@Preview
@Composable
fun SingleTransactionNewPreview() {
    SingleTransactionNew(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L)) {}
}

@Preview
@Composable
fun HeaderPreview() {
    Header("Today")
}