package com.roaa.expensetracker.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "budget_day_table")
data class BudgetDayModelClass(
    @PrimaryKey(autoGenerate = true)
    val budgetDayId:Long,
    val date: Long,
    val budgetAmount: BigDecimal ,
    var totalExpense: BigDecimal,
    var totalIncome: BigDecimal,
    val totalExpenseTransactionCount: Long = 0L,
    val totalIncomeTransactionCount: Long = 0L,
    val budgetId: Long,
)
