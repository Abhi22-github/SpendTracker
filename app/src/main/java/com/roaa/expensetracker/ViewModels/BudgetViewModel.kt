package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.BudgetRepository
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class DaileBudgetState {
    NORMAL,
    MIDDLE,
    END,
    OVERSPEND
}

@HiltViewModel
class BudgetViewModel @Inject constructor(private val budgetRepository: BudgetRepository) :
    ViewModel() {

    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getCurrentBudget(): Flow<BudgetModelClass> {
        return budgetRepository.getCurrentBudget
    }

    fun getCurrentBudgetWithDetails(): Flow<BudgetWithDayDetails?> {
        return budgetRepository.getCurrentBudgetWithDays
    }

    fun getAllBudgets(): Flow<List<BudgetModelClass>> {
        return budgetRepository.allBudget
    }

    fun deleteBudget(budgetModelClass: BudgetModelClass) {
        viewModelScope.launch {
            budgetRepository.delete(budgetModelClass)
        }
    }


    fun createObjectAndStoreIt(
        totalAMountForMonth: Float,
        totalAmountPerDay: Float,
        totalDaysRemaining: Long,
        currentMonthName: String,
        budgeMonthStartDate: Long,
        budgetMonthEndDate: Long,
        validDatesListFromLong: List<Long>
    ) {
        val budgeObject = BudgetModelClass(
            budgetId = 0L,
            budgetAmountForMonth = totalAMountForMonth,
            budgetAmountPerDay = totalAmountPerDay,
            budgetTotalDays = totalDaysRemaining,
            currentMonthName = currentMonthName,
            budgetMonthStartDate = budgeMonthStartDate,
            budgetMonthEndDate = budgetMonthEndDate,
            isActive = true
        )
        saveBudget(budgeObject, validDatesListFromLong)
    }

    private fun saveBudget(budgetModelClass: BudgetModelClass, validDatesListFromLong: List<Long>) {
        if (budgetModelClass.budgetId == 0L) {
            //adding new bank Accounts
            viewModelScope.launch {
                budgetRepository.insertWithDetails(budgetModelClass,validDatesListFromLong)
            }
        } else {
            //updating existing account
            viewModelScope.launch {
                budgetRepository.update(budgetModelClass)
            }
        }
    }


    fun loading() {
        _uiState.value = UiState.Loading
    }

    fun completed() {
        _uiState.value = UiState.Success
    }

    fun error(error: Throwable) {
        _uiState.value = UiState.Error(error.toString())
        Log.d("Hello Error reason", error.toString())
    }


}