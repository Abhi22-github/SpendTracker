package com.roaa.expensetracker.database

import com.roaa.expensetracker.model.BankAccountsClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BankAccountRepository @Inject constructor( private val bankAccountsDao: BankAccountDao ) {


    // below method is to read all category from database.
    val allBankAccountsExceptCash: Flow<List<BankAccountsClass>> = bankAccountsDao.allBankAccountsClassExceptCash
    val allBankAccounts: Flow<List<BankAccountsClass>> = bankAccountsDao.allBankAccountsClass

    // creating a method to insert the data to our database.
    suspend fun insert(bankAccountsClass: BankAccountsClass) {
        bankAccountsDao.insert(bankAccountsClass)
    }

    fun getSingleBankAccount(id: Long): Flow<BankAccountsClass> {
        return bankAccountsDao.getSingleBankAccount(id)
    }

    suspend fun migrateTransactions(firstBank:BankAccountsClass, secondBank:BankAccountsClass){
        bankAccountsDao.migrateTransactionToAnotherBankAccountAndDeleteIt(firstBank,secondBank)
    }

    suspend fun deleteBankAccountWithTransactions(bank:BankAccountsClass){
        bankAccountsDao.deleteBankAccountWithTransactions(bank)
    }

    // creating a method to update data in database.
    suspend fun update(bankAccountsClass: BankAccountsClass) {
        bankAccountsDao.update(bankAccountsClass)
    }

    // creating a method to delete the data in our database.
    suspend fun delete(bankAccountsClass: BankAccountsClass) {
        bankAccountsDao.delete(bankAccountsClass)
    }

    // below is the method to delete all the category.
    suspend fun deleteAllBankAccounts() {
        bankAccountsDao.deleteAllBankAccounts()
    }
}