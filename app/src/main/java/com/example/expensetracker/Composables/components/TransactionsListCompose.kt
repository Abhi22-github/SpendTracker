package com.example.expensetracker.Composables.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.expensetracker.Composables.CustomFonts
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.blueColor
import com.example.expensetracker.Composables.failureColor
import com.example.expensetracker.Composables.greenColor
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Composables.utils.HarmonizedColorPalette
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Composables.utils.toPalette
import com.example.expensetracker.Converters.TransactionConverter
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.convertLocalDateToLong
import com.example.expensetracker.Utilities.convertMillisToDateString
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.ViewModels.CategoryViewModel
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
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
            if (!transactionConverterList.isEmpty()) LazyColumn(
                modifier = Modifier.fillMaxWidth(), state = lazyList
            ) {
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
                        onSingleItemClick(item)
                        bottomSheet = true
                    })
                }
            }
            else EmptyScreen()
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
                    val image = rememberAsyncImagePainter(R.drawable.ic_category_1)
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
    ModalBottomSheet(onDismissRequest = { closeBottomSheet() },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemDetailsContent(modifier = Modifier)
    }
}

val bottomSheetStartEndPadding = 16.dp
val bottomSheetTopBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemDetailsContent(
    modifier: Modifier,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var cashMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val expenseCategoryList by categoryViewModel.onlyExpenseCategoryNames.collectAsState()

    // Request focus once when the composable is first composed
    LaunchedEffect(Unit) {
        // Request focus for the TextField
        focusRequester.requestFocus()
    }

    Column(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column(

        ) {
//            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
//                FilledTonalIconButton(
//                    onClick = {},
//                    colors = IconButtonDefaults.filledTonalIconButtonColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
//                        contentColor = MaterialTheme.colorScheme.onSurface
//                    )
//                ) {
//                    Icon(
//                        Icons.Filled.ArrowForward,
//                        contentDescription = "ArrowUp",
//                    )
//                }
//                RestBudgetPill(LocalDate.now())
//            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val colorPalletBlue = toPalette(blueColor)
                    Button(
                        modifier = Modifier.padding(end = 5.dp),
                        onClick = { cashMenuExpanded = !cashMenuExpanded },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(
                            start = 20.dp, end = 10.dp, top = 16.dp, bottom = 16.dp
                        )
                    ) {
                        val image = rememberAsyncImagePainter(R.drawable.ic_category_1)
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )

                        Text(
                            text = "Cash",
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(start = 5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            "backIcon",
                            modifier = Modifier.weight(0.2f)
                        )
                    }
//                    DropDownMenu(modifier,
//                        cashMenuExpanded,
//                        colorPalletBlue,
//                        onDismiss = { cashMenuExpanded = false })
                }
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val colorPalletGreen = toPalette(greenColor)
                    Button(
                        modifier = Modifier.padding(end = 5.dp),
                        onClick = { categoryMenuExpanded = !categoryMenuExpanded },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(
                            start = 20.dp, end = 10.dp, top = 16.dp, bottom = 16.dp
                        )
                    ) {
                        val image = rememberAsyncImagePainter(R.drawable.ic_category_1)
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )

                        Text(
                            text = "Entertainment",
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(start = 5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            "backIcon",
                            modifier = Modifier.weight(0.2f)
                        )
                    }

                    DropDownMenu(
                        modifier,
                        categoryMenuExpanded,
                        colorPalletGreen,
                        onDismiss = { categoryMenuExpanded = false },
                        expenseCategoryList
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = expenseValue,
                    onValueChange = { newValue ->
                        expenseValue = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .focusRequester(focusRequester),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "₹0",
                            style = typography.displayMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    },

                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    textStyle = typography.displayMedium.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = comment,
                    onValueChange = { newValue ->
                        comment = newValue
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Add a comment",
                            style = typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    textStyle = typography.bodyLarge.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }


//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
//                ) {
//                    TextField(
//                        value = expenseValue,
//                        onValueChange = { newValue ->
//                            expenseValue = newValue
//                        },
//                        modifier = Modifier
//                            .wrapContentWidth(),
//
//                        singleLine = true,
//                        placeholder = {
//                            Text(
//                                "Description",
//                                style = typography.bodyMedium,
//                                modifier = Modifier
//                                    .wrapContentWidth(),
//                                textAlign = TextAlign.Center,
//                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
//                            )
//                        },
//                        shape = RoundedCornerShape(24.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            unfocusedContainerColor = Color.Transparent,
//                            focusedContainerColor = Color.Transparent
//                        ),
//                        textStyle = typography.bodyMedium,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                    )
//                }

        }
        Row {
            BottomRow(modifier)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRow(modifier: Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
    ) {
        Row(modifier = modifier.weight(1f)) {

            FilledTonalButton(
                onClick = {
                    showDatePicker = !showDatePicker
                }, colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                ), contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
            ) {
                Icon(Icons.Rounded.DateRange, contentDescription = null)
                Spacer(Modifier.width(3.dp))
                Text(text = if (selectedDate?.let { convertMillisToDateString(it) } == convertMillisToDateString(
                        System.currentTimeMillis()
                    )) {
                    "Today"
                } else {
                    selectedDate?.let {
                        convertMillisToDateString(it)
                    } ?: "Date Error"
                })

            }


        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            FilledTonalButton(
                onClick = { },
            ) {
                Text("Save")
            }
        }
    }
    if (showDatePicker) {
        DatePickerModal(datePickerState, onDateSelected = { date ->
            selectedDate = date
        }, onDismiss = { showDatePicker = !showDatePicker })
    }
}


@Composable
fun DropDownMenu(
    modifier: Modifier,
    menuExpanded: Boolean,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    itemList: List<CategoryClass>
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surfaceVariant,
        shape = RoundedCornerShape(24.dp),

        ) {
//
//        val category2 = rememberAsyncImagePainter(R.drawable.ic_category_2)
//        val category3 = rememberAsyncImagePainter(R.drawable.ic_category_3)
//        val category4 = rememberAsyncImagePainter(R.drawable.ic_category_4)
//        val category5 = rememberAsyncImagePainter(R.drawable.ic_category_6)

        itemList.forEach { categoryClass ->
            val selectedIcon = rememberAsyncImagePainter(R.drawable.ic_category_2)
            DropdownMenuItem(text = { Text(text = categoryClass.categoryName) }, leadingIcon = {
                Image(
                    painter = selectedIcon,
                    contentDescription = categoryClass.categoryName,
                    modifier = Modifier.size(24.dp),
                )
            }, onClick = { /* Do something... */ })
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