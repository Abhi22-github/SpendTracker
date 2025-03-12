package com.roaa.expensetracker.model.uiDataModels

import java.math.BigDecimal

data class BarChartExpenseModel(
    val date: Long,
    val dayName: String,
    val expenseAmount: BigDecimal,
    val incomeAmount: BigDecimal
)
