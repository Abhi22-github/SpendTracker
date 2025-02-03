package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.BudgetRepository
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(private val budgetRepository: BudgetRepository) :
    ViewModel() {

    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getCurrentBudget(id: Long): Flow<BudgetModelClass> {
        return budgetRepository.getCurrentBudget
    }

    fun getAllBudgets(): Flow<List<BudgetModelClass>> {
        return budgetRepository.allBudget

    }


    fun deleteBudget(budgetModelClass: BudgetModelClass) {
        viewModelScope.launch {
            budgetRepository.delete(budgetModelClass)
        }
    }

//    fun createObjectAndStoreIt(
//        id: Long,
//        bankAmount: String,
//        bankName: String,
//        selectedColor: Int
//    ) {
//        val bankAccountObj = BankAccountsClass(
//            bankAccountId = id,
//            initialAmount = bankAmount.toLong(),
//            currentAmount = bankAmount.toLong(),
//            bankName = bankName,
//            cardColorNumber = selectedColor,
//            cardIconNumber = 24,
//            accountType = PRIMARY
//        )
//        storeBankAccount(bankAccountObj)
//    }

    fun saveBudget(budgetModelClass: BudgetModelClass) {
        if (budgetModelClass.budgetId == 0L) {
            //adding new bank Accounts
            viewModelScope.launch {
                budgetRepository.insert(budgetModelClass)
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