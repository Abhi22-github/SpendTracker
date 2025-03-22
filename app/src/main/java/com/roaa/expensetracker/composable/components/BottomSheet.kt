package com.roaa.expensetracker.composable.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.CustomFonts.numberFont
import com.roaa.expensetracker.composable.blueColor
import com.roaa.expensetracker.composable.greenColor
import com.roaa.expensetracker.composable.infoColor
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.screens.LivePaymentCard
import com.roaa.expensetracker.composable.screens.PaymentCard
import com.roaa.expensetracker.composable.screens.TitleWithCheckBox
import com.roaa.expensetracker.composable.screens.ValueLabelList
import com.roaa.expensetracker.composable.secondaryAlpha
import com.roaa.expensetracker.composable.secondaryAlphaForElements
import com.roaa.expensetracker.composable.successColor
import com.roaa.expensetracker.composable.utils.ColorState
import com.roaa.expensetracker.composable.utils.DistributionMethod
import com.roaa.expensetracker.composable.utils.IconState
import com.roaa.expensetracker.composable.utils.IconStateForType
import com.roaa.expensetracker.composable.utils.colorList
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.composable.utils.distributionChoiceList
import com.roaa.expensetracker.composable.utils.iconsList
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.model.uiDataModels.InfoStatClass
import com.roaa.expensetracker.model.uiDataModels.TransactionTypeClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.Constants.INSERT
import com.roaa.expensetracker.utilities.Constants.UPDATE
import com.roaa.expensetracker.utilities.DecimalFilterTransformation
import com.roaa.expensetracker.utilities.ErrorManager
import com.roaa.expensetracker.utilities.convertMillisToDateString
import com.roaa.expensetracker.utilities.extractNumbers
import com.roaa.expensetracker.utilities.getDayDifference
import com.roaa.expensetracker.utilities.getMonthEndDate
import com.roaa.expensetracker.utilities.getMonthStartDate
import com.roaa.expensetracker.utilities.parseAmountWithPrecision
import com.roaa.expensetracker.utilities.toDisplayStringForMonthWithYear
import com.roaa.expensetracker.utilities.toLocalDate
import com.roaa.expensetracker.utilities.toLongMillis
import com.roaa.expensetracker.utilities.utilityModalClass.defaultBank
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCategoryClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyInfoStat
import com.roaa.expensetracker.utilities.utilityModalClass.emptyTransactionClass
import com.roaa.expensetracker.utilities.utilityModalClass.firstSampleClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentAddItem(
    date: Long, sheetState: SheetState, viewModel: AllViewModel, closeBottomSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemAddContent(
            modifier = Modifier, viewModel = viewModel, date, closeBottomSheet
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentEdit(
    singleTransaction: TransactionWithDetails,
    sheetState: SheetState,
    viewModel: AllViewModel,
    closeBottomSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemEditContent(
            modifier = Modifier, viewModel = viewModel, singleTransaction, closeBottomSheet
        )
    }
}


val bottomSheetStartEndPadding = 16.dp
val bottomSheetTopBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemAddContent(
    modifier: Modifier,
    viewModel: AllViewModel,
    date: Long,
    closeBottomSheet: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(date) }
    val focusRequester = remember { FocusRequester() }
    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)
    var selectedType by remember { mutableStateOf(expenseType.type) }
    val bankAccountsList by viewModel.bankAccountsViewModel.allBankAccountList.collectAsState()
    var selectedPaymentMethod by remember { mutableStateOf<BankAccountsClass>(defaultBank) }

    val isAddFromSpecificCategoryOrBank by viewModel.uiViewModel.addCategorySpecificOrBankSpecificTransaction.collectAsState()
    val specificBankAccount by viewModel.uiViewModel.addSpecificBankForTransaction.collectAsState()
    val specificCategory by viewModel.uiViewModel.addSpecificCategoryForTransaction.collectAsState()

    val categoryList by viewModel.categoryViewModel.categoryList.collectAsState()
    val lastSelectedBank by viewModel.preferencesViewModel.getLastUsedBank.collectAsState(0L)
    val lastExpenseCategoryId by viewModel.preferencesViewModel.getLastExpenseCategory.collectAsState(
        0L
    )
    val lastIncomeCategoryId by viewModel.preferencesViewModel.getLastIncomeCategory.collectAsState(
        0L
    )
    val isDefaultCategorySet by viewModel.preferencesViewModel.getPreDefaultCategoryStatus.collectAsState(
        false
    )
    var selectedCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }
    var typeToggle by remember { mutableStateOf(true) }
    LaunchedEffect(
        lastExpenseCategoryId,
        lastIncomeCategoryId,
        categoryList,
        selectedType,
        isAddFromSpecificCategoryOrBank,
        specificBankAccount,
        specificCategory,
        lastSelectedBank
    ) {
        if (isAddFromSpecificCategoryOrBank) {
            if (specificBankAccount.bankAccountId != 0L) {
                if (bankAccountsList.isNotEmpty()) {
                    val bankAccountPresent =
                        bankAccountsList.any { it.bankAccountId == specificBankAccount.bankAccountId }
                    if (bankAccountPresent) {
                        selectedPaymentMethod =
                            bankAccountsList.first { it.bankAccountId == specificBankAccount.bankAccountId }
                    }
                }
            }
            if (specificCategory.categoryId != 0L) {
                if (categoryList.isNotEmpty()) {
                    if (specificCategory.categoryType == expenseType.type) {
                        val expenseCategoryPresent =
                            categoryList.any { it.categoryId == specificCategory.categoryId }
                        if (expenseCategoryPresent) {
                            selectedCategory =
                                categoryList.first { it.categoryId == specificCategory.categoryId }
                        }
                        typeToggle = true
                    } else {
                        val incomeCategoryPresent =
                            categoryList.any { it.categoryId == specificCategory.categoryId }
                        if (incomeCategoryPresent) {
                            selectedCategory =
                                categoryList.first { it.categoryId == specificCategory.categoryId }
                        }
                        typeToggle = false
                    }
                }
            }
            viewModel.categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
        } else {
            if (categoryList.isNotEmpty()) {
                if (selectedType == expenseType.type) {
                    val expenseCategoryPresent =
                        categoryList.any { it.categoryId == lastExpenseCategoryId }
                    if (expenseCategoryPresent) {
                        selectedCategory =
                            categoryList.first { it.categoryId == lastExpenseCategoryId }
                    }
                } else {
                    val incomeCategoryPresent =
                        categoryList.any { it.categoryId == lastIncomeCategoryId }
                    if (incomeCategoryPresent) {
                        selectedCategory =
                            categoryList.first { it.categoryId == lastIncomeCategoryId }
                    }
                }
            }
        }

        val bankPresent = bankAccountsList.any { it.bankAccountId == lastSelectedBank }
        if (bankPresent) {
            selectedPaymentMethod = bankAccountsList.first { it.bankAccountId == lastSelectedBank }

        }
    }

    val errorStatus by viewModel.uiViewModel.errorStatusInAddBottomSheet.collectAsState(false)


    //animations
    val colorAnimate by animateColorAsState(
        targetValue = if (typeToggle) orange.copy(alpha = .20f) else successColor.copy(
            alpha = 0.20f
        ), animationSpec = tween(500)
    )

    if (typeToggle) {
        selectedType = expenseType.type
    } else {
        selectedType = incomeType.type
    }

    // Request focus once when the composable is first composed
    LaunchedEffect(Unit) {
        // Request focus for the TextField
        focusRequester.requestFocus()
        viewModel.categoryViewModel.getCorrespondingList(selectedType)
        viewModel.categoryViewModel.getOnlyExpenseCategoryNames()
        viewModel.categoryViewModel.getOnlyIncomeCategoryNames()
    }

//    LaunchedEffect( typeToggle) {
//        if (expanded) {
//            delay(5000)
//            expanded = false
//        }
//    }
    val budget by viewModel.preferencesViewModel.getTotalAmountPerDay.collectAsState(1f)
    val oldAmount by viewModel.transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()
    val newAmountTemp = if (expenseValue.text.isEmpty()) 0L else extractNumbers(expenseValue.text)
    val newDailyBudget = oldAmount + BigDecimal(newAmountTemp)
    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget.toBigDecimal()
    } else {
        BigDecimal.ZERO
    }
    viewModel.animationViewModel.method(
        "${LocalCurrency.current.currencySymbol}$amountInString", percent.toFloat()
    )

    LaunchedEffect(percent) {
        scope.launch {
            viewModel.animationViewModel.newSpentPercentage.emit(percent.toFloat())
        }
    }

    Column(
        modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Add Transaction",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .background(
                            color = colorAnimate, shape = RoundedCornerShape(30.dp)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            typeToggle = !typeToggle
                            viewModel.categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
                            selectedCategory = firstSampleClass
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

                        Text(
                            text = if (typeToggle) expenseType.type else incomeType.type,
                            modifier = Modifier.padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                        Row {
                            Row(Modifier.weight(0.8f)) {
                                val image = rememberAsyncImagePainter(
                                    IconState.fromNumber(
                                        selectedCategory.categoryIconNumber
                                    )
                                )
                                AnimatedContent(image) {
                                    Image(
                                        painter = it,
                                        contentDescription = "Test Image",
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                                AnimatedContent(selectedCategory.categoryName) {
                                    Text(
                                        text = it,
                                        modifier = Modifier
                                            .weight(0.6f)
                                            .padding(start = 8.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Icon(
                                Icons.Filled.KeyboardArrowDown,
                                "backIcon",
                                modifier = Modifier.weight(0.2f)
                            )
                        }
                    }
                    DropDownMenu(
                        Modifier,
                        categoryMenuExpanded,
                        selectedCategory,
                        colorPalletGreen,
                        onDismiss = { categoryMenuExpanded = false },
                        categoryList,
                        selectedCategorySetter = {
                            selectedCategory = it
                            scope.launch {
                                viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .focusRequester(focusRequester),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "${LocalCurrency.current.currencySymbol}0",
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
                    .align(Alignment.CenterHorizontally)
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = comment,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 32) {
                            comment = newValue
                        }
                        scope.launch {
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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

//            TextButton(
//                onClick = {
//
//                },
//                Modifier
//                    .height(48.dp)
//                    .align(Alignment.Start),
//                colors = ButtonDefaults.textButtonColors(
////                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
////                    contentColor = MaterialTheme.colorScheme.onSurface
//                )
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = null)
//                Text("Add Tag")
//            }


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
                viewModel,
                selectedDate,
                { selectedDate = it },
                bankAccountsList,
                selectedPaymentMethod,
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
                        TransactionWithDetails(
                            emptyTransactionClass, defaultCategoryClass, defaultBank
                        ),
                        INSERT,
                        closeBottomSheet = closeBottomSheet,
                        selectedType,
                        selectedCategory,
                        expenseValue.text.replace(",", ""),
                        comment.text,
                        selectedDate,
                        selectedPaymentMethod,
                        scope,
                        isDefaultCategorySet,
                        viewModel
                    )
                },
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text("Save")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemEditContent(
    modifier: Modifier,
    viewModel: AllViewModel,
    singleTransaction: TransactionWithDetails,
    closeBottomSheet: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue(singleTransaction.transaction.amount.toString())) }
    var comment by remember { mutableStateOf(TextFieldValue(singleTransaction.transaction.note)) }
    var selectedDate by remember { mutableStateOf<Long?>(singleTransaction.transaction.date) }
    var selectedPaymentMethod by remember { mutableStateOf<BankAccountsClass>(singleTransaction.BankAccount) }
    val focusRequester = remember { FocusRequester() }
    val categoryList by viewModel.categoryViewModel.categoryList.collectAsState()
    var selectedCategory by remember {
        mutableStateOf(
            singleTransaction.category
        )
    }
    val bankAccountsList by viewModel.bankAccountsViewModel.allBankAccountList.collectAsState()

    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)


    val errorStatus by viewModel.uiViewModel.errorStatusInAddBottomSheet.collectAsState(false)


    //animations
    var expanded by remember { mutableStateOf(false) }
    var typeToggle by remember { mutableStateOf(singleTransaction.transaction.type == EXPENSE) }
    val boxSize by animateDpAsState(
        targetValue = if (expanded) 160.dp else 56.dp, animationSpec = tween(500)
    )
    val colorAnimate by animateColorAsState(
        targetValue = if (typeToggle) orange.copy(alpha = .20f) else successColor.copy(
            alpha = 0.20f
        ), animationSpec = tween(500)
    )


    var selectedType by remember { mutableStateOf(singleTransaction.transaction.type) }

    if (typeToggle) {
        selectedType = expenseType.type
    } else {
        selectedType = incomeType.type
    }

    // Request focus once when the composable is first composed
    LaunchedEffect(Unit) {
        // Request focus for the TextField
        focusRequester.requestFocus()
        viewModel.categoryViewModel.getCorrespondingList(selectedType)
        viewModel.categoryViewModel.getOnlyExpenseCategoryNames()
        viewModel.categoryViewModel.getOnlyIncomeCategoryNames()
    }

    LaunchedEffect(expanded, typeToggle) {
        if (expanded) {
            delay(5000)
            expanded = false
        }
    }
    val budget by viewModel.preferencesViewModel.getTotalAmountPerDay.collectAsState(1f)
    val oldAmount by viewModel.transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()
    val newAmountTemp = if (expenseValue.text.isEmpty()) 0L else extractNumbers(expenseValue.text)
    val newDailyBudget = oldAmount + BigDecimal(newAmountTemp)
    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget.toBigDecimal()
    } else {
        BigDecimal.ZERO
    }
    viewModel.animationViewModel.method(
        "${LocalCurrency.current.currencySymbol}$amountInString", percent.toFloat()
    )

    LaunchedEffect(percent) {
        scope.launch {
            viewModel.animationViewModel.newSpentPercentage.emit(percent.toFloat())
        }
    }

    Column(
        modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Edit Transaction",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(
                    contentAlignment = Alignment.Center,
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
                                viewModel.categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
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
                        selectedCategory,
                        colorPalletGreen,
                        onDismiss = { categoryMenuExpanded = false },
                        categoryList,
                        selectedCategorySetter = {
                            selectedCategory = it
                            scope.launch {
                                viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .focusRequester(focusRequester),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "${LocalCurrency.current.currencySymbol}0",
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
                    .align(Alignment.CenterHorizontally)
                    .padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
            ) {
                TextField(
                    value = comment,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 32) {
                            comment = newValue
                        }
                        scope.launch {
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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
//            TextButton(
//                onClick = {
//
//                },
//                Modifier
//                    .height(48.dp)
//                    .align(Alignment.Start),
//                colors = ButtonDefaults.textButtonColors(
////                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
////                    contentColor = MaterialTheme.colorScheme.onSurface
//                )
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = null)
//                Text("Add Tag")
//            }


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
                viewModel,
                selectedDate,
                { selectedDate = it },
                bankAccountsList,
                selectedPaymentMethod,
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
                        singleTransaction,
                        UPDATE,
                        closeBottomSheet = closeBottomSheet,
                        selectedType,
                        selectedCategory,
                        expenseValue.text.replace(",", ""),
                        comment.text,
                        selectedDate,
                        selectedPaymentMethod,
                        scope,
                        false,
                        viewModel,
                    )
                },
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text("Save")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}


fun validateTransactionData(
    singleTransaction: TransactionWithDetails,
    actionType: String,
    closeBottomSheet: () -> Unit,
    type: String,
    selectedCategory: CategoryClass,
    amount: String,
    comment: String,
    selectedDate: Long?,
    selectedPaymentMethod: BankAccountsClass,
    scope: CoroutineScope,
    isDefaultCategorySet: Boolean,
    viewModel: AllViewModel,
) {

    scope.launch {
        if (selectedCategory.categoryName == "Select Category") {
            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(true)
            viewModel.uiViewModel.setErrorMessage("Please select a category")
            return@launch
        }
        if (amount == "0" || amount.isEmpty()) {
            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(true)
            viewModel.uiViewModel.setErrorMessage("Please enter amount")
            return@launch
        }
        if (comment.isEmpty()) {
            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(true)
            viewModel.uiViewModel.setErrorMessage("Please provide some comment")
            return@launch
        }
        if (actionType == INSERT) viewModel.transactionsViewModel.validateAndPrepareTransactionData(
            type,
            selectedCategory.categoryId,
            amount,
            comment,
            selectedDate,
            selectedPaymentMethod.bankAccountId
        ) else viewModel.transactionsViewModel.updateFormDataInDatabase(singleTransaction.transaction.also {
            it.type = type
            selectedDate?.let { date -> it.date = date }
            it.amount = amount.toBigDecimal()
            it.note = comment
            it.categoryId = selectedCategory.categoryId
            it.bankAccountId = selectedPaymentMethod.bankAccountId
        })
        if (!isDefaultCategorySet) {
            if (type == EXPENSE) {
                viewModel.preferencesViewModel.setLastUsedExpenseCategoryId(selectedCategory.categoryId)
            } else {
                viewModel.preferencesViewModel.setLastUsedIncomeCategoryId(selectedCategory.categoryId)
            }
        }
        viewModel.preferencesViewModel.setLastUsedBankId(selectedPaymentMethod.bankAccountId)

        closeBottomSheet()

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRow(
    modifier: Modifier,
    viewModel: AllViewModel,
    selectedDate: Long?,
    selectedDateSetter: (Long?) -> Unit,
    bankAccountsList: List<BankAccountsClass>,
    selectedBankAccount: BankAccountsClass,
    selectedPaymentMethodSetter: (BankAccountsClass) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.toLocalDate()?.toLongMillis()
    )
    val colorPalletBlue = toPalette(blueColor)
    val scope = rememberCoroutineScope()

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
            Box {
                FilledTonalButton(
                    onClick = {
                        //selectedPaymentMethodSetter(selectedBankAccount)
                        bankAccountMenuExpanded = true
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

                DropDownMenuForBankAccounts(
                    bankAccountMenuExpanded,
                    selectedBankAccount,
                    colorPalletBlue,
                    onDismiss = { bankAccountMenuExpanded = false },
                    bankAccountsList,
                    selectedBankAccountSetter = {
                        // selectedBankAccount = it
                        selectedPaymentMethodSetter(it)
                        scope.launch {
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBottomSheet(
    date: Long,
    viewModel: AllViewModel,
    closeBottomSheet: () -> Unit,
) {
    BoxWithConstraints {
        val contentHeight = constraints.maxHeight.toFloat()
        val contentWidth = constraints.maxWidth.toFloat()
        val windowSizeClass = LocalWindowSize.current
        val localDensity = LocalDensity.current
        val systemKeyboardHeight = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
        systemKeyboardHeight != 0.dp && true
//        val isRequestedShowSystemKeyboard =
//            systemKeyboardHeight != 0.dp || appViewModel.showSystemKeyboard.value
        val internalKeyboardHeight = if (windowSizeClass == WindowWidthSizeClass.Compact) {
            contentWidth
        } else {
            contentWidth / 2f
        }.coerceAtMost(with(localDensity) { 500.dp.toPx() }).coerceAtMost(contentHeight / 2)

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val bottomSheetStateTest = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        if (true) BottomSheetContentAddItem(date, bottomSheetState, viewModel) {
            scope.launch {
                closeBottomSheet()
            }
        } else BottomSheetContentAddItemTest(
            bottomSheetStateTest,
            viewModel,
            localDensity,
            internalKeyboardHeight,
        ) {
            scope.launch {
                closeBottomSheet()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBottomSheet(
    viewModel: AllViewModel,
    singleTransaction: TransactionWithDetails,
    closeBottomSheet: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    BottomSheetContentEdit(singleTransaction, bottomSheetState, viewModel) {
        scope.launch {
            closeBottomSheet()
        }
    }

}


//bottom sheet to show item Details
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentItemDetails(
    sheetState: SheetState,
    viewModel: AllViewModel,
    singleTransaction: TransactionWithDetails,
    closeBottomSheet: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        BottomSheetContentItemDetailsContent(
            modifier = Modifier, viewModel, closeBottomSheet, singleTransaction
        )
    }
}

val valueArrangement = Arrangement.End
val spaceHeightInDetail = 10.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContentItemDetailsContent(
    modifier: Modifier,
    viewModel: AllViewModel,
    closeBottomSheet: () -> Unit,
    singleTransaction: TransactionWithDetails,
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }
    val labelAndValueStyle = typography.bodyMedium
    val scope = rememberCoroutineScope()
    val colorPalette =
        toPalette(if (singleTransaction.transaction.type == EXPENSE) orange else greenColor)
    var excludeTransactionFromBudget by remember { mutableStateOf(!singleTransaction.transaction.includeInRespectiveBudget) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "${LocalCurrency.current.currencySymbol}" + parseAmountWithPrecision(
                singleTransaction.transaction.amount
            ), style = typography.headlineLarge, fontFamily = numberFont
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = singleTransaction.transaction.note,
            style = typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
            ),
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = modifier.wrapContentWidth()
        ) {
            Button(
                modifier = Modifier.padding(end = 0.dp), onClick = { }, colors = ButtonColors(
                    containerColor = colorPalette.container.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                ), contentPadding = PaddingValues(
                    start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp
                )
            ) {
                val image =
                    rememberAsyncImagePainter(IconState.fromNumber(singleTransaction.category.categoryIconNumber))
                Image(
                    painter = image,
                    contentDescription = "Test Image",
                    modifier = Modifier.size(24.dp),
                )

                Text(
                    text = singleTransaction.category.categoryName,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
            ) {
                Spacer(Modifier.width(12.dp))
                Column {
                    ValueLabelList(
                        modifier = Modifier,
                        labelAndValueStyle = labelAndValueStyle,
                        labelName = "Transaction Type",
                        labelValue = singleTransaction.transaction.type,
                        iconNumber = 12,
                        image = Icons.Outlined.Cable,
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))

                    ValueLabelList(
                        modifier = Modifier,
                        labelAndValueStyle = labelAndValueStyle,
                        labelName = "Date",
                        labelValue = singleTransaction.transaction.date.toLocalDate()
                            .toDisplayStringForMonthWithYear(),
                        iconNumber = 12,
                        image = Icons.Outlined.DateRange,
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                    )
                    Spacer(Modifier.height(spaceHeightInDetail))
                    ValueLabelList(
                        modifier = Modifier,
                        labelAndValueStyle = labelAndValueStyle,
                        labelName = "Payment Method",
                        labelValue = singleTransaction.BankAccount.bankName,
                        iconNumber = 12,
                        image = Icons.Outlined.Payment,
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            TitleWithCheckBox(
                Modifier, labelAndValueStyle, "Exclude from budget", excludeTransactionFromBudget, {
                    excludeTransactionFromBudget = it
                    scope.launch {
                        singleTransaction.transaction.let {
                            val temp =
                                it.copy(includeInRespectiveBudget = !excludeTransactionFromBudget)
                            viewModel.transactionsViewModel.updateForBudgetSwitchDataInDatabase(
                                temp
                            )
                        }

                    }
                })

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
                            TextButton(onClick = { showEdit = !showEdit }) {
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
                        viewModel.transactionsViewModel.deleteSingleTransaction(
                            singleTransaction.transaction
                        )
                        showDeleteConfirmation = false
                        closeBottomSheet()
                    }
                },
                dialogTitle = "Delete Transaction",
                dialogText = "Are you sure, you want to delete this transaction",
                icon = ImageVector.vectorResource(R.drawable.icon_expense)
            )
        }
        AnimatedVisibility(showEdit) {

            EditBottomSheet(
                viewModel,
                singleTransaction,
                closeBottomSheet = {
                    showEdit = !showEdit
                    closeBottomSheet()
                },
            )

        }
    }

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetIconPicker(
    sheetState: SheetState, viewModel: AllViewModel, closeBottomSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
        closeBottomSheet()
    },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime },
        scrimColor = Color.Transparent
    ) {
        BottomSheetContentIconPicker(modifier = Modifier, viewModel)
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BottomSheetContentIconPicker(modifier: Modifier = Modifier, viewModel: AllViewModel) {
    BoxWithConstraints(Modifier.padding(horizontal = 8.dp)) {
        val width = maxWidth / 7
        Column(
            Modifier
                .heightIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Choose Icon",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            val items = iconsList

            // LazyVerticalGrid with a fixed number of columns (e.g., 2 columns)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(width), // 2 columns
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                // contentPadding = PaddingValues(16.dp) // Optional padding for content
            ) {
                items(items) { item ->
                    SingleIcon(item, viewModel)
                }

            }
        }
    }
}

@Composable
fun SingleIcon(item: Int, viewModel: AllViewModel) {
    val scope = rememberCoroutineScope()
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(shape = RoundedCornerShape(50))
            .clickable {
                scope.launch {
                    viewModel.uiViewModel.selectedIconFromBottomSheet.emit(
                        item
                    )
                }
            },
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
    viewModel: AllViewModel,
    closeBottomSheet: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val showExperimentalComponent by viewModel.preferencesViewModel.showExperimentalComponent.collectAsState(
        false
    )
    val showError by viewModel.uiViewModel.errorStatusInBankAccountAdd.collectAsState()

    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = bottomSheetState,
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {

        BottomSheetContentPaymentMethodAddContentNew(
            Modifier.padding(16.dp, 0.dp),
            showError,
            showExperimentalComponent,
            { scope.launch { viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(false) } },
            { bankName, amount, selectedColor ->
                scope.launch {
                    if (amount.isEmpty()) {
                        viewModel.uiViewModel.setErrorMessage("Please enter bank amount")
                        viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(true)
                        return@launch
                    }
                    if (bankName.isEmpty()) {
                        viewModel.uiViewModel.setErrorMessage("Please enter bank name")
                        viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(true)
                        return@launch
                    }
                    if (bankName.isNotEmpty() && amount.isNotEmpty()) {
                        viewModel.bankAccountsViewModel.createObjectAndStoreIt(
                            0L, amount, bankName, selectedColor
                        )
                        closeBottomSheet()
                        return@launch
                    }
                }
            })

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPaymentMethodBottomSheet(
    viewModel: AllViewModel,
    bankAccountsClass: BankAccountsClass,
    closeBottomSheet: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val showExperimentalComponent by viewModel.preferencesViewModel.showExperimentalComponent.collectAsState(
        false
    )
    val showError by viewModel.uiViewModel.errorStatusInBankAccountAdd.collectAsState()

    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = bottomSheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {

        BottomSheetContentPaymentMethodEditContentNew(
            Modifier.padding(16.dp, 0.dp),
            bankAccountsClass,
            showError,
            showExperimentalComponent,
            { scope.launch { viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(false) } },
            { bankName, amount, selectedColor ->
                scope.launch {
                    if (amount.isEmpty()) {
                        viewModel.uiViewModel.setErrorMessage("Please enter bank amount")
                        viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(true)
                        return@launch
                    }
                    if (bankName.isEmpty()) {
                        viewModel.uiViewModel.setErrorMessage("Please enter bank name")
                        viewModel.uiViewModel.errorStatusInBankAccountAdd.emit(true)
                        return@launch
                    }
                    if (bankName.isNotEmpty() && amount.isNotEmpty()) {
                        viewModel.bankAccountsViewModel.createObjectAndStoreIt(
                            bankAccountsClass.bankAccountId, amount, bankName, selectedColor
                        )
                        closeBottomSheet()
                        return@launch
                    }
                }
            })

    }

}

val horizontalPadding = 16.dp
val verticalPadding = 0.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentPaymentMethodAddContentNew(
    modifier: Modifier,
    showError: Boolean,
    showExperimentalComponent: Boolean,
    removeError: () -> Unit,
    saveButtonClicked: (bankName: String, bankAmount: String, selectedColor: Int) -> Unit,
) {
    var bankAmount by remember { mutableStateOf(TextFieldValue("")) }
    var bankName by remember { mutableStateOf(TextFieldValue("")) }

    var selectedColor by remember { mutableIntStateOf(1) }
    val color = ColorState.fromNumber(selectedColor)!!
    val title = "Add Bank Account"

    Column {
        Text(
            text = title,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        LivePaymentCard(
            color = ColorState.fromNumber(selectedColor)!!, bankAmount.text, bankName.text
        )

        Spacer(Modifier.height(16.dp))
        Text(
            modifier = modifier,
            text = "Card Details",
            style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        TextField(
            value = bankName,
            onValueChange = { newText ->
                bankName = newText
                removeError()
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp),
            placeholder = {
                Text(
                    "Bank Name",
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        TextField(
            value = bankAmount,
            onValueChange = { newText ->
                val filteredText = newText.text.filter { it.isDigit() || it == '.' }
                // Ensure only one decimal point is allowed
                if (filteredText.count { it == '.' } <= 1) {
                    // Split into parts before and after the decimal
                    val parts = filteredText.split('.')
                    // Ensure max 7 digits before the decimal and max 2 after
                    if (parts.size == 1 && parts[0].length <= 7 || parts.size == 2 && parts[0].length <= 7 && parts[1].length <= 2) {
                        // Update the TextFieldValue with the filtered text
                        bankAmount = newText.copy(text = filteredText)
                    }
                }
                removeError()
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp),
            visualTransformation = DecimalFilterTransformation(),
            placeholder = {
                Text(
                    "Amount",
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            // visualTransformation = NumberCommaTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = modifier,
            text = "Card Colors", style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp, 0.dp)
        ) {
            for (i in colorList) {
                SingleColorButton(i, selectedColor) { selectedColor = it }
            }
        }
        if (showExperimentalComponent) {
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Card Limit",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = { Math.random().toFloat() },
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth(),
                        color = color.copy(alpha = 0.80f),
                        trackColor = color.copy(alpha = 0.10f),
                        strokeCap = StrokeCap.Round,
                        gapSize = 0.dp,
                        drawStopIndicator = {
                            drawStopIndicator(
                                drawScope = this,
                                stopSize = ProgressIndicatorDefaults.CircularStrokeWidth,
                                color = color,
                                strokeCap = StrokeCap.Round
                            )
                        })
                    Spacer(Modifier.height(12.dp))
                    Text(
                        modifier = Modifier,
                        text = "${LocalCurrency.current.currencySymbol}345890",
                        style = typography.titleMedium.copy(fontFamily = numberFont),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier,
                        text = "left in this month",
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Usage Notification",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    var sliderPosition by remember { mutableFloatStateOf(80f) }
                    Column {
                        Slider(
                            value = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            colors = SliderDefaults.colors(
                                thumbColor = color.copy(0.8f),
                                activeTrackColor = color.copy(0.8f),
                                inactiveTrackColor = color.copy(0.1f),
                            ),
                            steps = 9,
                            valueRange = 0f..100f
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier,
                        text = "get notification when you use ${sliderPosition}% of balance in your bank account",
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        ErrorRow(showError)
        Spacer(Modifier.height(16.dp))
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontalPadding, verticalPadding),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                saveButtonClicked(bankName.text, bankAmount.text, selectedColor)
            },
        ) {
            Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentPaymentMethodEditContentNew(
    modifier: Modifier,
    bankAccountsClass: BankAccountsClass,
    showError: Boolean,
    showExperimentalComponent: Boolean,
    removeError: () -> Unit,
    saveButtonClicked: (bankName: String, bankAmount: String, selectedColor: Int) -> Unit,
) {
    var bankAmount by remember { mutableStateOf(TextFieldValue(bankAccountsClass.currentAmount.toString())) }
    var bankName by remember { mutableStateOf(TextFieldValue(bankAccountsClass.bankName)) }

    var selectedColor by remember { mutableIntStateOf(bankAccountsClass.cardColorNumber) }
    val color = ColorState.fromNumber(selectedColor)!!
    val title = "Edit Bank Account"

    Column {
        Text(
            text = title,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        LivePaymentCard(
            color = ColorState.fromNumber(selectedColor)!!, bankAmount.text, bankName.text
        )

        Spacer(Modifier.height(16.dp))
        Text(
            modifier = modifier,
            text = "Card Details",
            style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        TextField(
            value = bankName,
            onValueChange = { newText ->
                bankName = newText
                removeError()
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp),
            placeholder = {
                Text(
                    "Bank Name",
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        TextField(
            value = bankAmount,
            onValueChange = { newText ->
                val filteredText = newText.text.filter { it.isDigit() || it == '.' }
                // Ensure only one decimal point is allowed
                if (filteredText.count { it == '.' } <= 1) {
                    // Split into parts before and after the decimal
                    val parts = filteredText.split('.')
                    // Ensure max 7 digits before the decimal and max 2 after
                    if (parts.size == 1 && parts[0].length <= 7 || parts.size == 2 && parts[0].length <= 7 && parts[1].length <= 2) {
                        // Update the TextFieldValue with the filtered text
                        bankAmount = newText.copy(text = filteredText)
                    }
                }
                removeError()
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp),
            visualTransformation = DecimalFilterTransformation(),
            placeholder = {
                Text(
                    "Amount",
                    style = typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            // visualTransformation = NumberCommaTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = modifier,
            text = "Card Colors", style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp, 0.dp)
        ) {
            for (i in colorList) {
                SingleColorButton(i, selectedColor) { selectedColor = it }
            }
        }
        if (showExperimentalComponent) {
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Card Limit",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = { Math.random().toFloat() },
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth(),
                        color = color.copy(alpha = 0.80f),
                        trackColor = color.copy(alpha = 0.10f),
                        strokeCap = StrokeCap.Round,
                        gapSize = 0.dp,
                        drawStopIndicator = {
                            drawStopIndicator(
                                drawScope = this,
                                stopSize = ProgressIndicatorDefaults.CircularStrokeWidth,
                                color = color,
                                strokeCap = StrokeCap.Round
                            )
                        })
                    Spacer(Modifier.height(12.dp))
                    Text(
                        modifier = Modifier,
                        text = "${LocalCurrency.current.currencySymbol}345890",
                        style = typography.titleMedium.copy(fontFamily = numberFont),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier,
                        text = "left in this month",
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Usage Notification",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    var sliderPosition by remember { mutableFloatStateOf(80f) }
                    Column {
                        Slider(
                            value = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            colors = SliderDefaults.colors(
                                thumbColor = color.copy(0.8f),
                                activeTrackColor = color.copy(0.8f),
                                inactiveTrackColor = color.copy(0.1f),
                            ),
                            steps = 9,
                            valueRange = 0f..100f
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier,
                        text = "get notification when you use ${sliderPosition}% of balance in your bank account",
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        ErrorRow(showError)
        Spacer(Modifier.height(16.dp))
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontalPadding, verticalPadding),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                saveButtonClicked(bankName.text, bankAmount.text, selectedColor)
            },
        ) {
            Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentAddContentPreview() {
    BottomSheetContentPaymentMethodAddContentNew(
        modifier = Modifier.padding(16.dp, 0.dp),
        showError = true, false, {},
        saveButtonClicked = { a, b, c -> },
    )
}

@Preview(showBackground = true)
@Composable
private fun PaymentEditContentPreview() {
    BottomSheetContentPaymentMethodEditContentNew(
        modifier = Modifier.padding(16.dp, 0.dp), defaultBank,
        showError = true, false, {},
        saveButtonClicked = { a, b, c -> },
    )
}


@Composable
fun ErrorRow(showError: Boolean) {
    val errorMessage = ErrorManager.errorMessage
    AnimatedVisibility(showError) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier.padding(16.dp, 8.dp), colors = CardDefaults.cardColors(
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
                        text = errorMessage.value,
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
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .size(48.dp)
                .fillMaxSize()
                .padding(6.dp),
            color = ColorState.fromNumber(color)!!,
            onClick = { setColor(color) }) {}
    }
}


//@Preview
//@Composable
//private fun BottomSheetContentItemDetailsPreview() {
//    ExpenseTrackerTheme {
//        Surface {
//            // BottomSheetContentItemDetailsContent(Modifier, singleTransaction)
//        }
//    }
//}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetContentAddItemTest(
    sheetState: SheetState,
    viewModel: AllViewModel,
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
        BottomSheetContentItemAddContentTest(modifier = Modifier, viewModel, keyboardHeight)
    }
}

val LocalWindowInsets = compositionLocalOf { PaddingValues(0.dp) }
val LocalWindowSize = compositionLocalOf { WindowWidthSizeClass.Compact }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContentItemAddContentTest(
    modifier: Modifier,
    viewModel: AllViewModel,
    keyboardHeight: Float,
) {
    val scope = rememberCoroutineScope()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    val categoryList by viewModel.categoryViewModel.categoryList.collectAsState()
    val firstSampleClass = firstSampleClass
    var selectedCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }

    val expenseType = TransactionTypeClass(1, EXPENSE)
    val incomeType = TransactionTypeClass(2, INCOME)

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
        viewModel.categoryViewModel.getCorrespondingList(selectedType)
        viewModel.categoryViewModel.getOnlyExpenseCategoryNames()
        viewModel.categoryViewModel.getOnlyIncomeCategoryNames()
    }

    LaunchedEffect(expanded, typeToggle) {
        if (expanded) {
            delay(5000)
            expanded = false
        }
    }
    val budget by viewModel.preferencesViewModel.getTotalAmountPerDay.collectAsState(1f)
    val oldAmount by viewModel.transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()
    val newAmountTemp = if (expenseValue.text.isEmpty()) 0L else extractNumbers(expenseValue.text)
    val newDailyBudget = oldAmount + BigDecimal(newAmountTemp)
    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget.toBigDecimal()
    } else {
        0f
    }
    viewModel.animationViewModel.method(
        "${LocalCurrency.current.currencySymbol}$amountInString", percent.toFloat()
    )

    LaunchedEffect(percent) {
        scope.launch {
            viewModel.animationViewModel.newSpentPercentage.emit(percent.toFloat())
        }
    }

    val imeHeight = WindowInsets.ime.getBottom(Density(LocalContext.current))

    val isKeyboardVisible = imeHeight > 0
    val localDensity = LocalDensity.current

    Column(
        modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Add Transaction",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)) {

                Box(
                    contentAlignment = Alignment.Center,
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
                                viewModel.categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
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
                        selectedCategory,
                        colorPalletGreen,
                        onDismiss = { categoryMenuExpanded = false },
                        categoryList,
                        selectedCategorySetter = {
                            selectedCategory = it
                            scope.launch {
                                viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
                            }
                        },
                    )

                }

                Spacer(Modifier.width(12.dp))

                Box(
                    contentAlignment = Alignment.Center,
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
                                viewModel.categoryViewModel.getCorrespondingList(if (typeToggle) expenseType.type else incomeType.type)
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
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "${LocalCurrency.current.currencySymbol}0",
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
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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
                            viewModel.uiViewModel.errorStatusInAddBottomSheet.emit(false)
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

            AnimatedVisibility(
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
fun NotificationPercentChooserBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    closeBottomSheet: () -> Unit,
    saveNotificationValue: (Float) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        NotificationPercentChooserBottomSheetContent(
            modifier = Modifier, {
                saveNotificationValue(it)
            })
    }
}

@Composable
fun NotificationPercentChooserBottomSheetContent(
    modifier: Modifier = Modifier, saveData: (Float) -> Unit
) {
    val color = MaterialTheme.colorScheme.primary
    var sliderPosition by remember { mutableFloatStateOf(80f) }
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = "Notification",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it

                        },
                        onValueChangeFinished = { saveData(sliderPosition) },
                        colors = SliderDefaults.colors(
                            thumbColor = color.copy(0.8f),
                            activeTrackColor = color.copy(0.8f),
                            inactiveTrackColor = color.copy(0.1f),
                        ),
                        steps = 9,
                        valueRange = 0f..100f
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    modifier = Modifier,
                    text = "get notification when you exceed ${sliderPosition}% of your budget",
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
//        Row {
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Start
//            ) {
//                FilledTonalButton(
//                    onClick = { closeBottomSheet() },
//                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
//                ) { Text(text = "Cancel", color = MaterialTheme.colorScheme.onSurface) }
//            }
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                FilledTonalButton(
//                    onClick = { saveDataAndCloseBottomSheet(sliderPosition) },
//                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary)
//                ) { Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary) }
//            }
//        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributionMethodPickerBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    closeBottomSheet: () -> Unit,
    restDistributionValue: DistributionMethod,
    saveDistributionMethod: (DistributionMethod) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            closeBottomSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        DistributionMethodPickerBottomSheetContent(
            modifier = Modifier, restDistributionValue, { saveDistributionMethod(it) })
    }
}

@Composable
fun DistributionMethodPickerBottomSheetContent(
    modifier: Modifier = Modifier,
    restDistributionValue: DistributionMethod,
    saveDistributionMethod: (DistributionMethod) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose a remaining amount distribution method",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "You can choose how to distribute the remaining balance of the budget after the day",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(16.dp))
        distributionChoiceList.forEach {
            DistributionRadioButtons(
                Modifier,
                it.first,
                it.second,
                it.third,
                restDistributionValue,
                { saveDistributionMethod(it) })
        }

    }
}

@Composable
fun DistributionRadioButtons(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    type: DistributionMethod,
    selectedDistributionMethod: DistributionMethod,
    setCurrentDistributionMethod: (DistributionMethod) -> Unit
) {
    Box(Modifier.clickable { setCurrentDistributionMethod(type) }) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(0.1f)) {
                androidx.compose.animation.AnimatedVisibility(type == selectedDistributionMethod) {
                    Icon(Icons.Rounded.Check, contentDescription = null)
                }
            }

            Column(Modifier.weight(0.9f)) {
                Spacer(Modifier.width(16.dp))
                Text(
                    text = title,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DistributionRadioButtonsPreview() {
    DistributionRadioButtons(
        Modifier,
        "Distribute",
        "The remaining amount will be distributed on the remaining days",
        DistributionMethod.DEFAULT,
        DistributionMethod.DEFAULT,
        {})
}

@Preview(showBackground = true)
@Composable
private fun DistributionMethodPickerBottomSheetContentPreview() {
    DistributionMethodPickerBottomSheetContent(Modifier, DistributionMethod.DEFAULT, {})
}

@Preview(showBackground = true)
@Composable
private fun NotificationPercentChooserBottomSheetContentPreview() {
    NotificationPercentChooserBottomSheetContent(Modifier, {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDetailsBottomSheet(
    bankAccountClass: BankAccountsClass,
    viewModel: AllViewModel,
    onDismiss: () -> Unit,
    editButtonClicked: (BankAccountsClass) -> Unit,
    deleteButtonClicked: (BankAccountsClass) -> Unit,
    statAnalysisClicked: (BankAccountsClass) -> Unit,
    specificTransactionsClicked: (BankAccountsClass) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bankSpecificStats by viewModel.transactionsViewModel.getSpecificBankStatistics(
        bankAccountClass.bankAccountId, getMonthStartDate(), getMonthEndDate()
    ).collectAsState(emptyInfoStat)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
    ) {
        BankDetailsBottomSheetContent(
            bankAccountClass,
            bankSpecificStats,
            {
                editButtonClicked(bankAccountClass)
            },
            { deleteButtonClicked(bankAccountClass) },
            { statAnalysisClicked(bankAccountClass) },
            { specificTransactionsClicked(bankAccountClass) })
    }
}

@Composable
fun BankDetailsBottomSheetContent(
    bankAccountClass: BankAccountsClass,
    bankSpecificStats: InfoStatClass,
    editButtonClicked: () -> Unit,
    deleteButtonClicked: () -> Unit,
    statAnalysisClicked: () -> Unit,
    specificTransactionsClicked: () -> Unit
) {
    val infoCardColors = toPalette(infoColor)
    Column {
        PaymentCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            bankAccountsClass = bankAccountClass,
        ) { }
        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = infoCardColors.main.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    color = infoCardColors.onSurface
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "You have made ${bankSpecificStats.transactionCount} transactions this month, totaling ${bankSpecificStats.totalExpense - bankSpecificStats.totalIncome} in expenses. You have spend around ${LocalCurrency.current.currencySymbol}${
                        bankSpecificStats.totalExpense.divide(
                            getDayDifference(
                                getMonthStartDate().toLocalDate(), LocalDate.now()
                            ).toBigDecimal(), 2, RoundingMode.HALF_UP
                        )
                    }/day in current Month",
                    style = MaterialTheme.typography.labelLarge,
                    color = infoCardColors.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(16.dp))
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { statAnalysisClicked() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = infoCardColors.container,
                        contentColor = infoCardColors.onContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "View Statistical analysis")
                        Icon(Icons.Rounded.PieChart, contentDescription = null)
                    }
                }
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { specificTransactionsClicked() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = infoCardColors.container,
                        contentColor = infoCardColors.onContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Add Transaction")
                        Icon(Icons.Rounded.AddCircle, contentDescription = null)
                    }

                }
            }
        }
        ViewItemBottomRow(Modifier, { editButtonClicked() }, { deleteButtonClicked() })
    }
}

@Preview(showBackground = true)
@Composable
private fun BankDetailsBottomSheetContentPreview() {
    BankDetailsBottomSheetContent(defaultBank, emptyInfoStat, {}, {}, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsBottomSheet(
    categoryClass: CategoryClass,
    viewModel: AllViewModel,
    onDismiss: () -> Unit,
    editButtonClicked: (CategoryClass) -> Unit,
    deleteButtonClicked: (CategoryClass) -> Unit,
    statAnalysisClicked: (CategoryClass) -> Unit,
    specificTransactionsClicked: (CategoryClass) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySpecificStats by viewModel.transactionsViewModel.getSpecificCategoryStatistics(
        categoryClass.categoryId, getMonthStartDate(), getMonthEndDate()
    ).collectAsState(emptyInfoStat)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
    ) {
        CategoryDetailsBottomSheetContent(
            categoryClass,
            categorySpecificStats,
            {
                editButtonClicked(categoryClass)
            },
            { deleteButtonClicked(categoryClass) },
            { statAnalysisClicked(categoryClass) },
            { specificTransactionsClicked(categoryClass) })
    }
}

@Composable
fun CategoryDetailsBottomSheetContent(
    categoryClass: CategoryClass,
    categorySpecificStats: InfoStatClass,
    editButtonClicked: () -> Unit,
    deleteButtonClicked: () -> Unit,
    statAnalysisClicked: () -> Unit,
    specificTransactionsClicked: () -> Unit
) {
    val color = toPalette(if (categoryClass.categoryType == EXPENSE) orange else greenColor)
    val infoCardColors = toPalette(infoColor)

    val containerColor by animateColorAsState(
        targetValue = combineColors(
            MaterialTheme.colorScheme.surface,
            color.main,
            angle = 0.1f,
        )
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box() {
            Surface(
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .size(160.dp)
                    .fillMaxSize(),
                color = containerColor
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {

                    val image =
                        rememberAsyncImagePainter(IconState.fromNumber(categoryClass.categoryIconNumber))
                    Image(
                        painter = image,
                        contentDescription = "Image ${categoryClass.categoryIconNumber}",
                        modifier = Modifier.size(96.dp)

                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = categoryClass.categoryName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = infoCardColors.main.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    color = infoCardColors.onSurface
                )
                Spacer(Modifier.height(4.dp))
                categorySpecificStats.totalExpense.div(
                    (getDayDifference(
                        getMonthStartDate().toLocalDate(), LocalDate.now()
                    )).toBigDecimal()
                )
                Text(
                    text = "You have made ${categorySpecificStats.transactionCount} transactions this month, totaling ${LocalCurrency.current.currencySymbol}${
                        if (categoryClass.categoryType == EXPENSE) {
                            categorySpecificStats.totalExpense
                        } else categorySpecificStats.totalIncome
                    } in ${
                        if (categoryClass.categoryType == EXPENSE) {
                            "expenses"
                        } else "income"
                    }. You have ${
                        if (categoryClass.categoryType == EXPENSE) {
                            "spend"
                        } else "earned"
                    } around ${LocalCurrency.current.currencySymbol}${
                        if (categoryClass.categoryType == EXPENSE) {
                            categorySpecificStats.totalExpense.divide(
                                (getDayDifference(
                                    getMonthStartDate().toLocalDate(), LocalDate.now()
                                )).toBigDecimal(), 2, RoundingMode.HALF_UP
                            )
                        } else categorySpecificStats.totalIncome.divide(
                            (getDayDifference(
                                getMonthStartDate().toLocalDate(), LocalDate.now()
                            )).toBigDecimal(), 2, RoundingMode.HALF_UP
                        )
                    }/day in current Month",
                    style = MaterialTheme.typography.labelLarge,
                    color = infoCardColors.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(16.dp))
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { statAnalysisClicked() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = infoCardColors.container,
                        contentColor = infoCardColors.onContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "View Statistical analysis")
                        Icon(Icons.Rounded.PieChart, contentDescription = null)
                    }
                }
                Spacer(Modifier.height(4.dp))
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { specificTransactionsClicked() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = infoCardColors.container,
                        contentColor = infoCardColors.onContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Add Transaction")
                        Icon(Icons.Rounded.AddCircle, contentDescription = null)
                    }

                }
            }
        }

        ViewItemBottomRow(Modifier, { editButtonClicked() }, { deleteButtonClicked() })
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDetailsBottomSheetContentPreview() {
    CategoryDetailsBottomSheetContent(defaultCategoryClass, emptyInfoStat, {}, {}, {}, {})
}