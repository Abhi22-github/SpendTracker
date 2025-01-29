package com.roaa.expensetracker.Utilities

import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import okhttp3.internal.toLongOrDefault
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
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

fun LocalDateToLong(localDate: LocalDate): Long {
    return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toLong()
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

fun convertLocalDateToLong(date: LocalDate): Long {
    return date.toString().replace("-", "").toLong()
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

fun getPreviousAndNext10Days(date: LocalDate): List<String> {
    val allDates = mutableListOf<String>()

    for (i in 5 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate = previousDate.toNormalString()
        allDates.add(formattedPrevDate)
    }

    allDates.add(date.toNormalString())

    for (i in 1..5) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = nextDate.toNormalString()
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

    // Number of days in the current month
    val daysInCurrentMonth = firstDayOfMonth.lengthOfMonth()

    // Prepare the list to hold the calendar grid
    val calendarGrid = mutableListOf<LocalDate>()

    // Step 1: Add dates from the previous month to fill the first row
    val prevMonthLastDay = firstDayOfMonth.minusDays(1)
    val prevMonthStartDate =
        prevMonthLastDay.minusDays(startDayOfWeek.value.toLong()) // Corrected calculation
    var prevMonthDate = prevMonthStartDate
    for (i in 0 until startDayOfWeek.value) {
        calendarGrid.add(prevMonthDate)
        prevMonthDate = prevMonthDate.plusDays(1)
    }

    // Step 2: Add all the dates of the current month
    var currentDate = firstDayOfMonth
    while (calendarGrid.size < 42) {
        calendarGrid.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    // Step 3: Add dates from the next month to fill the last row if necessary
    val nextMonthDate = lastDayOfMonth.plusDays(1)
    var nextDate = nextMonthDate
    while (calendarGrid.size < 42) {
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
