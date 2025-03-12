package com.roaa.expensetracker.hilt

import com.roaa.expensetracker.viewModels.AnimationViewModel
import com.roaa.expensetracker.viewModels.BankAccountsViewModel
import com.roaa.expensetracker.viewModels.BudgetDayViewModel
import com.roaa.expensetracker.viewModels.BudgetViewModel
import com.roaa.expensetracker.viewModels.CategoryViewModel
import com.roaa.expensetracker.viewModels.PreferencesViewModel
import com.roaa.expensetracker.viewModels.TransactionsViewModel
import com.roaa.expensetracker.viewModels.UiViewModel

class AllViewModel(
    val transactionsViewModel: TransactionsViewModel,
    val categoryViewModel: CategoryViewModel,
    val preferencesViewModel: PreferencesViewModel,
    val uiViewModel: UiViewModel,
    val budgetViewModel: BudgetViewModel,
    val budgetDayViewModel: BudgetDayViewModel,
    val bankAccountsViewModel: BankAccountsViewModel,
    val animationViewModel: AnimationViewModel
    // Add all 30 ViewModels
)