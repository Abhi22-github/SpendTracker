package com.roaa.expensetracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "budget_table")
data class BudgetModelClass(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long,
    val totalBudgetAmount: Float,
    val budgetAmountPerDay: Float,
    val budgetTotalDays: Long,
    val budgetStartDate: Long,
    val budgetEndDate: Long,
    @ColumnInfo(defaultValue = "1")
    val restDistributionType:Int,
    @ColumnInfo(defaultValue = "20")
    val notificationForBudgetUsage:Float,
    val isActive: Boolean

)
