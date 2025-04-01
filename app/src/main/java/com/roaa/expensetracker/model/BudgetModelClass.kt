package com.roaa.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "budget_table")
data class BudgetModelClass(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long,
    val totalBudgetAmount: BigDecimal,
    val budgetAmountPerDay: BigDecimal,
    val budgetTotalDays: Long,
    val budgetStartDate: Long,
    val budgetEndDate: Long,
    val restDistributionType:Int,
    val notificationForBudgetUsage:BigDecimal,
    var isActive: Boolean

)
