package com.example.expensetracker.Composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.blueColor
import com.example.expensetracker.Composables.greenColor
import com.example.expensetracker.Composables.orange
import com.example.expensetracker.Composables.successColor
import com.example.expensetracker.Composables.utils.IconState
import com.example.expensetracker.Composables.utils.IconStateForType
import com.example.expensetracker.Composables.utils.toPalette
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Model.TransactionTypeClass
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.Utilities.Constants.INCOME
import com.example.expensetracker.Utilities.convertMillisToDateString
import com.example.expensetracker.ViewModels.CategoryViewModel
import com.example.expensetracker.ViewModels.TransactionsViewModel
import com.example.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentItemDetails(sheetState: SheetState, closeBottomSheet: () -> Unit) {
    ModalBottomSheet(onDismissRequest = {
        closeBottomSheet()
    },
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

    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var cashMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    val focusRequester = remember { FocusRequester() }
    val categoryList by categoryViewModel.categoryList.collectAsState()
    var selectedCategory by remember { mutableStateOf(CategoryClass(-1, "Select", 1, 99, EXPENSE)) }

    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)


    val errorStatus by uiViewModel.errorStatusInAddBottomSheet.collectAsState(false)


    //animations
    var expanded by remember { mutableStateOf(false) }
    var typeToggle by remember { mutableStateOf(true) }
    val boxSize by animateDpAsState(targetValue = if (expanded) 160.dp else 56.dp, animationSpec = tween(500))
    val colorAnimate by animateColorAsState(
        targetValue = if (typeToggle) orange.copy(alpha = .20f) else successColor.copy(
            alpha = 0.20f
        ), animationSpec = tween(500)
    )

    var selectedType by remember { mutableStateOf(expenseType.type) }
    if (typeToggle) {
        selectedType = expenseType.type
    } else {
        selectedType = incomeType.type
    }

    // Request focus once when the composable is first composed
    LaunchedEffect(Unit) {
        // Request focus for the TextField
        focusRequester.requestFocus()
        categoryViewModel.getCorrespondingList(selectedType)
        categoryViewModel.getOnlyExpenseCategoryNames()
        categoryViewModel.getOnlyIncomeCategoryNames()
    }

    LaunchedEffect(expanded, typeToggle) {
        if (expanded) {
            delay(5000)
            expanded = false
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column() {
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
                RestBudgetPill(LocalDate.now())
            }

            Spacer(Modifier.height(16.dp))
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(boxSize)
                        .background(
                            color = colorAnimate, shape = RoundedCornerShape(30.dp)
                        )
                        .clickable {
                            if (!expanded) {
                                expanded = !expanded
                            } else {
                                typeToggle = !typeToggle
                                categoryViewModel.getCorrespondingList(if(typeToggle) expenseType.type else incomeType.type)
                            }
                        }
                        .height(56.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val colorPalletBlue = toPalette(blueColor)
                        val image =
                            rememberAsyncImagePainter(
                                if (typeToggle)
                                    IconStateForType.fromNumber(expenseType.iconNumber)
                                else
                                    IconStateForType.fromNumber(incomeType.iconNumber)
                            )
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(36.dp),
                        )

                        AnimatedVisibility(expanded) {
                            Text(
                                text = if (typeToggle) expenseType.type else incomeType.type,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = modifier.fillMaxWidth()
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
                        categoryMenuExpanded,
                        colorPalletGreen,
                        onDismiss = { categoryMenuExpanded = false },
                        categoryList,
                        selectedCategorySetter = {
                            selectedCategory = it
                            scope.launch {
                                uiViewModel.errorStatusInAddBottomSheet.emit(false)
                            }
                        },
                    )

                }
            }

            Spacer(Modifier.height(50.dp))
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
                        scope.launch {
                            uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
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
                        focusedContainerColor = Color.Transparent,
                    ),
                    textStyle = typography.displayMedium.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Spacer(Modifier.height(0.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = comment,
                    onValueChange = { newValue ->
                        comment = newValue
                        scope.launch {
                            uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
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
                    textStyle = typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
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

        AnimatedVisibility(errorStatus) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(
                    modifier = Modifier.padding(16.dp, 0.dp), colors = CardDefaults.cardColors(
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(modifier = modifier.padding(16.dp, 12.dp)) {
                        ErrorRow(uiViewModel)
                    }
                }
            }

        }
        Spacer(Modifier.height(16.dp))
        Row {
            BottomRow(modifier, selectedDate, { selectedDate = it }, buttonClicked = {
                validateTransactionData(
                    selectedType,
                    selectedCategory,
                    expenseValue.text,
                    comment.text,
                    selectedDate,
                    scope,
                    uiViewModel,
                    transactionsViewModel,
                )
            })
        }
        Spacer(Modifier.height(8.dp))
    }
}


fun validateTransactionData(
    type: String,
    selectedCategory: CategoryClass,
    amount: String,
    comment: String,
    selectedDate: Long?,
    scope: CoroutineScope,
    uiViewModel: UiViewModel,
    transactionsViewModel: TransactionsViewModel
) {

    scope.launch {
        if (selectedCategory.categoryName == "Select") {
            uiViewModel.errorStatusInAddBottomSheet.emit(true)
            uiViewModel.errorStatusMessage.emit("Please select a category")
            return@launch
        }
        if (amount == "0" || amount.isEmpty()) {
            uiViewModel.errorStatusInAddBottomSheet.emit(true)
            uiViewModel.errorStatusMessage.emit("Please enter amount")
            return@launch
        }
        if (comment.isEmpty()) {
            uiViewModel.errorStatusInAddBottomSheet.emit(true)
            uiViewModel.errorStatusMessage.emit("Please provide some comment")
            return@launch
        }
        transactionsViewModel.validateAndPrepareTransactionData(
            type,
            selectedCategory,
            amount,
            comment,
            selectedDate,
        )
        transactionsViewModel.bottomSheetStatus.emit(
            false
        )

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

@Composable
fun ErrorRow(uiViewModel: UiViewModel) {
    val errorMessage by uiViewModel.errorStatusMessage.collectAsState()
    Text(
        text = errorMessage,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyMedium
    )
}


//delete this after properly integrating with compose
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBottomSheet(
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val bottomSheet by transactionsViewModel.bottomSheetStatus.collectAsState(false)
    if (bottomSheet) {
        BottomSheetContentItemDetails(bottomSheetState) {
            scope.launch {
                transactionsViewModel.bottomSheetStatus.emit(
                    !bottomSheet
                )
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
