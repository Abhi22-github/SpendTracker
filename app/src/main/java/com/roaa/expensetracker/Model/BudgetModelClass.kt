package com.roaa.expensetracker.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "budget_table")
data class BudgetModelClass(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long,
    val budgetAmountForMonth: Float,
    val budgetAmountPerDay: Float,
    val budgetTotalDays: Long,
    val currentMonthName: String,
    val budgetMonthStartDate: Long,
    val budgetMonthEndDate: Long,
    val isActive: Boolean

)
