package com.roaa.expensetracker.database

import com.roaa.expensetracker.model.BankAmountCorrectionsClass
import javax.inject.Inject

class BankAccountCorrectionsRepository @Inject constructor(private val bankAccountCorrectionsDao: BankAccountCorrectionsDao) {


    // creating a method to insert the data to our database. 
    suspend fun insert(bankAmountCorrectionsClass: BankAmountCorrectionsClass) {
        bankAccountCorrectionsDao.insert(bankAmountCorrectionsClass)
    }

    // creating a method to update data in database. 
    suspend fun update(bankAmountCorrectionsClass: BankAmountCorrectionsClass) {
        bankAccountCorrectionsDao.update(bankAmountCorrectionsClass)
    }

    // creating a method to delete the data in our database. 
    suspend fun delete(bankAmountCorrectionsClass: BankAmountCorrectionsClass) {
        bankAccountCorrectionsDao.delete(bankAmountCorrectionsClass)
    }


}
