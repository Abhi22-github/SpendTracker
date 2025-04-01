package com.roaa.expensetracker.composable.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Start
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.CustomFonts.numberFont
import com.roaa.expensetracker.composable.components.ConfirmationAlertDialog
import com.roaa.expensetracker.composable.components.DatePickerModal
import com.roaa.expensetracker.composable.components.DistributionMethodPickerBottomSheet
import com.roaa.expensetracker.composable.components.ErrorRow
import com.roaa.expensetracker.composable.components.NotificationPercentChooserBottomSheet
import com.roaa.expensetracker.composable.components.TopBar
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.handleBackNavigation
import com.roaa.expensetracker.composable.secondaryAlpha
import com.roaa.expensetracker.composable.utils.ActionTypes
import com.roaa.expensetracker.composable.utils.DistributionMethod
import com.roaa.expensetracker.database.relations.BudgetWithDayDetails
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.notification.workManager.scheduleBudgetExpiryStatus
import com.roaa.expensetracker.notification.workManager.scheduleBudgetReminder
import com.roaa.expensetracker.utilities.DecimalFilterTransformation
import com.roaa.expensetracker.utilities.LongMillisToNormalLong
import com.roaa.expensetracker.utilities.getDayDifference
import com.roaa.expensetracker.utilities.getValidDatesListFromLong
import com.roaa.expensetracker.utilities.toDateWithDayName
import com.roaa.expensetracker.utilities.toLocalDate
import com.roaa.expensetracker.utilities.toLong
import com.roaa.expensetracker.utilities.toLongMillis
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBudgetDayClass
import kotlinx.coroutines.launch
import java.math.BigDecimal

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetSetupScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    type: ActionTypes,
    budgetId: Long,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        handleBackNavigation(navigationManager)
    }
    val title = when (type) {
        ActionTypes.ADD -> "Setup Budget"
        ActionTypes.EDIT -> "Edit Budget"
    }
    val budgetWithSummaryFromRoom by viewModel.budgetViewModel.getBudgetWithDays(budgetId)
        .collectAsState(
            BudgetWithDayDetails(
                emptyBudgetClass, listOf(emptyBudgetDayClass)
            )
        )
    var budgetWithSummary by remember {
        mutableStateOf(
            BudgetWithDayDetails(
                emptyBudgetClass, listOf(emptyBudgetDayClass)
            )
        )
    }
    var isBudgetSet by remember { mutableStateOf(false) }
    LaunchedEffect(budgetWithSummaryFromRoom) {
        budgetWithSummaryFromRoom?.let {
            isBudgetSet = it?.budgetSummary?.isActive ?: false
        }
        budgetWithSummary = budgetWithSummaryFromRoom ?: BudgetWithDayDetails(
            emptyBudgetClass, listOf(emptyBudgetDayClass)
        )
    }
    Scaffold(topBar = {
        TopBar(
            title = title,
            showDelete = false,
            sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
            delete = {}
        )
    }) {
        Column(Modifier.padding(it)) {
            key(budgetWithSummary) {
                BudgetContentController(
                    isBudgetSet, navigationManager, viewModel, budgetWithSummary
                )
            }
        }
    }
}

@Composable
fun BudgetContentController(
    isBudgetSet: Boolean,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    budgetWithSummary: BudgetWithDayDetails,
) {
    val modifier = Modifier.padding(16.dp, 0.dp)
    val context = LocalContext.current

    var shouldShowConfirmation by remember { mutableStateOf(false) }
    val errorStatus by viewModel.uiViewModel.errorStatusInBudgetAdd.collectAsState()
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var totalAmountText by remember { mutableStateOf("") }
    var totalAmountPerDay by remember { mutableStateOf(BigDecimal.ZERO) }
    var totalDaysRemaining by remember { mutableLongStateOf(0L) }
    var startDate by remember { mutableLongStateOf(0L) }
    var endDate by remember { mutableLongStateOf(0L) }
    var restDistributionValue by remember { mutableStateOf(DistributionMethod.DEFAULT) }
    var notificationUsageValue by remember { mutableStateOf(BigDecimal(20)) }

    BottomSheetBudgetContent(
        modifier,
        isBudgetSet,
        budgetWithSummary,
        { totalAmountTextInner, startDateInner, endDateInner, totalDaysRemainingInner, restDistributionValueInner, notificationUsageValueInner, totalAmountPerDayInner ->
            totalAmountText = totalAmountTextInner
            totalAmountPerDay = totalAmountPerDayInner
            totalDaysRemaining = totalDaysRemainingInner
            startDate = startDateInner
            endDate = endDateInner
            restDistributionValue = restDistributionValueInner
            notificationUsageValue = notificationUsageValueInner
            if (totalAmountText.isEmpty()) {
                scope.launch {
                    viewModel.uiViewModel.setErrorMessage(
                        "Please enter amount"
                    )
                    viewModel.uiViewModel.errorStatusInBudgetAdd.emit(
                        true
                    )
                }
            } else if (totalAmountText.toBigDecimal() == BigDecimal.ZERO) {
                scope.launch {
                    viewModel.uiViewModel.setErrorMessage(
                        "Please enter amount"
                    )
                    viewModel.uiViewModel.errorStatusInBudgetAdd.emit(
                        true
                    )
                }
            } else {
                if (isBudgetSet) {
                    shouldShowConfirmation = true
                } else {
                    SaveBudgetDetailsInDatabase(
                        context,
                        budgetWithSummary,
                        viewModel,
                        totalAmountText,
                        totalAmountPerDay,
                        totalDaysRemaining,
                        startDate,
                        endDate,
                        restDistributionValue,
                        notificationUsageValue,
                        keyboardController,
                        focusManager
                    )
                    handleBackNavigation(navigationManager)
                }
            }
        },
        errorStatus,
        {
            scope.launch {
                viewModel.uiViewModel.errorStatusInBudgetAdd.emit(false)
            }
        },
        {
            scope.launch {
                viewModel.uiViewModel.setErrorMessage("End Date should be greater than Start Date")
                viewModel.uiViewModel.errorStatusInBudgetAdd.emit(true)
            }
        }
    )

    if (shouldShowConfirmation) {
        ConfirmationAlertDialog(
            { shouldShowConfirmation = false },
            {
                SaveBudgetDetailsInDatabase(
                    context,
                    budgetWithSummary,
                    viewModel,
                    totalAmountText,
                    totalAmountPerDay,
                    totalDaysRemaining,
                    startDate,
                    endDate,
                    restDistributionValue,
                    notificationUsageValue,
                    keyboardController,
                    focusManager
                )
                handleBackNavigation(navigationManager)
                shouldShowConfirmation = false
            },
            "Change Budget",
            "Are you sure, you want to change the current budget?",
            ImageVector.vectorResource(R.drawable.icon_expense)
        )
    }
}

fun SaveBudgetDetailsInDatabase(
    context: Context,
    budgetWithSummary: BudgetWithDayDetails,
    viewModel: AllViewModel,
    totalAmountForMonth: String,
    totalAmountPerDay: BigDecimal,
    totalDaysRemaining: Long,
    budgetStartDate: Long,
    budgetEndDate: Long,
    restDistributionValue: DistributionMethod,
    notificationUsageValue: BigDecimal,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {
    viewModel.budgetViewModel.createObjectAndStoreIt(
        budgetWithSummary,
        totalAmountForMonth.toBigDecimal(),
        totalAmountPerDay,
        totalDaysRemaining,
        budgetStartDate,
        budgetEndDate,
        restDistributionValue,
        notificationUsageValue,
        getValidDatesListFromLong(budgetStartDate.toLocalDate(), budgetEndDate.toLocalDate())
    )
    scheduleBudgetReminder(context, budgetEndDate.toLocalDate())
    scheduleBudgetExpiryStatus(
        context,
        budgetEndDate.toLocalDate(),
        budgetWithSummary.budgetSummary.budgetId
    )
    focusManager.clearFocus()
    keyboardController?.hide()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetBudgetContent(
    modifier: Modifier,
    isBudgetSet: Boolean,
    budgetWithSummary: BudgetWithDayDetails,
    saveButtonClicked: (String, Long, Long, Long, DistributionMethod, BigDecimal, BigDecimal) -> Unit,
    errorStatus: Boolean,
    removeError: () -> Unit,
    setError: () -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val buttonTitle by remember { mutableStateOf(if (isBudgetSet) "Save Budget" else "Create Budget") }
    var totalAmountText by remember { mutableStateOf(TextFieldValue(if (isBudgetSet) budgetWithSummary.budgetSummary.totalBudgetAmount.toString() else "")) }
    var totalAmountPerDay by remember { mutableStateOf(if (isBudgetSet) budgetWithSummary.budgetSummary.budgetAmountPerDay else BigDecimal.ZERO) }
    var showDateRangePickerForStartDate by remember { mutableStateOf(false) }
    var showDateRangePickerForEndDate by remember { mutableStateOf(false) }

    var startDate by remember {
        mutableStateOf<Long>(
            if (isBudgetSet) budgetWithSummary.budgetSummary.budgetStartDate
            else java.time.LocalDate.now().toLong()
        )
    }
    var endDate by remember {
        mutableStateOf<Long>(
            if (isBudgetSet) budgetWithSummary.budgetSummary.budgetEndDate
            else java.time.LocalDate.now().toLong()
        )
    }
    val startDatePickerState =
        rememberDatePickerState(startDate.toLocalDate().toLongMillis())
    val endDatePickerState =
        rememberDatePickerState(endDate.toLocalDate().toLongMillis())

    var totalDaysRemaining = remember {
        if (isBudgetSet) budgetWithSummary.budgetSummary.budgetTotalDays
        else
            getDayDifference(
                startDate.LongMillisToNormalLong().toLocalDate(),
                endDate.LongMillisToNormalLong().toLocalDate()
            )
    }
    val notificationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val restDistributionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showNotificationPicker by remember { mutableStateOf(false) }
    var notificationUsageValue by remember {
        mutableStateOf(
            if (isBudgetSet) budgetWithSummary.budgetSummary.notificationForBudgetUsage else BigDecimal(
                80
            )
        )
    }
    var restDistribution by remember { mutableStateOf(false) }
    var restDistributionValue by remember {
        mutableStateOf(
            if (isBudgetSet)
                DistributionMethod.fromNumberToObject(budgetWithSummary.budgetSummary.restDistributionType)
            else
                DistributionMethod.DEFAULT
        )
    }

    LaunchedEffect(totalAmountText, startDate, endDate) {
        focusRequester.requestFocus()
        totalDaysRemaining = getDayDifference(
            startDate.toLocalDate(),
            endDate.toLocalDate()
        )
        if (totalAmountText.text.isNotEmpty() && totalAmountText.text.toBigDecimal() != BigDecimal.ZERO) {
            totalAmountPerDay =
                totalAmountText.text.toBigDecimal().div(totalDaysRemaining.toBigDecimal())
        } else {
            totalAmountPerDay = BigDecimal.ZERO
        }
        if (endDate < startDate) {
            setError()
        } else {
            removeError()
        }
    }
    ConstraintLayout(
        Modifier
            .fillMaxSize()
    ) {
        val (content, button) = createRefs()
        Column(Modifier.constrainAs(content) {
            top.linkTo(parent.top)
        }) {
            Spacer(Modifier.height(12.dp))
            ErrorRow(errorStatus)
            Spacer(Modifier.height(32.dp))
            Row {
                TextField(
                    value = totalAmountText,
                    onValueChange = { newValue ->
                        val filteredText = newValue.text.filter { it.isDigit() || it == '.' }
                        // Ensure only one decimal point is allowed
                        if (filteredText.count { it == '.' } <= 1) {
                            // Split into parts before and after the decimal
                            val parts = filteredText.split('.')
                            // Ensure max 7 digits before the decimal and max 2 after
                            if (parts.size == 1 && parts[0].length <= 7 || parts.size == 2 && parts[0].length <= 7 && parts[1].length <= 2) {
                                // Update the TextFieldValue with the filtered text
                                totalAmountText = newValue.copy(text = filteredText)
                            }
                        }
                        removeError()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .focusRequester(focusRequester),
                    singleLine = true,

                    placeholder = {
                        Text(
                            "${LocalCurrency.current.currencySymbol}0",
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDateRangePickerForStartDate = !showDateRangePickerForStartDate
                    }) {
                Row(
                    modifier
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Start, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "From  -> ${startDate.toLocalDate().toDateWithDayName()}",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePickerForEndDate = !showDateRangePickerForEndDate }) {
                Row(
                    modifier
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.StopCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "To -> ${
                            endDate.toLocalDate().toDateWithDayName()
                        } (${
                            getDayDifference(
                                startDate.toLocalDate(),
                                endDate.toLocalDate()
                            )
                        } days)",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { restDistribution = !restDistribution }) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Directions, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Rest",
                            modifier = Modifier,
                            textAlign = TextAlign.Start,
                            style = typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = restDistributionValue.type,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.End,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { showNotificationPicker = !showNotificationPicker }) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Notifications, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Notification",
                            modifier = Modifier,
                            textAlign = TextAlign.Start,
                            style = typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Above ${notificationUsageValue}% ",
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.End,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Total",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$totalAmountPerDay Per day",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(16.dp))
        }
        val padding = getImePadding()
        Column(
            modifier
                .constrainAs(button) { bottom.linkTo(parent.bottom, padding + 8.dp) }

        ) {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    saveButtonClicked(
                        totalAmountText.text,
                        startDate.toLocalDate().toLong(),
                        endDate.toLocalDate().toLong(),
                        getDayDifference(
                            startDate.toLocalDate(),
                            endDate.toLocalDate()
                        ),
                        restDistributionValue,
                        notificationUsageValue,
                        totalAmountPerDay
                    )
                },
                enabled = !errorStatus,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = buttonTitle, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
    if (restDistribution) {
        DistributionMethodPickerBottomSheet(
            modifier = Modifier,
            sheetState = restDistributionSheetState,
            closeBottomSheet = { restDistribution = !restDistribution },
            restDistributionValue = restDistributionValue,
            saveDistributionMethod = { restDistributionValue = it }
        )
    }

    if (showDateRangePickerForStartDate) {
        DatePickerModal(startDatePickerState, {
            startDate = it ?: System.currentTimeMillis()
        }
        ) { showDateRangePickerForStartDate = !showDateRangePickerForStartDate }
    }
    if (showDateRangePickerForEndDate) {
        DatePickerModal(
            endDatePickerState,
            { endDate = it ?: System.currentTimeMillis() }) {
            showDateRangePickerForEndDate = !showDateRangePickerForEndDate
        }
    }
    if (showNotificationPicker) {
        NotificationPercentChooserBottomSheet(
            sheetState = notificationSheetState,
            saveNotificationValue = { notificationUsageValue = it.toBigDecimal() },
            closeBottomSheet = { showNotificationPicker = !showNotificationPicker })
    }
}

@Composable
fun getImePadding(): Dp {
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val navBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)

    return with(LocalDensity.current) {
        (imeHeight - navBarHeight).coerceAtLeast(0).toDp() // Ensure non-negative padding
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomSheetBudgetContentPreview() {
//
//    BottomSheetBudgetContent(
//        modifier = Modifier.padding(16.dp, 0.dp),
//        saveButtonClicked = { String, Long, Long, Long, DistributionMethod, Float, Float -> },
//        errorStatus = false,
//
//        )
//}