package com.roaa.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Composables.changeThemeSystemWide
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.UiDateModels.CurrencyClass
import com.roaa.expensetracker.Utilities.PreferenceManger.CURRENT_BUDGET_DURATION
import com.roaa.expensetracker.Utilities.PreferenceManger.CURRENT_BUDGET_END_DATE
import com.roaa.expensetracker.Utilities.PreferenceManger.CURRENT_BUDGET_MONTH_NAME
import com.roaa.expensetracker.Utilities.PreferenceManger.CURRENT_BUDGET_START_DATE
import com.roaa.expensetracker.Utilities.PreferenceManger.IS_APP_FIRST_STARTUP
import com.roaa.expensetracker.Utilities.PreferenceManger.IS_BUDGET_SET
import com.roaa.expensetracker.Utilities.PreferenceManger.IS_ONE_DEFAULT_CATEGORY_SET
import com.roaa.expensetracker.Utilities.PreferenceManger.LAST_USED_EXPENSE_CATEGORY_ID
import com.roaa.expensetracker.Utilities.PreferenceManger.LAST_USED_INCOME_CATEGORY_ID
import com.roaa.expensetracker.Utilities.PreferenceManger.PRIMARY_BANK_ACCOUNT
import com.roaa.expensetracker.Utilities.PreferenceManger.PreferenceManager
import com.roaa.expensetracker.Utilities.PreferenceManger.SHOW_EXPERIMENTAL_COMPONENTS
import com.roaa.expensetracker.Utilities.PreferenceManger.THEME_MODE
import com.roaa.expensetracker.Utilities.PreferenceManger.TOTAL_BUDGET_FOR_MONTH
import com.roaa.expensetracker.Utilities.PreferenceManger.TOTAL_BUDGET_PER_DAY
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


    fun setExperimentalComponentsState(state: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(state, SHOW_EXPERIMENTAL_COMPONENTS)
        }
    }

    val showExperimentalComponent = preferenceManager.getBooleanValue(SHOW_EXPERIMENTAL_COMPONENTS)

    fun setPrimaryAccountNumber(position: Long) {
        viewModelScope.launch {
            preferenceManager.saveLongValue(position, PRIMARY_BANK_ACCOUNT)
        }
    }

    val getPrimaryAccountNumber = preferenceManager.getLongValue(PRIMARY_BANK_ACCOUNT)

    fun setPrimaryAccount(bankAccountsClass: BankAccountsClass) {
        viewModelScope.launch {
            preferenceManager.setPrimaryBankAccount(bankAccountsClass)
        }
    }

    val getPrimaryAccount = preferenceManager.getPrimaryBankAccount()


    fun saveBudget(
        totalAmountForMonth: Float,
        totalAmountPerDay: Float,
        totalDaysRemaining: Long,
        currentMonthName: String,
        budgeMonthStartDate: Long,
        budgetMonthEndDate: Long
    ) {
        viewModelScope.launch {
            preferenceManager.saveFloatValue(totalAmountForMonth, TOTAL_BUDGET_FOR_MONTH)
            preferenceManager.saveFloatValue(totalAmountPerDay, TOTAL_BUDGET_PER_DAY)
            preferenceManager.saveStringValue(currentMonthName, CURRENT_BUDGET_MONTH_NAME)
            preferenceManager.saveLongValue(totalDaysRemaining, CURRENT_BUDGET_DURATION)
            preferenceManager.saveLongValue(budgeMonthStartDate, CURRENT_BUDGET_START_DATE)
            preferenceManager.saveLongValue(budgetMonthEndDate, CURRENT_BUDGET_END_DATE)
        }
    }

    val getTotalAmountPerDay = preferenceManager.getFloatValue(TOTAL_BUDGET_PER_DAY)

    fun setBudgetState(budgetState: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(budgetState, IS_BUDGET_SET)
        }
    }

    val isBudgetSet = preferenceManager.getBooleanValue(IS_BUDGET_SET)

    fun setFirstStartupCompleted() {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(true, IS_APP_FIRST_STARTUP)
        }
    }

    val isNotFirstStartup = preferenceManager.getBooleanValue(IS_APP_FIRST_STARTUP)


    fun setCurrency(currencyClass: CurrencyClass) {
        viewModelScope.launch {
            preferenceManager.setCurrency(currencyClass)
        }
    }

    val getCurrency = preferenceManager.getCurrency()

    fun setLastUsedExpenseCategoryId(id: Long) {
        viewModelScope.launch {
            preferenceManager.saveLongValue(id, LAST_USED_EXPENSE_CATEGORY_ID)
        }
    }

    val getLastExpenseCategory = preferenceManager.getLongValue(LAST_USED_EXPENSE_CATEGORY_ID)

    fun setLastUsedIncomeCategoryId(id: Long) {
        viewModelScope.launch {
            preferenceManager.saveLongValue(id, LAST_USED_INCOME_CATEGORY_ID)
        }
    }

    val getLastIncomeCategory = preferenceManager.getLongValue(LAST_USED_INCOME_CATEGORY_ID)

    fun setPreDefaultCategoryStatus(state: Boolean) {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(state, IS_ONE_DEFAULT_CATEGORY_SET)
        }
    }

    val getPreDefaultCategoryStatus = preferenceManager.getBooleanValue(IS_ONE_DEFAULT_CATEGORY_SET)

}