package com.roaa.expensetracker.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "budget_day_table")
data class BudgetDayModelClass(
    @PrimaryKey(autoGenerate = true)
    val budgetDayId:Long,
    val date: Long,
    val budgetAmount: Float = 0f,
    var totalExpense: Float = 0f,
    var totalIncome: Float = 0f,
    val totalExpenseTransactionCount: Long = 0L,
    val totalIncomeTransactionCount: Long = 0L,
    val budgetId: Long,
)
