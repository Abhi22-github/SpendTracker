package com.roaa.expensetracker.Composables.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roaa.expensetracker.Composables.Screens.BankChips
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Utilities.LongMillisToNormalLong
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.getDayDifference
import com.roaa.expensetracker.Utilities.toDisplayStringForMonthWithYear
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.Utilities.toLongMillis
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    datePickerState: DatePickerState, onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected(datePickerState.selectedDateMillis?.LongMillisToNormalLong())
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    dateRangePickerState: DateRangePickerState,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateRangeSelected(
                Pair(
                    dateRangePickerState.selectedStartDateMillis,
                    dateRangePickerState.selectedEndDateMillis
                )
            )
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DateRangePicker(state = dateRangePickerState, title = {
            Text(
                text = "Select Budget Days",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                style = typography.bodyMedium
            )
        }, headline = {
            DateRangePickerDefaults.DateRangePickerHeadline(
                selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                displayMode = dateRangePickerState.displayMode,
                dateFormatter = DatePickerDefaults.dateFormatter(),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 0.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }, showModeToggle = true, modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit,
    bankAccountList: List<BankAccountsClass>,
    saveButtonClicked: (Long, Long, Long,BankAccountsClass) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedBankAccount by remember { mutableStateOf(bankAccountList.get(0)) }
    var startDate by remember { mutableStateOf<Long>(LocalDate.now().minusMonths(1).toLong()) }
    var endDate by remember { mutableStateOf<Long>(LocalDate.now().toLong()) }
    var selectedDuration by remember {
        mutableStateOf(
            getDayDifference(
                startDate.toLocalDate(),
                endDate.toLocalDate()
            )
        )
    }
    val showErrorStatus by uiViewModel.errorStatusInStatisticsFilter.collectAsState(false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(startDate, endDate) {
        if (endDate < startDate) {
            scope.launch {
                uiViewModel.errorStatusInStatisticsFilter.emit(true)
                uiViewModel.errorStatusMessage.emit("End Date should be greater than Start Date")
            }
        } else {
            uiViewModel.errorStatusInStatisticsFilter.emit(false)
        }
        selectedDuration = getDayDifference(startDate.toLocalDate(), endDate.toLocalDate())
    }
    ModalBottomSheet(onDismissRequest = closeBottomSheet, sheetState = bottomSheetState) {
        FilterBottomSheetContent(
            Modifier.padding(horizontal = 16.dp),
            closeBottomSheet,
            bankAccountList,
            selectedBankAccount,
            { selectedBankAccount = it },
            startDate,
            { startDate = it },
            endDate,
            { endDate = it },
            selectedDuration,
            showErrorStatus,
            { saveButtonClicked(startDate, endDate, selectedDuration,selectedBankAccount) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit,
    bankAccountList: List<BankAccountsClass>,
    selectedBankAccount: BankAccountsClass,
    setSelectedChip: (BankAccountsClass) -> Unit,
    startDate: Long,
    setStartDate: (Long) -> Unit,
    endDate: Long,
    setEndDate: (Long) -> Unit,
    selectedDuration: Long,
    showErrorStatus: Boolean,
    saveButtonClicked: () -> Unit
) {
    var filterRowContent = listOf("All Accounts", "HDFC Bank", "Bank of Maharashtra")
    val startDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = startDate.toLocalDate().toLongMillis())
    val endDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = endDate.toLocalDate().toLongMillis())
    var showStartDateDayPicker by remember { mutableStateOf(false) }
    var showEndDateDayPicker by remember { mutableStateOf(false) }
    Column(Modifier) {
        Column(
            verticalArrangement = Arrangement.Top, modifier = Modifier
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(Modifier
                    .weight(1f)
                    .background(
                        Color.Transparent, shape = RoundedCornerShape(25.dp)
                    )
                    .padding(start = 16.dp)
                    .border(
                        BorderStroke(
                            1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ), RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(25.dp))
                    .clickable {
                        showStartDateDayPicker = !showStartDateDayPicker
                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = startDate.toLocalDate().toDisplayStringForMonthWithYear(),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Box(Modifier
                    .weight(1f)
                    .background(
                        Color.Transparent, shape = RoundedCornerShape(25.dp)
                    )
                    .padding(end = 16.dp)
                    .border(
                        BorderStroke(
                            1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ), RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(25.dp))
                    .clickable {
                        showEndDateDayPicker = !showEndDateDayPicker
                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = endDate.toLocalDate().toDisplayStringForMonthWithYear(),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy()
                        )
                    }
                }
            }
            Text(
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Selected duration ${selectedDuration} days",
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = modifier,
                text = "Account",
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier
            ) {
                bankAccountList.forEachIndexed { index, bankAccountsClass ->
                    BankChips(
                        index, selectedBankAccount, bankAccountsClass
                    ) { setSelectedChip(it) }
                }
            }
            Spacer(Modifier.height(12.dp))
            ErrorRow(showErrorStatus)
            Spacer(Modifier.height(12.dp))
            FilledTonalButton(
                enabled = !showErrorStatus,
                onClick = { saveButtonClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Apply",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

            }
        }
    }
    if (showStartDateDayPicker) {
        DatePickerModal(startDatePickerState, { setStartDate(it ?: LocalDate.now().toLong()) }) {
            showStartDateDayPicker = !showStartDateDayPicker
        }
    }
    if (showEndDateDayPicker) {
        DatePickerModal(endDatePickerState, { setEndDate(it ?: LocalDate.now().toLong()) }) {
            showEndDateDayPicker = !showEndDateDayPicker
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FilterBottomSheetContentPreview() {
    FilterBottomSheetContent(
        Modifier.padding(horizontal = 16.dp),
        { },
        listOf(),
        emptyBank,
        {},
        0L,
        {},
        0L,
        {}, 3L,
        false, {}
    )
}

