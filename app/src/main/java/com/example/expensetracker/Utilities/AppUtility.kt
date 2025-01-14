package com.example.expensetracker.Utilities

import com.example.expensetracker.Model.TotalExpenseIncomeClass
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


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

fun getPreviousAndNextDays(date: LocalDate): MutableList<String> {
    val allDates = mutableListOf<String>()

    for (i in 25 downTo 1) {
        val previousDate = date.minusDays(i.toLong())  // Subtract days for previous dates
        val formattedPrevDate = previousDate.toNormalString()
        allDates.add(formattedPrevDate)
    }

    allDates.add(date.toNormalString())

    for (i in 1..25) {
        val nextDate = date.plusDays(i.toLong())  // Add days for next dates
        val formattedNextDate = nextDate.toNormalString()
        allDates.add(formattedNextDate)
    }
    return allDates
}

fun String.toLocalDate() = run { LocalDate.parse(this) }

fun LocalDate.toNormalString() = run { this.format(DateTimeFormatter.ISO_LOCAL_DATE) }