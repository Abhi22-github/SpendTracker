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
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.TurnRight
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.components.ConfirmationAlertDialog
import com.roaa.expensetracker.Composables.components.DateRangePickerModal
import com.roaa.expensetracker.Composables.components.DistributionMethodPickerBottomSheet
import com.roaa.expensetracker.Composables.components.ErrorRow
import com.roaa.expensetracker.Composables.components.NotificationPercentChooserBottomSheet
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.secondaryAlpha
import com.roaa.expensetracker.Composables.utils.DistributionMethod
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.DecimalFilterTransformation
import com.roaa.expensetracker.Utilities.LongMillisToNoralLong
import com.roaa.expensetracker.Utilities.getDayDifference
import com.roaa.expensetracker.Utilities.getValidDatesListFromLong
import com.roaa.expensetracker.Utilities.toDateWithDayName
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.BudgetDayViewModel
import com.roaa.expensetracker.ViewModels.BudgetViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetSetupScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager, modifier: Modifier = Modifier
) {
    BackHandler {
        handleBackNavigation(navigationManager)
    }
    Scaffold(topBar = {
        TopBar(
            title = "Setup Budget",
            showDelete = false,
            sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
            delete = {}
        )
    }) {
        Column(Modifier.padding(it)) {
            BudgetBottomSheet(
                false,navigationManager
            )
        }
    }
}

@Composable
fun BudgetBottomSheet(
    isBudgetSet: Boolean,
    navigationManager: NavigationManager,
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    budgetDayViewModel: BudgetDayViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    uiViewModel: UiViewModel = hiltViewModel(),
) {
    val modifier = Modifier.padding(16.dp, 0.dp)

    var shouldShowConfirmation by remember { mutableStateOf(false) }
    val errorStatus by uiViewModel.errorStatusInBudgetAdd.collectAsState()
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    BottomSheetBudgetContent(
        modifier,
        { totalAmountText, startDate, endDate, totalDaysRemaining, restDistributionValue, notificationUsageValue, totalAmountPerDay ->
            if (totalAmountText.isEmpty()) {
                scope.launch {
                    uiViewModel.errorStatusMessage.emit(
                        "Please enter amount"
                    )
                    uiViewModel.errorStatusInBudgetAdd.emit(
                        true
                    )
                }
            } else if (totalAmountText.toFloat() == 0f) {
                scope.launch {
                    uiViewModel.errorStatusMessage.emit(
                        "Please enter amount"
                    )
                    uiViewModel.errorStatusInBudgetAdd.emit(
                        true
                    )
                }
            } else {
                if (isBudgetSet) {
                    shouldShowConfirmation = true
                } else {
                    SaveBudgetDetailsInDatabase(
                        transactionsViewModel,
                        budgetDayViewModel,
                        budgetViewModel,
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
        //  uiViewModel
    )

    if (shouldShowConfirmation) {
        ConfirmationAlertDialog(
            { shouldShowConfirmation = false },
            {
//                SaveBudgetDetailsInDatabase(
//                    transactionsViewModel,
//                    budgetDayViewModel,
//                    budgetViewModel,
//                    totalAmountText,
//                    totalAmountPerDay,
//                    totalDaysRemaining,
//                    startDate,
//                    endDate,
//                    restDistributionValue,
//                    notificationUsageValue,
//                    keyboardController,
//                    focusManager
//                )
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
    budgetStartDate: Long,
    budgetEndDate: Long,
    restDistributionValue: DistributionMethod,
    notificationUsageValue: Float,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {
    budgetViewModel.createObjectAndStoreIt(
        totalAmountForMonth.toFloat(),
        totalAmountPerDay,
        totalDaysRemaining,
        budgetStartDate,
        budgetEndDate,
        restDistributionValue,
        notificationUsageValue,
        getValidDatesListFromLong(budgetStartDate, budgetEndDate)
    )

    focusManager.clearFocus()
    keyboardController?.hide()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetBudgetContent(
    modifier: Modifier,
    saveButtonClicked: (String, Long, Long, Long, DistributionMethod, Float, Float) -> Unit,
    errorStatus: Boolean,
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember {
        FocusRequester()
    }
    var totalAmountText by remember { mutableStateOf(TextFieldValue("")) }
    var totalAmountPerDay by remember { mutableFloatStateOf(0f) }
    var showDateRangePicker by remember { mutableStateOf(false) }
    val dateRangePickerState =
        rememberDateRangePickerState(initialSelectedStartDateMillis = System.currentTimeMillis())
    var startDate by remember { mutableStateOf<Long>(System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf<Long>(System.currentTimeMillis()) }
    var totalDaysRemaining = remember {
        getDayDifference(
            startDate.LongMillisToNoralLong().toLocalDate(),
            endDate.LongMillisToNoralLong().toLocalDate()
        )
    }
    val notificationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val restDistributionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showNotificationPicker by remember { mutableStateOf(false) }
    var notificationUsageValue by remember { mutableFloatStateOf(20f) }
    var restDistribution by remember { mutableStateOf(false) }
    var restDistributionValue by remember { mutableStateOf(DistributionMethod.DEFAULT) }

    LaunchedEffect(totalAmountText, startDate, endDate) {
        focusRequester.requestFocus()
        totalDaysRemaining = getDayDifference(
            startDate.LongMillisToNoralLong().toLocalDate(),
            endDate.LongMillisToNoralLong().toLocalDate()
        )
        if (totalAmountText.text.isNotEmpty() && totalAmountText.text.toFloat() != 0f) {
            totalAmountPerDay = totalAmountText.text.toFloat() / totalDaysRemaining
        } else {
            totalAmountPerDay = 0f
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
//                    scope.launch {
//                        uiViewModel.errorStatusInBudgetAdd.emit(false)
//                    }
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
            Box(Modifier
                .fillMaxWidth()
                .clickable { showDateRangePicker = !showDateRangePicker }) {
                Row(
                    modifier
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Timelapse, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "To ${
                            endDate.LongMillisToNoralLong().toLocalDate().toDateWithDayName()
                        } (${
                            getDayDifference(
                                startDate.LongMillisToNoralLong().toLocalDate(),
                                endDate.LongMillisToNoralLong().toLocalDate()
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
            Box(Modifier
                .fillMaxWidth()
                .clickable { restDistribution = !restDistribution }) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.TurnRight, contentDescription = null)
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
                        text = " Days",
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.End,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Box(Modifier
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
                        startDate.LongMillisToNoralLong().toLocalDate().toLong(),
                        endDate.LongMillisToNoralLong().toLocalDate().toLong(),
                        totalDaysRemaining,
                        restDistributionValue,
                        notificationUsageValue,
                        totalAmountPerDay
                    )
                },
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Create Budget", color = MaterialTheme.colorScheme.onPrimary)
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

    if (showDateRangePicker) {
        DateRangePickerModal(dateRangePickerState, {
            startDate = it.first ?: System.currentTimeMillis()
            endDate = it.second ?: System.currentTimeMillis()
        }) { showDateRangePicker = !showDateRangePicker }
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