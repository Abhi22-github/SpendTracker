package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.BankAccountRepository
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Utilities.Constants.PRIMARY
import com.roaa.expensetracker.Utilities.UiState
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    private var _allBankAccountList = MutableStateFlow<MutableList<BankAccountsClass>>(
        mutableListOf()
    )
    val allBankAccountList: StateFlow<MutableList<BankAccountsClass>> =
        _allBankAccountList

    //flow to get all bank accounts except cash
    private var _allBankAccountListExceptCash = MutableStateFlow<MutableList<BankAccountsClass>>(
        mutableListOf()
    )
    val allBankAccountListExceptCash: StateFlow<MutableList<BankAccountsClass>> =
        _allBankAccountListExceptCash

    //flow to get singleBankAccount
    private var _singleBankAccount = MutableStateFlow<BankAccountsClass>(
        emptyBank
    )
    val singleBankAccount: StateFlow<BankAccountsClass> =
        _singleBankAccount

    var selectedBankAccount = MutableStateFlow<BankAccountsClass>(
        emptyBank
    )

    init {
        getAllBankAccounts()
        getAllBankAccountsExceptCash()
        getSingleBankAccount(1)
    }

    fun getSingleBankAccount(id: Long) {
        viewModelScope.launch {
            loading()
            bankAccountRepository.getSingleBankAccount(id).catch { error(it) }.collect {
                _singleBankAccount.value = it
                selectedBankAccount.value = it
                completed()
            }
        }
    }

    fun getAllBankAccounts() {
        viewModelScope.launch {
            loading()
            bankAccountRepository.allBankAccounts.catch { error(it) }.collect {
                _allBankAccountList.value = it.toMutableList()
                completed()
            }
        }
    }

    fun getAllBankAccountsExceptCash() {
        viewModelScope.launch {
            loading()
            bankAccountRepository.allBankAccountsExceptCash.catch { error(it) }.collect {
                _allBankAccountListExceptCash.value = it.toMutableList()
                completed()
            }
        }
    }

    fun getAllBankAccountsExcept(): Flow<List<BankAccountsClass>> {
        return bankAccountRepository.allBankAccounts
    }

    fun getAllBankAccountsExceptCashCompose(): Flow<List<BankAccountsClass>> {
        return bankAccountRepository.allBankAccountsExceptCash
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
            bankAccountId = id,
            initialAmount = bankAmount.toLong(),
            currentAmount = bankAmount.toLong(),
            bankName = bankName,
            cardColorNumber = selectedColor,
            cardIconNumber = 24,
            accountType = PRIMARY,
            isActive = true
        )
        storeBankAccount(bankAccountObj)
    }

    fun storeBankAccount(bankAccountsClass: BankAccountsClass) {
        if (bankAccountsClass.bankAccountId == 0L) {
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

    fun migrateTransactions(firstBank: BankAccountsClass, secondBank: BankAccountsClass) {
        viewModelScope.launch {
            bankAccountRepository.migrateTransactions(firstBank, secondBank)
        }
    }

    fun deleteBankAccountWithTransactions(bank: BankAccountsClass) {
        viewModelScope.launch {
            bankAccountRepository.deleteBankAccountWithTransactions(bank)
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