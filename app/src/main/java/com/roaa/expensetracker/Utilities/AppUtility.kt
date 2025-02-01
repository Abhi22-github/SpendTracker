package com.roaa.expensetracker.Utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import com.roaa.expensetracker.Composables.Navigation.NavRoutes
import com.roaa.expensetracker.Model.NavigationItems
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import okhttp3.internal.toLongOrDefault
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun getFirstAndLastDateOfGivenPeriod(prevMonth: LocalDate, nextMonth: LocalDate): Pair<Long, Long> {

    // First date of the current month
    val firstDate = prevMonth.withDayOfMonth(1)

    // Last date of the current month
    val lastDate = nextMonth.withDayOfMonth(nextMonth.lengthOfMonth())

    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return Pair(firstDate.format(formatter).toLong(), lastDate.format(formatter).toLong())
}

fun LocalDate.toLong(): Long {
    return this.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toLong()
}

fun Long.toLocalDate(): LocalDate {
    val dateString = this.toString()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return LocalDate.parse(dateString, formatter)
}

fun LocalDateToString(localDate: LocalDate): String {
    return localDate.format(DateTimeFormatter.ofPattern("dd MMM")).toString()
}

fun parseAmount(amount: Long): String {
    val formatter = DecimalFormat("##,##,##,###")
    return formatter.format(amount)
}

fun getDateFromMillis(milliSeconds: Long): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter: SimpleDateFormat = SimpleDateFormat("MMM dd,yyyy")

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.getTime())
}

fun convertTotalExpenseIncomeClassToMap(totalExpenseIncomeClassList: List<TotalExpenseIncomeClass>): HashMap<Long, Pair<Long, Long>> {
    val map = hashMapOf<Long, Pair<Long, Long>>()
    totalExpenseIncomeClassList.forEach { it ->
        map[it.date] = Pair(it.totalExpense, it.totalIncome)
    }
    return map
}

fun LocalDate.toLongMillis(): Long {
    // Convert LocalDate to LocalDateTime at midnight (start of the day)
    val localDateTime = this.atStartOfDay()

    // Convert LocalDateTime to Instant (UTC time)
    val instant = localDateTime.toInstant(ZoneOffset.UTC)

    // Return the milliseconds from the epoch (1970-01-01T00:00:00Z)
    return instant.toEpochMilli()
}

fun Long.LongMillisToNoralLong(): Long {
    // Convert milliseconds to Instant
    val instant = Instant.ofEpochMilli(this)
    // Convert Instant to LocalDate (using UTC)
    return instant.atZone(ZoneOffset.UTC).toLocalDate().toLong()
}

fun Float.clamp(min: Float, max: Float): Float =
    (1f - ((this.coerceIn(min, max) - min) / (max - min)))

fun getNext10Dates(date: LocalDate): List<String> {
    val next10Dates = mutableListOf<String>()

    for (i in 1..10) {
        val nextDate = date.plusDays(i.toLong())
        val formattedDate = nextDate.toNormalString()
        next10Dates.add(formattedDate)
    }
    return next10Dates
}

fun getPrev10Dates(date: LocalDate): List<String> {

    val prev10Dates = mutableListOf<String>()

    for (i in 1..10) {
        val nextDate = date.plusDays(i.toLong())
        val formattedDate = nextDate.toNormalString()
        prev10Dates.add(formattedDate)
    }
    return prev10Dates
}

fun getPreviousAndNext500Days(date: LocalDate): List<String> {
    val allDates = mutableListOf<String>()
    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    val DateFormatter = DateTimeFormatter.ofPattern("dd")

    for (i in 250 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate =
            "${previousDate.format(dayFormatter)},${previousDate.format(DateFormatter)}"
        allDates.add(formattedPrevDate)
    }

    allDates.add("${date.format(dayFormatter)},${date.format(DateFormatter)}")

    for (i in 1..250) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = "${nextDate.format(dayFormatter)},${nextDate.format(DateFormatter)}"
        allDates.add(formattedNextDate)
    }
    return allDates
}

fun getCalendarForMonthFromDate(localDate: LocalDate): List<LocalDate> {
    // Get the first day of the month
    val firstDayOfMonth = localDate.withDayOfMonth(1)
    // Get the last day of the month
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())

    // Calculate the start day of the week for the first day of the month
    val startDayOfWeek = firstDayOfMonth.dayOfWeek

    // Prepare the list to hold the calendar grid
    val calendarGrid = mutableListOf<LocalDate>()

    // Step 1: Add dates from the previous month to fill the first row
    val prevMonthLastDay = firstDayOfMonth.minusDays(1)
    val prevMonthStartDate = firstDayOfMonth.minusDays(startDayOfWeek.value.toLong())
    var prevMonthDate = prevMonthStartDate
    while (prevMonthDate.isBefore(firstDayOfMonth)) {
        calendarGrid.add(prevMonthDate)
        prevMonthDate = prevMonthDate.plusDays(1)
    }

    // Step 2: Add all the dates of the current month
    var currentDate = firstDayOfMonth
    while (currentDate.isBefore(lastDayOfMonth.plusDays(1))) {
        calendarGrid.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    // Step 3: Add dates from the next month to fill the grid to 42 dates
    val nextMonthDate = lastDayOfMonth.plusDays(1)
    var nextDate = nextMonthDate
    while (calendarGrid.size < 42) { // Ensure the grid has exactly 42 dates
        calendarGrid.add(nextDate)
        nextDate = nextDate.plusDays(1)
    }

    return calendarGrid
}


fun getPreviousAndNextDays(date: LocalDate): MutableList<String> {
    val allDates = mutableListOf<String>()

    for (i in 50 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate = previousDate.toNormalString()
        allDates.add(formattedPrevDate)
    }

    allDates.add(date.toNormalString())

    for (i in 1..50) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = nextDate.toNormalString()
        allDates.add(formattedNextDate)
    }
    return allDates
}

fun String.toLocalDate() = run { LocalDate.parse(this) }

fun LocalDate.toNormalString() = run { this.format(DateTimeFormatter.ISO_LOCAL_DATE) }

fun convertMillisToDateString(millis: Long): String {
    val format = SimpleDateFormat("dd MMM YYYY", Locale.getDefault())
    val date = Date(millis)
    return format.format(date)
}

fun extractNumbers(input: String): Long {
    return input.filter { it.isDigit() }.toLongOrDefault(0L)
}


val items = listOf(
    NavigationItems(
        title = "List",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = NavRoutes.MainScreen,
    ),
    NavigationItems(
        title = "Month",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        route = NavRoutes.MonthScreen,
    ),
    NavigationItems(
        title = "Day",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit,
        badgeCount = 105,
        route = NavRoutes.DayScreen(LocalDate.now().toLong()),
    ),
    NavigationItems(
        title = "Category",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = NavRoutes.ScreenA,
    ), NavigationItems(
        title = "Bank Accounts",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = NavRoutes.BankAccountScreen,
    ), NavigationItems(
        title = "Statistics",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = NavRoutes.MainScreen,
    ), NavigationItems(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = NavRoutes.SettingScreen,
    )

)