package com.example.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.Utilities.PreferenceManger.IS_BUDGET_SET
import com.example.expensetracker.Utilities.PreferenceManger.PreferenceManager
import com.example.expensetracker.Utilities.PreferenceManger.TOTAL_BUDGET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {

    fun saveBudget(budget: Float) {
        viewModelScope.launch {
            preferenceManager.saveFloatValue(budget, TOTAL_BUDGET)
        }
    }

    val getBudgetValue= preferenceManager.getFloatValue(TOTAL_BUDGET)

    fun setBudgetState(budgetState: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(budgetState, IS_BUDGET_SET)
        }
    }

    val isBudgetSet = preferenceManager.getBooleanValue(IS_BUDGET_SET)

}