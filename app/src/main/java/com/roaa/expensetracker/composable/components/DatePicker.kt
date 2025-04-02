package com.roaa.expensetracker.composable.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.roaa.expensetracker.composable.screens.horizontalPadding
import com.roaa.expensetracker.utilities.LongMillisToNormalLong
import com.roaa.expensetracker.utilities.convertTo12HourFormat
import com.roaa.expensetracker.utilities.formatTimeForDisplay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
        DateRangePicker(
            state = dateRangePickerState, title = {
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
fun TimePickerDialog(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    hour: Int,
    minutes: Int
) {

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minutes,
        is24Hour = false,
    )

    /** Determines whether the time picker is dial or input */
    var showDial by remember { mutableStateOf(true) }

    /** The icon used for the icon button that switches from dial to input */
    val toggleIcon = if (showDial) {
        Icons.Rounded.Keyboard
    } else {
        Icons.Rounded.AccessTime
    }

    AdvancedTimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState.hour, timePickerState.minute) },
        toggle = {
            IconButton(onClick = { showDial = !showDial }) {
                Icon(
                    imageVector = toggleIcon,
                    contentDescription = "Time picker type toggle",
                )
            }
        },
    ) {
        if (showDial) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    periodSelectorBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            )
        } else {
            TimeInput(
                colors = TimePickerDefaults.colors(
                    clockDialColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    periodSelectorBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ),
                state = timePickerState,
            )
        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            // tonalElevation = 6.dp,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            modifier =
                Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)

        ) {
            Column(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}


@Composable
fun UpdatedDateTimePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    hour: Int,
    minute: Int,
    saveButtonClicked: (LocalDate, Int, Int) -> Unit,
    dismissDialog: () -> Unit
) {
    var hour by remember { mutableIntStateOf(hour) }
    var minutes by remember { mutableIntStateOf(minute) }
    var selectedDate by remember { mutableStateOf(selectedDate) }

    Dialog(
        onDismissRequest = { dismissDialog() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                .heightIn(min = 200.dp, max = 600.dp),
            shape = RoundedCornerShape(25.dp),
        ) {
            UpdatedDateTimePickerContent(
                hour = hour,
                minute = minutes,
                setTime = { tempHour, tempMinutes ->
                    hour = tempHour
                    minutes = tempMinutes
                },
                selectedDate = selectedDate,
                setSelectedDate = { selectedDate = it },
                saveButtonClicked = {
                    saveButtonClicked(selectedDate, hour, minutes)
                    dismissDialog()
                },
                dismissDialog = { dismissDialog() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatedDateTimePickerContent(
    modifier: Modifier = Modifier,
    hour: Int,
    minute: Int,
    setTime: (Int, Int) -> Unit,
    selectedDate: LocalDate,
    setSelectedDate: (LocalDate) -> Unit,
    saveButtonClicked: () -> Unit,
    dismissDialog: () -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomDatePickerDialog(
                selectedDate,
                onDateSelected = { setSelectedDate(it) },
                onDismiss = { dismissDialog() }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //show Time Picker
                        showTimePicker = !showTimePicker
                    }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontalPadding, 16.dp)

                ) {
                    val (hour12, amPm) = convertTo12HourFormat(hour, minute)
                    val displayTime = formatTimeForDisplay(hour12, minute, amPm)
                    Icon(Icons.Rounded.AccessTime, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = displayTime, style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                }
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
            ) {
                Button(
                    onClick = {
                        dismissDialog()
                    },
                    colors = ButtonDefaults.textButtonColors(),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        saveButtonClicked()
                    },
                    colors = ButtonDefaults.textButtonColors(),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
    if (showTimePicker) {
        TimePickerDialog(
            { hour, minutes ->
                setTime(hour, minutes)
                showTimePicker = !showTimePicker
            },
            { showTimePicker = !showTimePicker },
            hour,
            minute
        )
    }
}

@Composable
fun CustomDatePickerDialog(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 12,
        pageCount = { 24 }
    )
    val visibleMonth by remember {
        derivedStateOf {
            selectedDate.plusMonths(pagerState.currentPage.toLong() - MAX_MONTHS / 2L)
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth()

    ) {
        // Month header

        // Month pager
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Column {
                Text(
                    text = visibleMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    style = typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))

                // Days of week header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                        Text(
                            text = day,
                            style = typography.bodySmall,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                val monthDate = selectedDate.plusMonths(page.toLong() - MAX_MONTHS / 2L)
                MonthCalendar(
                    month = monthDate,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        onDateSelected(date)
                    }
                )
            }
        }
    }

}

@Composable
private fun MonthCalendar(
    month: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfMonth = month.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0

    val days = (0 until 6 * 7).map { index ->
        if (index < firstDayOfWeek || index >= firstDayOfWeek + daysInMonth) {
            null // Empty day
        } else {
            month.withDayOfMonth(index - firstDayOfWeek + 1)
        }
    }
    val weeks = days.chunked(7)
        .takeWhile { week -> week.any { it != null } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        // Split days into weeks (rows of 7 days)
        weeks.forEach { weekDays ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                weekDays.forEach { date ->
                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isCurrentDate = date == LocalDate.now(),
                        onClick = { date?.let(onDateSelected) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate?,
    isSelected: Boolean,
    isCurrentDate: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    }

    val border = when {
        isCurrentDate -> BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
        else -> BorderStroke(0.dp, Color.Transparent)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = date != null, onClick = onClick)
            .border(border, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date?.dayOfMonth?.toString() ?: "",
            color = textColor,
            style = typography.bodyMedium
        )
    }
}

// Constants
private const val MAX_MONTHS = 24 // 12 months before and after current date

@Preview
@Composable
private fun UpdatedDateTimePickerPreview() {
    UpdatedDateTimePickerContent(
        Modifier,
        LocalTime.now().hour,
        LocalTime.now().minute,
        { a, b -> }, LocalDate.now(), {}, {}, {})
}


