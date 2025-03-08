package com.roaa.expensetracker.Utilities.UtilityModalClass


import androidx.compose.ui.graphics.Color
import com.roaa.expensetracker.Model.CategoryClass

data class CategorySummaryClass(
    val categoryClass: CategoryClass,
    val transactionCount: Int,
    var percentage: Float,
    val totalAmount: Float,
    var color: Color
)
