package com.roaa.expensetracker.Database

import android.content.Context
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.TotalAmountClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

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

    suspend fun update(transactionClass: TransactionClass) {
        transactionDao.update(transactionClass)
    }

    suspend fun delete(transactionClass: TransactionClass) {
        transactionDao.delete(transactionClass)
    }

    suspend fun getTotalAmountByDateRangeAndCategoryType(
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

    fun getTotalAmountForDate(date: Long, type: String): Flow<Long?> {
        return transactionDao.getTotalAmountForDate(date, type)
    }

    fun getListOfTotalAmountPerDayForRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionDao.getListOfTotalAmountPerDayForRange(startDate, endDate)
    }

    fun getAllTransactionsForDate(date: Long): Flow<List<TransactionClass>> {
        return transactionDao.getAllTransactionsForDate(date)
    }

    // below is the method to delete all the courses.
    suspend fun deleteAllTransaction() {
        transactionDao.deleteAllTransaction()
    }
}
