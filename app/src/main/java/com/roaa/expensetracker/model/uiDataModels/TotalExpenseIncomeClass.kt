package com.roaa.expensetracker.model.uiDataModels

import java.math.BigDecimal

data class TotalExpenseIncomeClass(
    val date: Long,
    val totalExpense: BigDecimal,
    val totalIncome: BigDecimal
)

data class InfoStatClass(
    val transactionCount: Int,
    val totalExpense: BigDecimal,
    val totalIncome: BigDecimal
)
