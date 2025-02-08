package com.roaa.expensetracker.Composables.components

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.RootScreen
import com.roaa.expensetracker.Composables.failureColor
import com.roaa.expensetracker.Composables.successColor
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Converters.TransactionConverter
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Model.emptyBank
import com.roaa.expensetracker.Model.emptyCategoryClass
import com.roaa.expensetracker.Model.emptyTransactionClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.LongMillisToNoralLong
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toDisplayDate
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListCompose(
    navController: NavigationManager,
    modifier: Modifier,
    showSingleDateTransactions: Boolean,
    date: Long,
    viewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var bottomSheet by remember { mutableStateOf(false) }
    val showNewLayouts by preferencesViewModel.showForecastBar.collectAsState(false)
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                showAddBottomSheet = !showAddBottomSheet
            },
            icon = { Icon(Icons.Filled.Add, "Localized description") },
            text = { Text(text = "Add") },
        )
    }) {

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var singleTransaction by remember {
            mutableStateOf(
                TransactionWithDetails(
                    emptyTransactionClass,
                    emptyCategoryClass,
                    emptyBank
                )
            )
        }
        val scope = rememberCoroutineScope()
        val showForecast by preferencesViewModel.showForecastBar.collectAsState(false)
        Column {
            if (!showSingleDateTransactions) {

                val transactionList by viewModel.allTransactions.collectAsState(emptyList())
                val transactionsMap =
                    transactionList.sortedByDescending { it.transaction.date }
                        .groupBy { it.transaction.date }
                        .toSortedMap()

                val transactionConverterList = transactionsMap.map {
                    TransactionConverter(it.key.toString(), it.value)
                }.reversed()
                val lazyList = rememberLazyListState()
                Surface(color = MaterialTheme.colorScheme.surface) {
                    if (!transactionConverterList.isEmpty()) LazyColumn(
                        modifier = Modifier.fillMaxWidth(), state = lazyList
                    ) {
                        transactionConverterList.forEach { (date, transactionList) ->
                            val date = transactionList.get(0).transaction.date
                            item {
                                Header(
                                    if (date == System.currentTimeMillis().LongMillisToNoralLong()
                                    ) "Today" else date.toLocalDate().toDisplayDate()
                                )
                            }
                            items(transactionList, key = { it.transaction.id }) { item ->
                                SingleTransaction(item, onSingleItemClick = {

                                    singleTransaction = (item)
                                    if(showNewLayouts)
                                        navController.navigateTo(RootScreen.DetailsScreen)
                                    else
                                    bottomSheet = true

                                })
                            }
                        }
                    }
                    else EmptyScreen()
                }
            } else {
                viewModel.getAllTransactionsForDate(date)
                val transactionList by viewModel.getAllTransactionsForDateCompose(
                    date
                ).collectAsState(listOf())
                val lazyList = rememberLazyListState()
                Surface(color = MaterialTheme.colorScheme.surface) {
                    if (!transactionList.isEmpty()) LazyColumn(
                        modifier = Modifier.fillMaxWidth(), state = lazyList
                    ) {
                        items(transactionList, key = { it.transaction.id }) { item ->
                            SingleTransaction(item, onSingleItemClick = {
                                singleTransaction = (item)
                                if(showNewLayouts)
                                    navController.navigateTo(RootScreen.DetailsScreen)
                                else
                                    bottomSheet = true
                            })
                        }
                    }
                    else EmptyScreen()
                }
            }

            if (bottomSheet) {
                BottomSheetContentItemDetails(
                    bottomSheetState,
                    singleTransaction,
                    { bottomSheet = !bottomSheet })
            }
            if (showAddBottomSheet) {
                AddBottomSheet(date, { showAddBottomSheet = !showAddBottomSheet })
            }
        }
    }
}

@Composable
fun SingleTransaction(
    item: TransactionWithDetails,
    onSingleItemClick: (TransactionWithDetails) -> Unit
) {
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
            var amount = parseAmount(item.transaction.amount)
            var amountColor = successColor
            if (item.category.categoryType.equals(EXPENSE)) {
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
                    val image =
                        rememberAsyncImagePainter(IconState.fromNumber(item.category.categoryIconNumber))
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
                    .fillMaxWidth(0.60f)

            ) {
                Text(
                    text = item.transaction.note.replaceFirstChar { it.uppercase() },
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (true) {
                    Text(
                        text = item.category.categoryName,
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
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
            Text(text = item.dateWithTime.toDisplayDate(), style = typography.titleMedium)
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
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L,1L,1L),
//                    {})
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
//                    {})
//                SingleTransaction(
//                    item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L),
//                    {})
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
    //  SingleTransaction(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L), {})
}


@Preview
@Composable
fun SingleTransactionNewPreview() {
    // SingleTransactionNew(item = TransactionClass("Expesne", 20L, "Hello", "", 99, 0L, 0L)) {}
}

@Preview
@Composable
fun HeaderPreview() {
    Header("Today")
}