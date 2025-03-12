package com.roaa.expensetracker.utilities.utilityModalClass

import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.BudgetModelClass
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.model.uiDataModels.CurrencyClass
import com.roaa.expensetracker.model.uiDataModels.TotalAmountClass
import com.roaa.expensetracker.model.uiDataModels.TotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.Constants.CASH
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import java.math.BigDecimal

val emptyBank = BankAccountsClass(
    1L,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
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
    totalBudgetAmount = BigDecimal.ZERO,
    budgetAmountPerDay = BigDecimal.ZERO,
    budgetTotalDays = 0L,
    budgetStartDate = 20250101,
    budgetEndDate = 20250131,
    restDistributionType = 1,
    notificationForBudgetUsage = BigDecimal(20),
    isActive = false
)

val emptyBudgetDayClass = BudgetDayModelClass(
    budgetDayId = 0L,
    date = 0L,
    budgetAmount = BigDecimal.ZERO,
    totalExpense = BigDecimal.ZERO,
    totalIncome = BigDecimal.ZERO,
    totalExpenseTransactionCount = 0L,
    totalIncomeTransactionCount = 0L,
    budgetId = 0L
)

val emptyTotalAmountClass = TotalAmountClass(20250101L,BigDecimal.ZERO)

val emptyTotalExpenseIncomeClass = TotalExpenseIncomeClass(20250101L, BigDecimal.ZERO, BigDecimal.ZERO)

val emptyTransactionClass = TransactionClass(0L,EXPENSE, BigDecimal.ZERO, "", 0L, 20250101L, true,1L, 1L)

val emptyTransactionWithDetailsClass = TransactionWithDetails(
    emptyTransactionClass,
    emptyCategoryClass, emptyBank
)

val firstSampleClass = CategoryClass(
    -1, "Select Category", 1, -99, EXPENSE, isActive = false
)

val defaultCurrency = CurrencyClass("India", "INR", "Indian Rupees", "₹","🇮🇳" )
