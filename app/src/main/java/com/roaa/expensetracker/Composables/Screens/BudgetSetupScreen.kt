package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.components.ConfirmationAlertDialog
import com.roaa.expensetracker.Composables.components.DatePickerModal
import com.roaa.expensetracker.Composables.components.DistributionMethodPickerBottomSheet
import com.roaa.expensetracker.Composables.components.ErrorRow
import com.roaa.expensetracker.Composables.components.NotificationPercentChooserBottomSheet
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.secondaryAlpha
import com.roaa.expensetracker.Composables.utils.ActionTypes
import com.roaa.expensetracker.Composables.utils.DistributionMethod
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Hilt.AllViewModel
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.DecimalFilterTransformation
import com.roaa.expensetracker.Utilities.LongMillisToNormalLong
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBudgetClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBudgetDayClass
import com.roaa.expensetracker.Utilities.getDayDifference
import com.roaa.expensetracker.Utilities.getValidDatesListFromLong
import com.roaa.expensetracker.Utilities.toDateWithDayName
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.Utilities.toLongMillis
import kotlinx.coroutines.launch

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
    val budgetWithSummaryFromRoom by viewModel.budgetViewModel.getBudgetWithDays(budgetId).collectAsState(
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
                    isBudgetSet, navigationManager, viewModel,budgetWithSummary
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

    var shouldShowConfirmation by remember { mutableStateOf(false) }
    val errorStatus by viewModel.uiViewModel.errorStatusInBudgetAdd.collectAsState()
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var totalAmountText by remember { mutableStateOf("") }
    var totalAmountPerDay by remember { mutableFloatStateOf(0f) }
    var totalDaysRemaining by remember { mutableLongStateOf(0L) }
    var startDate by remember { mutableLongStateOf(0L) }
    var endDate by remember { mutableLongStateOf(0L) }
    var restDistributionValue by remember { mutableStateOf(DistributionMethod.DEFAULT) }
    var notificationUsageValue by remember { mutableFloatStateOf(20f) }

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
            } else if (totalAmountText.toFloat() == 0f) {
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
    budgetWithSummary: BudgetWithDayDetails,
    viewModel: AllViewModel,
    totalAmountForMonth: String,
    totalAmountPerDay: Float,
    totalDaysRemaining: Long,
    budgetStartDate: Long,
    budgetEndDate: Long,
    restDistributionValue: DistributionMethod,
    notificationUsageValue: Float,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {
    viewModel.budgetViewModel.createObjectAndStoreIt(
        budgetWithSummary,
        totalAmountForMonth.toFloat(),
        totalAmountPerDay,
        totalDaysRemaining,
        budgetStartDate,
        budgetEndDate,
        restDistributionValue,
        notificationUsageValue,
        getValidDatesListFromLong(budgetStartDate.toLocalDate(), budgetEndDate.toLocalDate())
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
    saveButtonClicked: (String, Long, Long, Long, DistributionMethod, Float, Float) -> Unit,
    errorStatus: Boolean,
    removeError: () -> Unit,
    setError:() -> Unit
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember {
        FocusRequester()
    }
    val buttonTitle by remember { mutableStateOf(if (isBudgetSet) "Save Budget" else "Create Budget") }
    var totalAmountText by remember { mutableStateOf(TextFieldValue(if (isBudgetSet) budgetWithSummary.budgetSummary.totalBudgetAmount.toString() else "")) }
    var totalAmountPerDay by remember { mutableFloatStateOf(if (isBudgetSet) budgetWithSummary.budgetSummary.budgetAmountPerDay else 0f) }
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
    var notificationUsageValue by remember { mutableFloatStateOf(if (isBudgetSet) budgetWithSummary.budgetSummary.notificationForBudgetUsage else 20f) }
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
        if (totalAmountText.text.isNotEmpty() && totalAmountText.text.toFloat() != 0f) {
            totalAmountPerDay = totalAmountText.text.toFloat() / totalDaysRemaining
        } else {
            totalAmountPerDay = 0f
        }
        if (endDate < startDate) {
            setError()
        } else {
            removeError()
        }
    }
    ConstraintLayout(Modifier.fillMaxSize()) {
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
                        text = "${restDistributionValue.type}",
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
                        text = "Below ${notificationUsageValue}% ",
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
        Column(modifier.constrainAs(button) { bottom.linkTo(parent.bottom) }) {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                onClick = {
                    saveButtonClicked(
                        totalAmountText.text,
                        startDate.toLocalDate().toLong(),
                        endDate.toLocalDate().toLong(),
                        totalDaysRemaining,
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
            saveNotificationValue = { notificationUsageValue = it },
            closeBottomSheet = { showNotificationPicker = !showNotificationPicker })
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