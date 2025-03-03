package com.roaa.expensetracker.Utilities.UtilityModalClass

import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants.CASH
import com.roaa.expensetracker.Utilities.Constants.EXPENSE

val emptyBank = BankAccountsClass(
    1L,
    0f,
    0f,
    "Cash",
    1,
    25,
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
    totalBudgetAmount = 0f,
    budgetAmountPerDay = 0f,
    budgetTotalDays = 0L,
    budgetStartDate = 20250101,
    budgetEndDate = 20250131,
    restDistributionType = 1,
    notificationForBudgetUsage = 20f,
    isActive = false
)

val emptyBudgetDayClass = BudgetDayModelClass(
    budgetDayId = 0L,
    date = 0L,
    budgetAmount = 0f,
    totalExpense = 0f,
    totalIncome = 0f,
    totalExpenseTransactionCount = 0L,
    totalIncomeTransactionCount = 0L,
    budgetId = 0L
)

val emptyTotalExpenseIncomeClass = TotalExpenseIncomeClass(20250101L, 0f, 0f)

val emptyTransactionClass = TransactionClass(0L,EXPENSE, 0f, "", 0L, 20250101L, true,1L, 1L)

val emptyTransactionWithDetailsClass = TransactionWithDetails(
    emptyTransactionClass,
    emptyCategoryClass, emptyBank
)

val firstSampleClass = CategoryClass(
    -1, "Select Category", 1, -99, EXPENSE, isActive = false
)