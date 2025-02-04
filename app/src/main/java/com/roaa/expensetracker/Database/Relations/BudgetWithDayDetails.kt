package com.roaa.expensetracker.Database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass

data class BudgetWithDayDetails(
    @Embedded val budgetSummary: BudgetModelClass,
    @Relation(
        parentColumn = "budgetId",
        entityColumn = "budgetId"
    )
    val budgetAllDays: List<BudgetDayModelClass>,
)