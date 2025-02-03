package com.roaa.expensetracker.Model

import com.roaa.expensetracker.Utilities.Constants.CASH
import com.roaa.expensetracker.Utilities.Constants.EXPENSE

val emptyBank = BankAccountsClass(
    1L,
    0L,
    0L,
    "Cash",
    1,
    99,
    CASH,
    isActive = true
)
val emptyCategoryClass = CategoryClass(
    categoryId = 1L,
    categoryName = "Default",
    categoryColorNumber = 1,
    categoryIconNumber = 99,
    categoryType = EXPENSE,
    isActive = true
)

val emptyBudgetClass = BudgetModelClass(
    budgetId = 0L,
    budgetAmountForMonth = 0f,
    budgetAmountPerDay = 0f,
    budgetTotalDays = 0L,
    currentMonthName = "Month",
    budgetMonthStartDate = 20250101,
    budgetMonthEndDate = 20250131,
    isActive = true
)

val emptyTotalExpenseIncomeClass = TotalExpenseIncomeClass(20250101L,0f,0f)

val emptyTransactionClass = TransactionClass(EXPENSE, 0f, "", 0L, 0L, 1L, 1L)

val firstSampleClass = CategoryClass(
    -1, "Select Category", 1, -99, EXPENSE, isActive = false
)