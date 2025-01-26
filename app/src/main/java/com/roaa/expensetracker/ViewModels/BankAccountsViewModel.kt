package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.BankAccountRepository
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Utilities.Constants.PRIMARY
import com.roaa.expensetracker.Utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankAccountsViewModel @Inject constructor(private val bankAccountRepository: BankAccountRepository) :
    ViewModel() {

    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    //flow to get all bank accounts
    private var _allBankAccountListExceptCash = MutableStateFlow<MutableList<BankAccountsClass>>(
        mutableListOf()
    )
    val allBankAccountListExceptCash: MutableStateFlow<MutableList<BankAccountsClass>> =
        _allBankAccountListExceptCash

    init {
        getAllBankAccounts()
    }

    fun getAllBankAccounts() {
        viewModelScope.launch {
            loading()
            bankAccountRepository.allBankAccountsExceptCash.catch { error(it) }.collect {
                _allBankAccountListExceptCash.value = it.toMutableList()
                completed()
            }
        }
    }

    fun deleteBankAccount(bankAccountsClass: BankAccountsClass) {
        viewModelScope.launch {
            bankAccountRepository.delete(bankAccountsClass)
        }
    }

    fun createObjectAndStoreIt(
        id: Long,
        bankAmount: String,
        bankName: String,
        selectedColor: Int
    ) {
        val bankAccountObj = BankAccountsClass(
            id = id,
            initialAmount = bankAmount.toLong(),
            currentAmount = bankAmount.toLong(),
            bankName = bankName,
            cardColorNumber = selectedColor,
            accountType = PRIMARY
        )
        storeBankAccount(bankAccountObj)
    }

    fun storeBankAccount(bankAccountsClass: BankAccountsClass) {
        if (bankAccountsClass.id == 0L) {
            //adding new bank Accounts
            viewModelScope.launch {
                bankAccountRepository.insert(bankAccountsClass)
            }
        } else {
            //updating existing account
            viewModelScope.launch {
                bankAccountRepository.update(bankAccountsClass)
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