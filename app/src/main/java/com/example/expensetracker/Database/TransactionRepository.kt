package com.example.expensetracker.Database

import android.content.Context
import com.example.expensetracker.Model.TotalAmountClass
import com.example.expensetracker.Model.TotalExpenseIncomeClass
import com.example.expensetracker.Model.TransactionClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

class TransactionRepository(@ApplicationContext applicationContext: Context) {

    private val transactionDao: TransactionDao
    val allTransactions: Flow<List<TransactionClass>>

    init {
        val database = TransactionDatabase.getInstance(applicationContext)
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

//    // we are creating a async task method to insert new course.
//    private class InsertCourseAsyncTask(private val transactionDao: TransactionDao) :
//        AsyncTask<TransactionClass?, Void?, Void?>() {
//        override fun doInBackground(vararg model: TransactionClass): Void? {
//            // below line is use to insert our modal in dao.
//            transactionDao.insert(model[0])
//            return null
//        }
//    }
//
//    // we are creating a async task method to update our course.
//    private class UpdateCourseAsyncTask(private val transactionDao: TransactionDao) :
//        AsyncTask<TransactionClass?, Void?, Void?>() {
//        override fun doInBackground(vararg models: TransactionClass): Void? {
//            // below line is use to update
//            // our modal in dao.
//            transactionDao.update(models[0])
//            return null
//        }
//    }
//
//    // we are creating a async task method to delete course.
//    private class DeleteCourseAsyncTask(private val transactionDao: TransactionDao) :
//        AsyncTask<TransactionClass?, Void?, Void?>() {
//        override fun doInBackground(vararg models: TransactionClass): Void? {
//            // below line is use to delete
//            // our course modal in dao.
//            transactionDao.delete(models[0])
//            return null
//        }
//    }
//
//    // we are creating a async task method to delete all courses.
//    private class DeleteAllCoursesAsyncTask(private val transactionDao: TransactionDao) :
//        AsyncTask<Void?, Void?, Void?>() {
//        override fun doInBackground(vararg voids: Void): Void? {
//            // on below line calling method
//            // to delete all courses.
//            transactionDao.deleteAllTransaction()
//            return null
//        }
//    }
}
