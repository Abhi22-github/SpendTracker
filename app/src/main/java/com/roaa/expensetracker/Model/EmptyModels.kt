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
    CASH
)
val emptyCategoryClass = CategoryClass(
    categoryId = 1L,
    categoryName = "Default",
    categoryColorNumber = 1,
    categoryIconNumber = 99,
    categoryType = EXPENSE
)
val emptyTransactionClass = TransactionClass(EXPENSE, 0.00, "", 0L, 0L, 1L, 1L)

val firstSampleClass = CategoryClass(
    -1, "Select Category", 1, -99, EXPENSE
)