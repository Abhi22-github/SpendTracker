package com.roaa.expensetracker.Composables.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.roaa.expensetracker.Utilities.LongMillisToNoralLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis?.LongMillisToNoralLong())
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}