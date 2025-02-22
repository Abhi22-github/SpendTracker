package com.roaa.expensetracker.Model.UiDateModels

data class BarChartExpenseModel(
    val date: Long,
    val dayName: String,
    val expenseAmount: Float,
    val incomeAmount: Float
)
