package com.example.expensetracker.Utilities

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun getFirstAndLastDateOfCurrentMonth(): Pair<Long, Long> {
    val currentDate = LocalDate.now()

    // First date of the current month
    val firstDate = currentDate.withDayOfMonth(1)

    // Last date of the current month
    val lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return Pair(firstDate.format(formatter).toLong(), lastDate.format(formatter).toLong())
}

