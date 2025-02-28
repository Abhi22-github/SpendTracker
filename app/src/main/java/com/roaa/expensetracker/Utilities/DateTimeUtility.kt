package com.roaa.expensetracker.Utilities

import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import okhttp3.internal.toLongOrDefault
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale


val monthWithYearFormat = DateTimeFormatter.ofPattern("MMM uuuu", Locale.getDefault())
val fullMonthNameWithYearFormat = DateTimeFormatter.ofPattern("MMMM uuuu", Locale.getDefault())
val fullMonthNameFormat = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
val yearMonthDateLongFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault())
val showDateFormat = DateTimeFormatter.ofPattern("dd MMM,yyyy",Locale.getDefault())
val onlyDayName = DateTimeFormatter.ofPattern("EEE",Locale.getDefault())
val only2LetterDate = DateTimeFormatter.ofPattern("dd",Locale.getDefault())

fun getFirstAndLastDateOfGivenPeriod(prevMonth: LocalDate, nextMonth: LocalDate): Pair<Long, Long> {

    val firstDate = prevMonth.withDayOfMonth(1)

    val lastDate = nextMonth.withDayOfMonth(nextMonth.lengthOfMonth())

    return Pair(firstDate.format(yearMonthDateLongFormat).toLong(), lastDate.format(yearMonthDateLongFormat).toLong())
}
/*
* Extension Functions for Local Date
*/
fun LocalDate.toLong(): Long {
    return this.format(yearMonthDateLongFormat).toLong()
}

fun Long.toLocalDate(): LocalDate {
    val dateString = this.toString()
    return LocalDate.parse(dateString, yearMonthDateLongFormat)
}


fun LocalDate.toDisplayStringForMonth(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM")).toString()
}

fun LocalDate.toDisplayStringForMonthWithYear(): String {
    return this.format(showDateFormat).toString()
}
fun Long.toDisplayDate(): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter: SimpleDateFormat = SimpleDateFormat("MMM dd,yyyy")

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(this)
    return formatter.format(calendar.getTime())
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

fun LocalDate.toDateWithDayName(): String {
    return "${this.format(showDateFormat)}"
}

fun parseAmount(amount: Float): String {
    val formatter = DecimalFormat("##,##,##,###")
    return formatter.format(amount)
}


fun convertTotalExpenseIncomeClassToMap(totalExpenseIncomeClassList: List<TotalExpenseIncomeClass>): HashMap<Long, Pair<Float, Float>> {
    val map = hashMapOf<Long, Pair<Float, Float>>()
    totalExpenseIncomeClassList.forEach { it ->
        map[it.date] = Pair(it.totalExpense, it.totalIncome)
    }
    return map
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

fun getPreviousAndNext500Months(date: LocalDate): List<String> {
    val allMonths = mutableListOf<String>()
    for (i in 250 downTo 1) {
        val previousMonth = date.minusMonths(i.toLong())  // Subtract days for previous dates
        val formattedPrevMonth =
            previousMonth.format(monthWithYearFormat)
        allMonths.add(formattedPrevMonth)
    }

    allMonths.add(date.format(monthWithYearFormat))

    for (i in 1..250) {
        val nextMonth = date.plusMonths(i.toLong())  // Add days for next dates
        val formattedNextMonth = nextMonth.format(monthWithYearFormat)
        allMonths.add(formattedNextMonth)
    }
    return allMonths
}

fun getDayDifference(startDate: LocalDate,endDate: LocalDate): Long {
return ChronoUnit.DAYS.between(startDate, endDate)
}

fun getPreviousAndNext10Days(date: LocalDate): List<String> {
    val allDates = mutableListOf<String>()
    for (i in 5 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate =
            "${previousDate.format(onlyDayName)},${previousDate.format(only2LetterDate)}"
        allDates.add(formattedPrevDate)
    }

    allDates.add("${date.format(onlyDayName)},${date.format(only2LetterDate)}")

    for (i in 1..5) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = "${nextDate.format(onlyDayName)},${nextDate.format(only2LetterDate)}"
        allDates.add(formattedNextDate)
    }
    return allDates
}

fun getPreviousAndNext500Days(date: LocalDate): List<String> {
    val allDates = mutableListOf<String>()
    for (i in 250 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate =
            "${previousDate.format(onlyDayName)},${previousDate.format(only2LetterDate)}"
        allDates.add(formattedPrevDate)
    }

    allDates.add("${date.format(onlyDayName)},${date.format(only2LetterDate)}")

    for (i in 1..250) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = "${nextDate.format(onlyDayName)},${nextDate.format(only2LetterDate)}"
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

fun LocalDate.toDisplayDate() = run { this.format(DateTimeFormatter.ofPattern("MMM dd,YYYY")) }

fun convertMillisToDateString(millis: Long): String {
    val format = SimpleDateFormat("dd MMM YYYY", Locale.getDefault())
    val date = Date(millis)
    return format.format(date)
}

fun extractNumbers(input: String): Long {
    return input.filter { it.isDigit() }.toLongOrDefault(0L)
}

fun getCurrentMonthName() =
    run { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault()) }

fun getRemainingDaysInCurrentMonth(): Long = run {
    val currentDate = LocalDate.now()
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    return java.time.temporal.ChronoUnit.DAYS.between(currentDate, lastDayOfMonth)
}

fun getCurrentDate() = run { LocalDate.now().toLong() }

fun getMonthEndDate() =
    run { LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toLong() }

fun getDaysRemaining(endDate: LocalDate): Long = run {
    return Math.abs(java.time.temporal.ChronoUnit.DAYS.between(endDate, LocalDate.now()))
}

val dayNameList = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

fun datesListForMonth(
    localDate: LocalDate,
    budgetMonthStartDate: Long,
    budgetMonthEndDate: Long,

    ): List<CalenderDayState> = run {
    val firstDayOfMonth = localDate.withDayOfMonth(1)
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())
    val startDayOfWeek = firstDayOfMonth.dayOfWeek

    val budgetStartDate = budgetMonthStartDate.toString().takeLast(2).toInt()
    val budgetEndDate = budgetMonthEndDate.toString().takeLast(2).toInt()
    val calendarGrid = mutableListOf<CalenderDayState>()


    for (i in 1..startDayOfWeek.value) {
        calendarGrid.add(CalenderDayState(-1, 0L, false, DayState.NOT_STARTED))
    }

    for (i in 1..lastDayOfMonth.dayOfMonth) {
        if (i in budgetStartDate..budgetEndDate) {
            calendarGrid.add(
                CalenderDayState(
                    i,
                    firstDayOfMonth.plusDays(i.toLong() - 1).toLong(),
                    true,
                    DayState.IN_LIMIT
                )
            )
        } else {
            calendarGrid.add(
                CalenderDayState(
                    i,
                    firstDayOfMonth.plusDays(i.toLong() - 1).toLong(),
                    false,
                    DayState.NOT_STARTED
                )
            )
        }
    }
    calendarGrid
}

fun getCalendarForMonthFromDateIncludingPrevMont(localDate: LocalDate): List<LocalDate> {
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

fun getValidDatesListFromLong(startDate: Long, endDate: Long): List<Long> {
    val dateList = mutableListOf<Long>()
    for (i in 0..endDate - startDate) {
        dateList.add(startDate.toLocalDate().plusDays(i).toLong())
    }
    return dateList
}

val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM uuuu"))
val currentDay = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"))
val currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu"))

fun getFirstAndLastMonth(month: String) = run {
    val yearMonth = YearMonth.parse(month.trim(), monthWithYearFormat)
    Pair(yearMonth.atDay(1).toLong(), yearMonth.atEndOfMonth().toLong())
}

fun getMonthFromLocalDate(date: LocalDate) = run {
    date.format(monthWithYearFormat)
}

fun convertToWholeMonthName(monthWithYear: String): String = run {
    val yearMonth = YearMonth.parse(monthWithYear.trim(), monthWithYearFormat)
    yearMonth.format(fullMonthNameFormat)
}

fun convertMonthShortToFullName(monthWithYear: String) = run {
    val yearMonth = YearMonth.parse(monthWithYear.trim(), monthWithYearFormat)
    yearMonth.format(fullMonthNameWithYearFormat)
}

fun getAllDatesWithDayNameForMonth(monthYear: String): List<Pair<String, Long>> {
    // Parse the input string (e.g., "Feb 2024") into a YearMonth object
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val yearMonth = YearMonth.parse(monthYear, formatter)

    // Generate all dates in the month with day names
    return (1..yearMonth.lengthOfMonth()).map { day ->
        val date = yearMonth.atDay(day)
        val dayOfMonth = date.dayOfMonth // Get the day of the month (e.g., 1, 2, 3)
        val dayName = date.dayOfWeek.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        ) // Full day name (e.g., "Thursday")
        Pair("$dayOfMonth $dayName", date.toLong()) // Format: "01 Thursday"
    }
}

fun calculateEffectivePage(month: String) = run {
    val yearMonth: YearMonth = YearMonth.parse(month.trim(), monthWithYearFormat)
    val currentMonth: YearMonth = YearMonth.now()
    currentMonth.until(yearMonth, ChronoUnit.MONTHS)
}