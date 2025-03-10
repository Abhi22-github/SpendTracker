package com.roaa.expensetracker.Hilt

import com.roaa.expensetracker.ViewModels.AnimationViewModel
import com.roaa.expensetracker.ViewModels.BankAccountsViewModel
import com.roaa.expensetracker.ViewModels.BudgetDayViewModel
import com.roaa.expensetracker.ViewModels.BudgetViewModel
import com.roaa.expensetracker.ViewModels.CategoryViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel

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