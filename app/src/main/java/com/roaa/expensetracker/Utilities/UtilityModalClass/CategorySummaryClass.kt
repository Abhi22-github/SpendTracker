package com.roaa.expensetracker.Utilities.UtilityModalClass

import com.roaa.expensetracker.Model.CategoryClass

data class CategorySummaryClass(
    val categoryClass: CategoryClass,
    val transactionCount: Int,
    val percentage: Float,
    val totalAmount: Float
)
