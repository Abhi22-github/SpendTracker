package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Composables.utils.DistributionMethod
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Utilities.UiState
import com.roaa.expensetracker.Database.BudgetRepository
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
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

    fun getBudgetWithDays(budgetId: Long): Flow<BudgetWithDayDetails?> {
        return budgetRepository.getBudgetWithDays(budgetId)
    }

    fun getAllBudgets(): Flow<List<BudgetModelClass>> {
        return budgetRepository.allBudget
    }

    fun deleteBudget(budgetModelClass: BudgetModelClass) {
        viewModelScope.launch {
            budgetRepository.delete(budgetModelClass)
        }
    }

    fun updateBudget(budgetModelClass: BudgetModelClass){
        viewModelScope.launch {
            budgetRepository.update(budgetModelClass)
        }
    }


    fun createObjectAndStoreIt(
        budgetSummaryWithDay: BudgetWithDayDetails,
        totalAMountForMonth: BigDecimal,
        totalAmountPerDay: BigDecimal,
        totalDaysRemaining: Long,
        budgeMonthStartDate: Long,
        budgetMonthEndDate: Long,
        restDistributionValue: DistributionMethod,
        notificationUsageValue: BigDecimal,
        validDatesListFromLong: List<Long>
    ) {
        val budgeObject = BudgetModelClass(
            budgetId = budgetSummaryWithDay.budgetSummary.budgetId,
            totalBudgetAmount = totalAMountForMonth,
            budgetAmountPerDay = totalAmountPerDay,
            budgetTotalDays = totalDaysRemaining,
            budgetStartDate = budgeMonthStartDate,
            budgetEndDate = budgetMonthEndDate,
            restDistributionType = when (restDistributionValue) {
                DistributionMethod.DEFAULT -> 1
                DistributionMethod.SPILLOVER -> 2
            },
            notificationForBudgetUsage = notificationUsageValue,
            isActive = true
        )
        saveBudget(budgeObject, budgetSummaryWithDay, validDatesListFromLong)
    }

    private fun saveBudget(
        budgetModelClass: BudgetModelClass,
        budgetSummaryWithDay: BudgetWithDayDetails,
        validDatesListFromLong: List<Long>
    ) {
        if (budgetModelClass.budgetId == 0L) {
            //adding new bank Accounts
            viewModelScope.launch {
                budgetRepository.insertWithDetails(budgetModelClass, validDatesListFromLong)
            }
        } else {
            //updating existing account
            viewModelScope.launch {
                budgetRepository.updateWithDetails(budgetModelClass,validDatesListFromLong,budgetSummaryWithDay.budgetAllDays)
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