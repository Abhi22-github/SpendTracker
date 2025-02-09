package com.roaa.expensetracker.Composables.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.Screens.LivePaymentCard
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.secondaryAlpha
import com.roaa.expensetracker.Composables.secondaryAlphaForElements
import com.roaa.expensetracker.Composables.successColor
import com.roaa.expensetracker.Composables.utils.ColorState
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.IconStateForType
import com.roaa.expensetracker.Composables.utils.colorList
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.iconsList
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TransactionTypeClass
import com.roaa.expensetracker.Model.emptyBank
import com.roaa.expensetracker.Model.firstSampleClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.DecimalFilterTransformation
import com.roaa.expensetracker.Utilities.convertMillisToDateString
import com.roaa.expensetracker.Utilities.extractNumbers
import com.roaa.expensetracker.Utilities.getCurrentDate
import com.roaa.expensetracker.Utilities.getCurrentMonthName
import com.roaa.expensetracker.Utilities.getMonthEndDate
import com.roaa.expensetracker.Utilities.getRemainingDaysInCurrentMonth
import com.roaa.expensetracker.Utilities.getValidDatesListFromLong
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLongMillis
import com.roaa.expensetracker.ViewModels.AnimationViewModel
import com.roaa.expensetracker.ViewModels.BankAccountsViewModel
import com.roaa.expensetracker.ViewModels.BudgetDayViewModel
import com.roaa.expensetracker.ViewModels.BudgetViewModel
import com.roaa.expensetracker.ViewModels.CategoryViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentAddItem(date: Long, sheetState: SheetState, closeBottomSheet: () -> Unit) {
    ModalBottomSheet(onDismissRequest = {
        closeBottomSheet()
    },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemAddContent(modifier = Modifier, date, closeBottomSheet)
    }
}


val bottomSheetStartEndPadding = 16.dp
val bottomSheetTopBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemAddContent(
    modifier: Modifier,
    date: Long,
    closeBottomSheet: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
    animationViewModel: AnimationViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(date) }
    var selectedPaymentMethod by remember { mutableStateOf<BankAccountsClass>(emptyBank) }
    val focusRequester = remember { FocusRequester() }
    val categoryList by categoryViewModel.categoryList.collectAsState()
    val showForecast by preferencesViewModel.showForecastBar.collectAsState(false)

    var selectedCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }

    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)


    val errorStatus by uiViewModel.errorStatusInAddBottomSheet.collectAsState(false)


    //animations
    var expanded by remember { mutableStateOf(false) }
    var typeToggle by remember { mutableStateOf(true) }
    val boxSize by animateDpAsState(
        targetValue = if (expanded) 160.dp else 56.dp, animationSpec = tween(500)
    )
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
    val budget by preferencesViewModel.getTotalAmountPerDay.collectAsState(1f)
    val oldAmount by transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()
    val newAmountTemp = if (expenseValue.text.isEmpty()) 0L else extractNumbers(expenseValue.text)
    val newDailyBudget = oldAmount + newAmountTemp
    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget
    } else {
        0f
    }
    animationViewModel.method("₹$amountInString", percent)

    LaunchedEffect(percent) {
        scope.launch {
            animationViewModel.newSpentPercentage.emit(percent)
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column() {
            Text(
                text = "Add Transaction",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
//            AnimatedVisibility(showForecast) {
//                Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
//                    RestBudgetPill(LocalDate.now().toLong())
//                }
//            }
            if (showForecast) {
                Spacer(Modifier.height(16.dp))
            }
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(boxSize)
                        .background(
                            color = colorAnimate, shape = RoundedCornerShape(30.dp)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            if (!expanded) {
                                expanded = !expanded
                            } else {
                                typeToggle = !typeToggle
                                categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
                                selectedCategory = firstSampleClass
                            }
                        }
                        .height(56.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val image = rememberAsyncImagePainter(
                            if (typeToggle) IconStateForType.fromNumber(expenseType.iconNumber)
                            else IconStateForType.fromNumber(incomeType.iconNumber)
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
                        Modifier,
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

            Spacer(Modifier.height(48.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = expenseValue,
                    onValueChange = { newValue ->
                        val filteredText = newValue.text.filter { it.isDigit() || it == '.' }
                        // Ensure only one decimal point is allowed
                        if (filteredText.count { it == '.' } <= 1) {
                            // Split into parts before and after the decimal
                            val parts = filteredText.split('.')
                            // Ensure max 7 digits before the decimal and max 2 after
                            if (parts.size == 1 && parts[0].length <= 7 || parts.size == 2 && parts[0].length <= 7 && parts[1].length <= 2) {
                                // Update the TextFieldValue with the filtered text
                                expenseValue = newValue.copy(text = filteredText)
                            }
                        }
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
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                        )
                    },
                    visualTransformation = DecimalFilterTransformation(),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                    ),
                    textStyle = typography.displayMedium.copy(
                        textAlign = TextAlign.Center, fontFamily = numberFont
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )
            }
            Spacer(Modifier.height(0.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = comment,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 32) {
                            comment = newValue
                        }
                        scope.launch {
                            uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    },
                    modifier = Modifier,
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Add a comment",
                            style = typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

                    )
            }

            TextButton(
                onClick = {

                },
                Modifier
                    .height(48.dp)
                    .align(Alignment.Start),
                colors = ButtonDefaults.textButtonColors(
//                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
//                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text("Add Tag")
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
//                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
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

        Spacer(Modifier.height(24.dp))
        ErrorRow(errorStatus)
        Spacer(Modifier.height(8.dp))
        Row {
            BottomRow(
                modifier,
                selectedDate,
                { selectedDate = it },
                { selectedPaymentMethod = it },
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding),
        ) {
            FilledTonalButton(
                onClick = {
                    validateTransactionData(
                        closeBottomSheet = closeBottomSheet,
                        selectedType,
                        selectedCategory,
                        expenseValue.text.replace(",", ""),
                        comment.text,
                        selectedDate,
                        selectedPaymentMethod,
                        scope,
                        uiViewModel,
                        transactionsViewModel,
                    )
                }, Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Save")
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}


fun validateTransactionData(
    closeBottomSheet: () -> Unit,
    type: String,
    selectedCategory: CategoryClass,
    amount: String,
    comment: String,
    selectedDate: Long?,
    selectedPaymentMethod: BankAccountsClass,
    scope: CoroutineScope,
    uiViewModel: UiViewModel,
    transactionsViewModel: TransactionsViewModel
) {

    scope.launch {
        if (selectedCategory.categoryName == "Select Category") {
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
            selectedCategory.categoryId,
            amount,
            comment,
            selectedDate,
            selectedPaymentMethod.bankAccountId
        )
        closeBottomSheet()

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRow(
    modifier: Modifier,
    selectedDate: Long?,
    selectedDateSetter: (Long?) -> Unit,
    selectedPaymentMethodSetter: (BankAccountsClass) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.toLocalDate()?.toLongMillis()
    )
    val bankAccountsList by bankAccountsViewModel.allBankAccountList.collectAsState()

    val colorPalletBlue = toPalette(blueColor)
    val scope = rememberCoroutineScope()

    val primaryBankAccount by preferencesViewModel.getPrimaryAccount.collectAsState(
        emptyBank
    )
    var selectedBankAccount by remember {
        mutableStateOf(emptyBank)
    }
    LaunchedEffect(primaryBankAccount) {
        preferencesViewModel.getPrimaryAccount.take(1).collect { data ->
            selectedBankAccount = data
        }
        selectedPaymentMethodSetter(selectedBankAccount)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
    ) {
        Row(modifier = modifier) {

            FilledTonalButton(
                onClick = {
                    showDatePicker = !showDatePicker
                    selectedDateSetter(selectedDate)
                }, colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = secondaryAlphaForElements
                    )
                ), contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
            ) {
                Icon(Icons.Rounded.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = if (selectedDate?.let {
                        convertMillisToDateString(
                            it.toLocalDate().toLongMillis()
                        )
                    } == convertMillisToDateString(
                        System.currentTimeMillis()
                    )) {
                    "Today"
                } else {
                    selectedDate?.let {
                        convertMillisToDateString(it.toLocalDate().toLongMillis())
                    } ?: "Date Error"
                })

            }
        }
        Row(modifier = modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            var bankAccountMenuExpanded by remember { mutableStateOf(false) }
            Box() {
                FilledTonalButton(
                    onClick = {
                        bankAccountMenuExpanded = true
                        selectedPaymentMethodSetter(selectedBankAccount)
                    }, colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = secondaryAlphaForElements
                        )
                    ), contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
                ) {
                    val image = rememberAsyncImagePainter(
                        IconState.fromNumber(selectedBankAccount.cardIconNumber)
                    )
                    Image(
                        painter = image,
                        contentDescription = "Test Image",
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = selectedBankAccount.bankName)
                }

                DropDownMenuForBankAccounts(bankAccountMenuExpanded,
                    colorPalletBlue,
                    onDismiss = { bankAccountMenuExpanded = false },
                    bankAccountsList,
                    selectedBankAccountSetter = {
                        selectedBankAccount = it
                        scope.launch {
                            uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    })
            }
        }
    }

    if (showDatePicker) {
        DatePickerModal(datePickerState, onDateSelected = { date ->
            selectedDateSetter(date)
        }, onDismiss = { showDatePicker = !showDatePicker })
    }
}


//delete this after properly integrating with compose
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBottomSheet(
    date: Long,
    closeBottomSheet: () -> Unit,
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    BoxWithConstraints {
        val contentHeight = constraints.maxHeight.toFloat()
        val contentWidth = constraints.maxWidth.toFloat()
        val windowSizeClass = LocalWindowSize.current
        val localDensity = LocalDensity.current
        val systemKeyboardHeight = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
        val isShowSystemKeyboard =
            systemKeyboardHeight != 0.dp && true
//        val isRequestedShowSystemKeyboard =
//            systemKeyboardHeight != 0.dp || appViewModel.showSystemKeyboard.value
        val internalKeyboardHeight = if (windowSizeClass == WindowWidthSizeClass.Compact) {
            contentWidth
        } else {
            contentWidth / 2f
        }.coerceAtMost(with(localDensity) { 500.dp.toPx() }).coerceAtMost(contentHeight / 2)

        val currentKeyboardHeight = if (isShowSystemKeyboard) {
            with(localDensity) { systemKeyboardHeight.toPx() }
        } else {
            internalKeyboardHeight
        }

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val bottomSheetStateTest = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        val showNewLayout by preferencesViewModel.showForecastBar.collectAsState(false)

        if (true)
            BottomSheetContentAddItem(date, bottomSheetState) {
                scope.launch {
                    closeBottomSheet()
                }
            } else
            BottomSheetContentAddItemTest(
                bottomSheetStateTest,
                localDensity,
                internalKeyboardHeight,
            ) {
                scope.launch {
                    closeBottomSheet()
                }
            }
    }
}

//bottom sheet to show item Details
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentItemDetails(
    sheetState: SheetState,
    singleTransaction: TransactionWithDetails,
    closeBottomSheet: () -> Unit,
    uiViewModel: UiViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(onDismissRequest = {
        closeBottomSheet()
    },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemDetailsContent(
            modifier = Modifier, closeBottomSheet, singleTransaction, uiViewModel
        )
    }
}

val valueArrangement = Arrangement.End
val spaceHeightInDetail = 10.dp


@Composable
fun BottomSheetContentItemDetailsContent(
    modifier: Modifier,
    closeBottomSheet: () -> Unit,
    singleTransaction: TransactionWithDetails,
    uiViewModel: UiViewModel,
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val labelAndValueStyle = typography.bodyMedium
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "₹" + parseAmount(singleTransaction.transaction.amount),
            style = typography.headlineLarge,
            fontFamily = numberFont
        )
//        Spacer(Modifier.height(4.dp))
        Text(
            text = singleTransaction.transaction.note,
            style = typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
            ),
        )
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = combineColors(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant,
                            angle = 0.8f,
                        ), shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
            ) {
                Spacer(Modifier.width(12.dp))
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Type",
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha),
                                style = labelAndValueStyle
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = valueArrangement,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val image = rememberAsyncImagePainter(
                                if (singleTransaction.transaction.type == EXPENSE) R.drawable.icon_expense else R.drawable.icon_income
                            )
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = singleTransaction.transaction.type,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = labelAndValueStyle
                            )
                        }
                    }

                    Spacer(Modifier.height(spaceHeightInDetail))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Category",
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha),
                                style = labelAndValueStyle
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = valueArrangement,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val image = rememberAsyncImagePainter(
                                IconState.fromNumber(singleTransaction.category.categoryIconNumber)
                            )
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = singleTransaction.category.categoryName,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = labelAndValueStyle
                            )
                        }
                    }
                    Spacer(Modifier.height(spaceHeightInDetail))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Date",
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha),
                                style = labelAndValueStyle
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = valueArrangement,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val image = rememberAsyncImagePainter(
                                R.drawable.ic_category_10
                            )
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = convertMillisToDateString(singleTransaction.transaction.dateWithTime),
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = labelAndValueStyle
                            )
                        }
                    }
                    Spacer(Modifier.height(spaceHeightInDetail))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Paid by",
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha),
                                style = labelAndValueStyle
                            )
                        }
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = valueArrangement,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val image = rememberAsyncImagePainter(
                                R.drawable.ic_category_9
                            )
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = singleTransaction.BankAccount.bankName,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = labelAndValueStyle
                            )
                        }
                    }
                }
            }
        }

        Row(
            Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 8.dp),
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextButton(onClick = { }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "edit",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Edit")
                            }
                        }
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            TextButton(
                                onClick = {
                                    showDeleteConfirmation = !showDeleteConfirmation
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),

                                ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "delete",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Delete")
                            }
                        }
                    }

                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(showDeleteConfirmation) {
            ConfirmationAlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                onConfirmation = {
                    scope.launch {
                        transactionsViewModel.deleteSingleTransaction(singleTransaction.transaction)
                        showDeleteConfirmation = false
                        closeBottomSheet()
                    }
                },
                dialogTitle = "Delete Transaction",
                dialogText = "Are you sure, you want to delete this transaction",
                icon = ImageVector.vectorResource(R.drawable.icon_expense)
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetIconPicker(sheetState: SheetState, closeBottomSheet: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime },

        scrimColor = Color.Transparent
    ) {
        BottomSheetContentIconPicker(modifier = Modifier)
    }
}

@Composable
fun BottomSheetContentIconPicker(modifier: Modifier = Modifier) {
    Column(Modifier.heightIn(max = 400.dp)) {
        Text(
            text = "Choose Icon",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        val items = iconsList

        // LazyVerticalGrid with a fixed number of columns (e.g., 2 columns)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 60.dp), // 2 columns
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp) // Optional padding for content
        ) {
            items(items) { item ->
                SingleIcon(item)
            }

        }
    }
}

@Composable
fun SingleIcon(item: Int, uiViewModel: UiViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .size(70.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(50))
            .clickable { scope.launch { uiViewModel.selectedIconFromBottomSheet.emit(item) } },
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            val image = rememberAsyncImagePainter(IconState.fromNumber(item))
            Image(
                painter = image, contentDescription = "Test Image", Modifier.size(32.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodBottomSheet(
    bankAccountsClass: BankAccountsClass, uiViewModel: UiViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val bottomSheet by uiViewModel.paymentMethodBottomSheetStatus.collectAsState(false)
    val bankAccountsClass by remember { mutableStateOf(bankAccountsClass) }
    var isEdit by remember { mutableStateOf(false) }
    if (bankAccountsClass.bankAccountId != 0L) isEdit = true

    if (bottomSheet) {
        ModalBottomSheet(onDismissRequest = {
            scope.launch {
                uiViewModel.paymentMethodBottomSheetStatus.emit(
                    !bottomSheet
                )
            }
        },
            sheetState = bottomSheetState,
            modifier = Modifier
                .imePadding()
                .fillMaxWidth(),
            contentWindowInsets = { WindowInsets.ime }) {

            BottomSheetContentPaymentMethodAddContent(
                modifier = Modifier, isEdit, bankAccountsClass
            )

        }

    }
}

val horizontalPadding = 16.dp
val verticalPadding = 0.dp

@Composable
fun BottomSheetContentPaymentMethodAddContent(
    modifier: Modifier,
    isEdit: Boolean,
    bankAccountsClass: BankAccountsClass,
    uiViewModel: UiViewModel = hiltViewModel(),
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel()
) {
    val showError by uiViewModel.errorStatusInBankAccountAdd.collectAsState()
    var bankAmount by remember { mutableStateOf(bankAccountsClass.initialAmount.toString()) }
    var bankName by remember { mutableStateOf(bankAccountsClass.bankName) }

    var selectedColor by remember { mutableIntStateOf(bankAccountsClass.cardColorNumber) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val title = if (isEdit) "Edit Bank Account" else "Add Bank Account"

    Column() {
        Text(
            text = title,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(16.dp))
        LivePaymentCard(color = ColorState.fromNumber(selectedColor)!!,
            bankAccountsClass,
            sendBankAmount = { bankAmount = it },
            sendBankName = { bankName = it })
        Text(
            text = "Enter total Amount present in bank along with bank name in designated field",
            style = typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha),
            modifier = Modifier.padding(16.dp, 8.dp)
        )
        Spacer(Modifier.height(8.dp))

//        Text(
//            text = "Colors", style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.onSurface,
//        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            for (i in colorList) {
                SingleColorButton(i, selectedColor) { selectedColor = it }
            }
        }
//        if (false) {
//            Spacer(Modifier.height(16.dp))
//            Text(
//                text = "Amount",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//
//            Text(
//                text = "Enter total Amount present in bank account",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
//            )
//            TextField(
//                value = bankAmount,
//                onValueChange = { newText ->
//                    bankAmount = newText
//                },
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(0.dp, 8.dp),
//                placeholder = {
//                    Text(
//                        "Amount",
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
//                    )
//                },
//                leadingIcon = { Text("₹", style = MaterialTheme.typography.bodyLarge) },
//                shape = RoundedCornerShape(24.dp),
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                // visualTransformation = NumberCommaTransformation()
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                text = "Bank Name",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Text(
//                text = "Enter the name of your bank",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
//            )
//            TextField(
//                value = bankAmount,
//                onValueChange = { newText ->
//                    bankAmount = newText
//                },
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(0.dp, 8.dp),
//                placeholder = {
//                    Text(
//                        "Bank Name",
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
//                    )
//                },
//                leadingIcon = {
//                    Image(
//                        painter = painterResource(IconState.fromNumber(24)!!),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp)
//                    )
//                },
//                shape = RoundedCornerShape(24.dp),
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                // visualTransformation = NumberCommaTransformation()
//            )
//
//        }
        Spacer(Modifier.height(8.dp))
        ErrorRow(showError)
        Spacer(Modifier.height(16.dp))



        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontalPadding, verticalPadding),
            onClick = {
                scope.launch {
                    if (bankAmount.isEmpty()) {
                        uiViewModel.errorStatusMessage.emit("Please enter bank amount")
                        uiViewModel.errorStatusInBankAccountAdd.emit(true)
                    }
                    if (bankName.isEmpty()) {
                        uiViewModel.errorStatusMessage.emit("Please enter bank name")
                        uiViewModel.errorStatusInBankAccountAdd.emit(true)
                    }
                    if (!bankName.isEmpty() && !bankAmount.isEmpty()) {
                        if (isEdit) showConfirmationDialog = isEdit
                        else scope.launch {
                            bankAccountsViewModel.createObjectAndStoreIt(
                                bankAccountsClass.bankAccountId, bankAmount, bankName, selectedColor
                            )
                            uiViewModel.paymentMethodBottomSheetStatus.emit(false)
                        }
                    }
                }
            },
        ) {
            Text(text = "Save")
        }
        if (showConfirmationDialog) {
            ConfirmationAlertDialog(
                onDismissRequest = { showConfirmationDialog = !showConfirmationDialog },
                onConfirmation = {
                    scope.launch {
                        bankAccountsViewModel.createObjectAndStoreIt(
                            bankAccountsClass.bankAccountId, bankAmount, bankName, selectedColor
                        )
                        uiViewModel.paymentMethodBottomSheetStatus.emit(false)
                    }
                },
                dialogTitle = "Change Bank Details",
                dialogText = "Are you sure, that you want to change current bank details",
                icon = ImageVector.vectorResource(R.drawable.icon_income)
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun ErrorRow(showError: Boolean, uiViewModel: UiViewModel = hiltViewModel()) {
    val errorMessage by uiViewModel.errorStatusMessage.collectAsState()
    AnimatedVisibility(showError) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier.padding(16.dp, 0.dp), colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 12.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Start,
                        style = typography.bodyMedium
                    )
                }
            }
        }

    }
}

@Composable
fun SingleColorButton(color: Int, selectedColor: Int, setColor: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .then(
                if (selectedColor == color) {
                    Modifier.border(
                        3.dp, ColorState.fromNumber(color)!!, shape = RoundedCornerShape(50)
                    )
                } else Modifier
            )
            .zIndex(-1f)
    ) {
        Surface(shape = RoundedCornerShape(50),
            modifier = Modifier
                .size(48.dp)
                .fillMaxSize()
                .padding(6.dp),
            color = ColorState.fromNumber(color)!!,
            onClick = { setColor(color) }) {}
    }
}


@Preview
@Composable
private fun BottomSheetContentItemDetailsPreview() {
    ExpenseTrackerTheme {
        Surface {
            // BottomSheetContentItemDetailsContent(Modifier, singleTransaction)
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentAddItemTest(
    sheetState: SheetState,
    localDensity: Density,
    keyboardHeight: Float,
    closeBottomSheet: () -> Unit
) {

    LaunchedEffect(true) {
        sheetState.show()
    }
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomSheetContentItemAddContentTest(modifier = Modifier, localDensity, keyboardHeight)
    }
}

val LocalWindowInsets = compositionLocalOf { PaddingValues(0.dp) }
val LocalWindowSize = compositionLocalOf { WindowWidthSizeClass.Compact }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemAddContentTest(
    modifier: Modifier,
    localDensity: Density,
    keyboardHeight: Float,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
    animationViewModel: AnimationViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    val categoryList by categoryViewModel.categoryList.collectAsState()
    val showForecast by preferencesViewModel.showForecastBar.collectAsState(false)
    val firstSampleClass = firstSampleClass
    var selectedCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }

    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)


    val errorStatus by uiViewModel.errorStatusInAddBottomSheet.collectAsState(false)


    //animations
    var expanded by remember { mutableStateOf(false) }
    var typeToggle by remember { mutableStateOf(true) }
    val boxSize by animateDpAsState(
        targetValue = if (expanded) 160.dp else 56.dp, animationSpec = tween(500)
    )
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
    val budget by preferencesViewModel.getTotalAmountPerDay.collectAsState(1f)
    val oldAmount by transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()
    val newAmountTemp = if (expenseValue.text.isEmpty()) 0L else extractNumbers(expenseValue.text)
    val newDailyBudget = oldAmount + newAmountTemp
    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget
    } else {
        0f
    }
    animationViewModel.method("₹$amountInString", percent)
    Log.d("Hello", percent.toString())

    LaunchedEffect(percent) {
        scope.launch {
            animationViewModel.newSpentPercentage.emit(percent)
        }
    }

    val imeHeight = WindowInsets.ime.getBottom(Density(LocalContext.current))

    // Check if the keyboard is open (i.e., imeHeight > 0)
    val isKeyboardVisible = imeHeight > 0
    //val isKeyboardVisible by remember { mutableStateOf(height != 0) }
    val localDensity = LocalDensity.current
    val windowSizeClass = LocalWindowSize.current
    val windowInsets = LocalWindowInsets.current

    val keyboardAdditionalOffset =
        windowInsets.calculateBottomPadding().minus(16.dp).coerceAtLeast(0.dp)


    Column(
        modifier.fillMaxWidth()
    ) {
        Column(

        ) {
            Text(
                text = "Add Transaction",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
//            AnimatedVisibility(showForecast) {
//                Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {
//                    RestBudgetPill(LocalDate.now().toLong())
//                }
//            }
//            if (showForecast) {
//                Spacer(Modifier.height(16.dp))
//            }
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(boxSize)
                        .background(
                            color = colorAnimate, shape = RoundedCornerShape(30.dp)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            if (!expanded) {
                                expanded = !expanded
                            } else {
                                typeToggle = !typeToggle
                                categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
                                selectedCategory = firstSampleClass
                            }
                        }
                        .height(56.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val image = rememberAsyncImagePainter(
                            if (typeToggle) IconStateForType.fromNumber(expenseType.iconNumber)
                            else IconStateForType.fromNumber(incomeType.iconNumber)
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
                        Modifier,
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

                Spacer(Modifier.width(12.dp))

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(boxSize)
                        .background(
                            color = colorAnimate, shape = RoundedCornerShape(30.dp)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            if (!expanded) {
                                expanded = !expanded
                            } else {
                                typeToggle = !typeToggle
                                categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
                                selectedCategory = firstSampleClass
                            }
                        }
                        .height(56.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val image = rememberAsyncImagePainter(
                            if (typeToggle) IconStateForType.fromNumber(expenseType.iconNumber)
                            else IconStateForType.fromNumber(incomeType.iconNumber)
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
                        expenseValue = TextFieldValue(
                            extractNumbers(newValue.text).toString(),
                            selection = TextRange(extractNumbers(newValue.text).toString().length)
                        )
                        scope.launch {
                            uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "₹0",
                            style = typography.displayMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                    ),
                    textStyle = typography.displayMedium.copy(
                        textAlign = TextAlign.Center, fontFamily = numberFont
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )
            }
            Spacer(Modifier.height(12.dp))
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
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
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
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottomSheetStartEndPadding, 0.dp)
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
                            "Add Tags",
                            style = typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
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
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }


            Spacer(Modifier.height(16.dp))
            Row {
                BottomRowTest(modifier, selectedDate, { selectedDate = it }, buttonClicked = {
//                    validateTransactionData(
//                        selectedType,
//                        selectedCategory,
//                        expenseValue.text.replace(",", ""),
//                        comment.text,
//                        selectedDate,
//                        selectedPaymentMethod =,
//                        scope,
//                        uiViewModel,
//                        transactionsViewModel,
//                    )
                })
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = !isKeyboardVisible,
                enter = fadeIn(
                    tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                    )
                ) + slideInVertically(
                    tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                    )
                ) { with(localDensity) { 10.dp.toPx().toInt() } },
                exit = fadeOut(
                    tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                    )
                ) + slideOutVertically(
                    tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                    )
                ) { with(localDensity) { 10.dp.toPx().toInt() } },
            ) {

                KeyBoard(
                    modifier = Modifier
                        .height(with(localDensity) { keyboardHeight.toDp() + 50.dp })
                        .fillMaxWidth()
                )

            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRowTest(
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
                        alpha = secondaryAlphaForElements
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
                        "HEllo"
                    } ?: "Date Error"
                })

            }


        }
    }
    if (showDatePicker) {
        DatePickerModal(datePickerState, onDateSelected = { date ->
            selectedDateSetter(date)
        }, onDismiss = { showDatePicker = !showDatePicker })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetBottomSheet(
    sheetState: SheetState,
    bottomSheetDismissed: () -> Unit,
    amount: TextFieldValue,
    isBudgetSet: Boolean,
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    budgetDayViewModel: BudgetDayViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
) {
    val modifier = Modifier.padding(16.dp, 0.dp)
    var totalAmountText by remember { mutableStateOf(TextFieldValue("")) }
    var totalAmountPerDay by remember { mutableFloatStateOf(0f) }
    var totalDaysRemaining = remember {
        getRemainingDaysInCurrentMonth()
    }


    var shouldShowConfirmation by remember { mutableStateOf(false) }
    val errorStatus by uiViewModel.errorStatusInBudgetAdd.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    ModalBottomSheet(onDismissRequest = { bottomSheetDismissed() }, sheetState = sheetState) {
        BottomSheetBudgetContent(modifier, totalAmountText, amountTextValueChange = {
            totalAmountText = it
        }, totalAmountPerDay, { totalAmountPerDay = it }, totalDaysRemaining, {
            if (isBudgetSet) {
                shouldShowConfirmation = true
            } else {
                SaveBudgetDetailsInDatabase(
                    transactionsViewModel,
                    budgetDayViewModel,
                    budgetViewModel,
                    totalAmountText.text,
                    totalAmountPerDay,
                    totalDaysRemaining,
                    getCurrentMonthName(),
                    getCurrentDate(),
                    getMonthEndDate(),
                    bottomSheetDismissed,
                    keyboardController,
                    focusManager
                )
            }
        }, errorStatus, uiViewModel)
    }
    if (shouldShowConfirmation) {
        ConfirmationAlertDialog(
            { shouldShowConfirmation = false },
            {
                SaveBudgetDetailsInDatabase(
                    transactionsViewModel,
                    budgetDayViewModel,
                    budgetViewModel,
                    totalAmountText.text,
                    totalAmountPerDay,
                    totalDaysRemaining,
                    getCurrentMonthName(),
                    getCurrentDate(),
                    getMonthEndDate(),
                    bottomSheetDismissed,
                    keyboardController,
                    focusManager
                )
            },
            "Change Budget",
            "Are you sure, you want to change the current budget?",
            ImageVector.vectorResource(R.drawable.icon_expense)
        )
    }
}

fun SaveBudgetDetailsInDatabase(
    transactionsViewModel: TransactionsViewModel,
    budgetDayViewModel: BudgetDayViewModel,
    budgetViewModel: BudgetViewModel,
    totalAmountForMonth: String,
    totalAmountPerDay: Float,
    totalDaysRemaining: Long,
    currentMonthName: String,
    budgetMonthStartDate: Long,
    budgetMonthEndDate: Long,
    bottomSheetDismissed: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {
    budgetViewModel.createObjectAndStoreIt(
        totalAmountForMonth.toFloat(),
        totalAmountPerDay,
        totalDaysRemaining,
        currentMonthName,
        budgetMonthStartDate,
        budgetMonthEndDate,
        getValidDatesListFromLong(budgetMonthStartDate, budgetMonthEndDate)
    )

    focusManager.clearFocus()
    keyboardController?.hide()
    bottomSheetDismissed()

}


@Composable
fun BottomSheetBudgetContent(
    modifier: Modifier,
    dailySpendLimit: TextFieldValue,
    amountTextValueChange: (TextFieldValue) -> Unit,
    totalAmountPerDay: Float,
    totalAmountPerDayValueChange: (Float) -> Unit,
    totalDaysRemaining: Long,
    saveDailySpendLimit: () -> Unit,
    errorStatus: Boolean,
    uiViewModel: UiViewModel
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(dailySpendLimit) {
        focusRequester.requestFocus()
        if (dailySpendLimit.text.isNotEmpty() && dailySpendLimit.text.toFloat() != 0f) {
            totalAmountPerDayValueChange(dailySpendLimit.text.toFloat() / totalDaysRemaining)
        } else {
            totalAmountPerDayValueChange(0f)
        }
    }
    Column(modifier.fillMaxWidth()) {
        Text(
            text = "Set up a budget",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Setup your budget for current month ",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(12.dp))
        ErrorRow(errorStatus)
        Spacer(Modifier.height(32.dp))
        Row {
            TextField(
                value = dailySpendLimit,
                onValueChange = { newValue ->
                    val filteredText = newValue.text.filter { it.isDigit() || it == '.' }
                    // Ensure only one decimal point is allowed
                    if (filteredText.count { it == '.' } <= 1) {
                        // Split into parts before and after the decimal
                        val parts = filteredText.split('.')
                        // Ensure max 7 digits before the decimal and max 2 after
                        if (parts.size == 1 && parts[0].length <= 7 || parts.size == 2 && parts[0].length <= 7 && parts[1].length <= 2) {
                            // Update the TextFieldValue with the filtered text
                            amountTextValueChange(newValue.copy(text = filteredText))
                        }
                    }
                    scope.launch {
                        uiViewModel.errorStatusInBudgetAdd.emit(false)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .focusRequester(focusRequester),
                singleLine = true,

                placeholder = {
                    Text(
                        "₹0",
                        style = typography.displayMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
                    )
                },
                visualTransformation = DecimalFilterTransformation(),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
                textStyle = typography.displayMedium.copy(
                    textAlign = TextAlign.Center, fontFamily = numberFont
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
            )
        }
        Spacer(Modifier.height(64.dp))
        Row {
            Text(
                text = "Current Month",
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = getCurrentMonthName(),
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(Modifier.height(16.dp))
        Row {
            Text(
                text = "Total Days ",
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${totalDaysRemaining} Days remaining",
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Total",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "$totalAmountPerDay Per day",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(16.dp))
        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (dailySpendLimit.text.isEmpty()) {
                    scope.launch {
                        uiViewModel.errorStatusMessage.emit(
                            "Please enter amount"
                        )
                        uiViewModel.errorStatusInBudgetAdd.emit(
                            true
                        )
                    }
                } else if (dailySpendLimit.text.toFloat() == 0f) {
                    scope.launch {
                        uiViewModel.errorStatusMessage.emit(
                            "Please enter amount"
                        )
                        uiViewModel.errorStatusInBudgetAdd.emit(
                            true
                        )
                    }
                } else {
                    saveDailySpendLimit()
                }
            },
        ) {
            Text(text = "Create Budget")
        }
    }
}

@Preview
@Composable
fun BottomSheetBudgetContentPreview() {
    ExpenseTrackerTheme {
        Surface {
            // BottomSheetBudgetContent(Modifier, TextFieldValue("0"), {}, {})
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    ExpenseTrackerTheme {
        Surface {
            BottomSheetContentItemAddContent(Modifier, 0L, {})
        }
    }
}
