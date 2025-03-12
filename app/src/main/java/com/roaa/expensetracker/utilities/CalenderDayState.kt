package com.roaa.expensetracker.utilities

enum class DayState {
    OUT_OF_BUDGET,
    IN_LIMIT,
    NOT_STARTED,
    OVER_LIMIT;
}

data class CalenderDayState(
    val day: String,
    val dayDate: Long,
    val isInBudget: Boolean,
    var dayState: DayState
)
