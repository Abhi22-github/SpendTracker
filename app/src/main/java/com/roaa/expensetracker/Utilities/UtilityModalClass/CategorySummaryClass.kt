package com.roaa.expensetracker.Utilities.UtilityModalClass


import androidx.compose.ui.graphics.Color
import com.roaa.expensetracker.Model.CategoryClass
import java.math.BigDecimal

data class CategorySummaryClass(
    val categoryClass: CategoryClass,
    val transactionCount: Int,
    var percentage: BigDecimal,
    val totalAmount: BigDecimal,
    var color: Color
)
