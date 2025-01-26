package com.roaa.expensetracker.Database

import android.content.Context
import com.roaa.expensetracker.Database.AppDatabase.Companion.getInstance
import com.roaa.expensetracker.Model.BankAccountsClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

class BankAccountRepository(@ApplicationContext applicationContext: Context) {
    private val bankAccountsDao: BankAccountDao

    // below method is to read all category from database.
    val allBankAccountsExceptCash: Flow<List<BankAccountsClass>>

    init {
        val database = getInstance(applicationContext)
        bankAccountsDao = database.bankAccountsDao()
        allBankAccountsExceptCash = bankAccountsDao.allBankAccountsClassExceptCash
    }

    // creating a method to insert the data to our database.
    suspend fun insert(bankAccountsClass: BankAccountsClass) {
        bankAccountsDao.insert(bankAccountsClass)
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