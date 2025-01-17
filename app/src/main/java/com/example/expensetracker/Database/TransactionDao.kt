package com.example.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.Model.TotalAmountClass
import com.example.expensetracker.Model.TotalExpenseIncomeClass
import com.example.expensetracker.Model.TransactionClass
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transactionClass: TransactionClass)

    @Delete
    suspend fun delete(transactionClass: TransactionClass)

    @Update
    suspend fun update(transactionClass: TransactionClass)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransaction()

    @get:Query("SELECT * FROM transaction_table ORDER BY dateWithTime DESC")
    val allTransactions: Flow<List<TransactionClass>>

    @Query("SELECT * FROM transaction_table where date == :date ORDER BY dateWithTime DESC")
    fun getAllTransactionsForDate(date: Long): Flow<List<TransactionClass>>

    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    fun getTotalAmountForDate(date: Long, type: String): Flow<Long?>

    @Query("SELECT date,SUM(amount) AS totalAmount FROM transaction_table where date >= :startDate and date <= :endDate and type == :type")
    fun getTotalAmountByDateRangeAndCategoryType(
        startDate: Long,
        endDate: Long,
        type: String
    ): Flow<TotalAmountClass>

    @Query("SELECT date,SUM(CASE WHEN type == \"Expense\" then amount else 0 END) AS totalExpense,SUM(CASE WHEN type == \"Income\" then amount else 0 END) AS totalIncome from transaction_table where date >= :startDate and date <= :endDate group by date")
    fun getListOfTotalAmountPerDayForRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TotalExpenseIncomeClass>>

}
