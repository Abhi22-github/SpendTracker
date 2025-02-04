package com.roaa.expensetracker.Utilities

enum class DayState {
    IN_LIMIT,
    NOT_STARTED,
    OVER_LIMIT;
}

data class CalenderDayState(
    val day: Int,
    val dayDate: Long,
    val isInBudget: Boolean,
    var dayState: DayState
)
