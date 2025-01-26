package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.BankAccountRepository
import com.roaa.expensetracker.Model.BankAccountsClass
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
    private var _allBankAccountsList = MutableStateFlow<List<BankAccountsClass>>(listOf())
    val allBankAccountList: MutableStateFlow<List<BankAccountsClass>> = _allBankAccountsList

    init {
        getAllBankAccounts()
    }

    fun getAllBankAccounts() {
        viewModelScope.launch {
            loading()
            bankAccountRepository.allBankAccounts.catch { error(it) }.collect {
                _allBankAccountsList.value = it
                completed()
            }
        }
    }

    fun deleteBankAccount(bankAccountsClass: BankAccountsClass) {
        viewModelScope.launch {
            bankAccountRepository.delete(bankAccountsClass)
        }
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