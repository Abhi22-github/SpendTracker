package com.roaa.expensetracker.Database

import android.content.Context
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.TotalAmountClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepository(@ApplicationContext applicationContext: Context) {

    private val transactionDao: TransactionDao
    val allTransactions: Flow<List<TransactionWithDetails>>

    init {
        val database = AppDatabase.getInstance(applicationContext)
        transactionDao = database.transactionDao()
        allTransactions = transactionDao.allTransactions
    }

    suspend fun insert(transactionClass: TransactionClass) {
        transactionDao.insert(transactionClass)
    }

    suspend fun insertAndPropagateChanges(transactionClass: TransactionClass) {
        transactionDao.addTransactionAndPropagateChanges(transactionClass)
    }

    suspend fun updateAndPropagateChanges(transactionClass: TransactionClass) {
        transactionDao.updateTransactionAndPropagateChanges(transactionClass)
    }
    suspend fun updateForBudgetSwitchAndPropagateChanges(transactionClass: TransactionClass) {
        transactionDao.updateTransactionOnlyForBudgetSwitchAndPropagateChanges(transactionClass)
    }

    suspend fun deleteAndPropagateChanges(transactionClass: TransactionClass) {
        transactionDao.deleteTransactionAndPropagateChanges(transactionClass)
    }

    suspend fun update(transactionClass: TransactionClass) {
        transactionDao.update(transactionClass)
    }

    suspend fun delete(transactionClass: TransactionClass) {
        transactionDao.delete(transactionClass)
    }

    fun getTotalTransactionForMonth(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>> {
        return transactionDao.getAllTransactionsForMonth(startDate, endDate)
    }

    fun getTotalAmountByDateRangeAndCategoryType(
        startDate: Long,
        endDate: Long,
        categoryType: String
    ): Flow<TotalAmountClass> {
        return transactionDao.getTotalAmountByDateRangeAndCategoryType(
            startDate,
            endDate,
            categoryType
        )
    }
    fun getTotalAmountByDateRangeCategoryTypeAndBudgetStatus(
        startDate: Long,
        endDate: Long,
        categoryType: String,
        budgetStatus:Boolean
    ): Flow<TotalAmountClass> {
        return transactionDao.getTotalAmountByDateRangeCategoryTypeAndBudgetStatus(
            startDate,
            endDate,
            categoryType,
            budgetStatus
        )
    }

    fun getTotalAmountForDate(date: Long, type: String): Flow<Float> {
        return transactionDao.getTotalAmountForDate(date, type).map { it ?: 0f }
    }

    fun getTotalAmountForDateExcludingLast(date: Long, type: String): Flow<Float> {
        return transactionDao.getTotalAmountForDate(date, type).map { it ?: 0f }
    }

    fun getTransactionListForBankAccountId(bankAccountId: Long): Flow<List<TransactionWithDetails>> {
        return transactionDao.getAllTransactionForBankAccountId(bankAccountId)
    }

    fun getListOfTotalAmountPerDayForRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionDao.getListOfTotalAmountPerDayForRange(startDate, endDate)
    }
    fun getListOfTotalAmountPerDayForRangeForBankAccountId(
        startDate: Long,
        endDate: Long,
        bankAccountId: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionDao.getListOfTotalAmountPerDayForRangeForBankAccountId(startDate, endDate, bankAccountId)
    }




    fun getAllTransactionsForDate(date: Long): Flow<List<TransactionWithDetails>> {
        return transactionDao.getAllTransactionsForDate(date)
    }

    // below is the method to delete all the courses.
    suspend fun deleteAllTransaction() {
        transactionDao.deleteAllTransaction()
    }
}
