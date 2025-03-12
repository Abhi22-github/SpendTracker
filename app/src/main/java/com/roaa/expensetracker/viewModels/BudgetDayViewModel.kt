package com.roaa.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.database.BudgetDayRepository
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.BudgetModelClass
import com.roaa.expensetracker.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class BudgetDayViewModel @Inject constructor(private val budgetDayRepository: BudgetDayRepository) :
    ViewModel() {

    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    fun getAllBudgets(): Flow<List<BudgetDayModelClass>> {
        return budgetDayRepository.allDaysForAllBudgets
    }

    fun deleteBudget(budgetDayModelClass: BudgetDayModelClass) {
        viewModelScope.launch {
            budgetDayRepository.delete(budgetDayModelClass)
        }
    }

    fun createObjectAndStoreIt(
        totalAMountForMonth: BigDecimal,
        totalAmountPerDay: BigDecimal,
        totalDaysRemaining: Long,
        currentMonthName: String,
        budgeMonthStartDate: Long,
        budgetMonthEndDate: Long
    ) {
        val budgeObject = BudgetModelClass(
            budgetId = 0L,
            totalBudgetAmount = totalAMountForMonth,
            budgetAmountPerDay = totalAmountPerDay,
            budgetTotalDays = totalDaysRemaining,
            budgetStartDate = budgeMonthStartDate,
            budgetEndDate = budgetMonthEndDate,
            restDistributionType = 1,
            notificationForBudgetUsage = BigDecimal(20),
            isActive = true
        )
        // saveBudget(budgeObject)
    }


    fun insertBudgetDays(budgetDayModelClass: BudgetDayModelClass) {
        //updating existing account
        viewModelScope.launch {
            budgetDayRepository.insert(budgetDayModelClass)
        }
    }


    fun updateBudgetDays(budgetDayModelClass: BudgetDayModelClass) {
        //updating existing account
        viewModelScope.launch {
            budgetDayRepository.update(budgetDayModelClass)
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
    }


}