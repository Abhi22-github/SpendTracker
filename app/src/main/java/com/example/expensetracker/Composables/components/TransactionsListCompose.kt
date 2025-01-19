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
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import coil.compose.rememberAsyncImagePainter
import com.example.expensetracker.Composables.CustomFonts
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.blueColor
import com.example.expensetracker.Composables.failureColor
import com.example.expensetracker.Composables.greenColor
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Composables.utils.HarmonizedColorPalette
import com.example.expensetracker.Composables.utils.IconState
import com.example.expensetracker.Composables.utils.IconStateForType
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Composables.utils.toPalette
import com.example.expensetracker.Converters.TransactionConverter
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.Model.TransactionTypeClass
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.Constants.INCOME
import com.example.expensetracker.Utilities.convertLocalDateToLong
import com.example.expensetracker.Utilities.convertMillisToDateString
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.ViewModels.CategoryViewModel
import com.example.expensetracker.ViewModels.TransactionsViewModel
import java.time.LocalDate

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
        BottomSheetContentItemDetailsContent(modifier = Modifier,closeBottomSheet)
    }
}

val bottomSheetStartEndPadding = 16.dp
val bottomSheetTopBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemDetailsContent(
    modifier: Modifier,closeBottomSheet: () -> Unit, categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    var cashMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    val focusRequester = remember { FocusRequester() }
    val categoryList by categoryViewModel.categoryList.collectAsState()
    var selectedCategory by remember { mutableStateOf(CategoryClass(-1, "Select", 1, 99, EXPENSE)) }
    val list = listOf(
        TransactionTypeClass(1, EXPENSE),
        TransactionTypeClass(2, INCOME)
    )
    var selectedType by remember { mutableStateOf(TransactionTypeClass(1, EXPENSE)) }


    // Request focus once when the composable is first composed
    LaunchedEffect(Unit) {
        // Request focus for the TextField
        focusRequester.requestFocus()
        categoryViewModel.getCorrespondingList(selectedType.type)
        categoryViewModel.getOnlyExpenseCategoryNames()
        categoryViewModel.getOnlyIncomeCategoryNames()
    }

    Column(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column(

        ) {
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
                FilledTonalIconButton(
                    onClick = {},
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "ArrowUp",
                    )
                }
                RestBudgetPill(LocalDate.now())
            }

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
                        val image =
                            rememberAsyncImagePainter(IconStateForType.fromNumber(selectedType.iconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )

                        Text(
                            text = selectedType.type,
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(start = 8.dp),
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
                        cashMenuExpanded,
                        colorPalletBlue,
                        onDismiss = { cashMenuExpanded = false },
                        2,
                        null,
                        list,
                        selectedType,
                        selectedTypeSetter = {
                            selectedType = it
                            categoryViewModel.getCorrespondingList(it.type)
                        },
                        selectedCategory = selectedCategory,
                        selectedCategorySetter = { selectedCategory = it },
                    )
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
                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(selectedCategory.categoryIconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )

                        Text(
                            text = selectedCategory.categoryName,
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(start = 8.dp),
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
                        1,
                        categoryList,
                        null,
                        selectedType,
                        selectedTypeSetter = {
                            selectedType = it
                            categoryViewModel.getCorrespondingList(it.type)
                        },
                        selectedCategory = selectedCategory,
                        selectedCategorySetter = { selectedCategory = it },
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
            BottomRow(
                modifier,
                selectedDate,
                { selectedDate = it }, buttonClicked = {
                    transactionsViewModel.validateTransactionData(
                        selectedType.type,
                        selectedCategory,
                        expenseValue.text,
                        comment.text,
                        selectedDate
                    )
                    closeBottomSheet()
                }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRow(
    modifier: Modifier,
    selectedDate: Long?,
    selectedDateSetter: (Long?) -> Unit,
    buttonClicked: () -> Unit
) {
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
                    selectedDateSetter(selectedDate)
                }, colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                ), contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
            ) {
                Icon(Icons.Rounded.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
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
                onClick = { buttonClicked() },
            ) {
                Text("Save")
            }
        }
    }
    if (showDatePicker) {
        DatePickerModal(datePickerState, onDateSelected = { date ->
            selectedDateSetter(date)
        }, onDismiss = { showDatePicker = !showDatePicker })
    }
}

//type 1 -> CategoryList
//type 2 -> expense/Income
@Composable
fun DropDownMenu(
    modifier: Modifier,
    menuExpanded: Boolean,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    type: Int,
    itemList: List<CategoryClass>?,
    typeList: List<TransactionTypeClass>?,
    selectedType: TransactionTypeClass,
    selectedTypeSetter: (TransactionTypeClass) -> Unit,
    selectedCategory: CategoryClass,
    selectedCategorySetter: (CategoryClass) -> Unit
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surfaceVariant,
        shape = RoundedCornerShape(24.dp),
    ) {
        if (type == 1) {
            itemList?.forEach { categoryClass ->
                val selectedIcon =
                    rememberAsyncImagePainter(IconState.fromNumber(categoryClass.categoryIconNumber))
                DropdownMenuItem(text = { Text(text = categoryClass.categoryName) }, leadingIcon = {
                    Image(
                        painter = selectedIcon,
                        contentDescription = categoryClass.categoryName,
                        modifier = Modifier.size(24.dp),
                    )
                }, onClick = {
                    selectedCategorySetter(categoryClass)
                    onDismiss()
                })
            }
        }
        if (type == 2) {
            typeList?.forEach { transactionTypeClass ->
                val selectedIcon =
                    rememberAsyncImagePainter(IconStateForType.fromNumber(transactionTypeClass.iconNumber))
                DropdownMenuItem(text = { Text(text = transactionTypeClass.type) }, leadingIcon = {
                    Image(
                        painter = selectedIcon,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                    )
                }, onClick = {
                    selectedTypeSetter(transactionTypeClass)
                    selectedCategorySetter(CategoryClass(-1, "Select", 1, 99, EXPENSE))
                    onDismiss()
                })
            }
        }

    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    ExpenseTrackerTheme {
        Surface {
            BottomSheetContentItemDetailsContent(Modifier,{})
        }
    }
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
    Header(11L)
}