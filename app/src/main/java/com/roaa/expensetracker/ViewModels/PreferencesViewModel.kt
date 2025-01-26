package com.roaa.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Composables.changeThemeSystemWide
import com.roaa.expensetracker.Utilities.PreferenceManger.IS_BUDGET_SET
import com.roaa.expensetracker.Utilities.PreferenceManger.PRIMARY_BANK_ACCOUNT
import com.roaa.expensetracker.Utilities.PreferenceManger.PreferenceManager
import com.roaa.expensetracker.Utilities.PreferenceManger.SHOW_FORECAST
import com.roaa.expensetracker.Utilities.PreferenceManger.THEME_MODE
import com.roaa.expensetracker.Utilities.PreferenceManger.TOTAL_BUDGET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {
    fun saveTheme(state: String) {
        viewModelScope.launch {
            preferenceManager.saveThemeValue(state, THEME_MODE)
            changeThemeSystemWide(state)
        }
    }

    val getThemeMode = preferenceManager.getThemeValue(THEME_MODE)


    fun setForecastState(state: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(state, SHOW_FORECAST)
        }
    }

    val showForecastBar = preferenceManager.getBooleanValue(SHOW_FORECAST)

    fun setPrimaryAccount(position: Long) {
        viewModelScope.launch {
            preferenceManager.saveLongValue(position, PRIMARY_BANK_ACCOUNT)
        }
    }

    val getPrimaryAccountNumber = preferenceManager.getLongValue(PRIMARY_BANK_ACCOUNT)


    fun saveBudget(budget: Float) {
        viewModelScope.launch {
            preferenceManager.saveFloatValue(budget, TOTAL_BUDGET)
        }
    }

    val getBudgetValue = preferenceManager.getFloatValue(TOTAL_BUDGET)

    fun setBudgetState(budgetState: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(budgetState, IS_BUDGET_SET)
        }
    }

    val isBudgetSet = preferenceManager.getBooleanValue(IS_BUDGET_SET)

}