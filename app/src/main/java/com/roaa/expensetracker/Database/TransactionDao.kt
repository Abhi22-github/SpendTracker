package com.roaa.expensetracker.Database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Model.TotalAmountClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    // Normal

    @Insert
    suspend fun insert(transactionClass: TransactionClass)

    @Delete
    suspend fun delete(transactionClass: TransactionClass)

    @Update
    suspend fun update(transactionClass: TransactionClass)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransaction()

    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    fun getTotalAmountForDate(date: Long, type: String): Flow<Float?>

    @Query("SELECT COALESCE(SUM(amount),0) FROM transaction_table WHERE date = :date AND type = :type AND id NOT IN (SELECT id FROM transaction_table WHERE date = :date AND type = :type ORDER BY dateWithTime DESC LIMIT 1)")
    fun getTotalAmountForDateExcludingLast(date: Long, type: String): Flow<Float?>

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

    //Relations
    @get:Query("SELECT * FROM transaction_table ORDER BY dateWithTime DESC")
    val allTransactions: Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transaction_table where date == :date ORDER BY dateWithTime DESC")
    fun getAllTransactionsForDate(date: Long): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transaction_table where date >= :startDate AND date<= :endDate ORDER BY dateWithTime DESC")
    fun getAllTransactionsForMonth(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>>

    //Transaction Supporting
    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    fun getTotalAmountForDateWithoutFlow(date: Long, type: String): Float?

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudget: BudgetModelClass?

    @Query("SELECT * FROM budget_day_table WHERE date == :date AND budgetId == :budgetId")
    fun getSingleBudgetDay(date: Long, budgetId: Long): BudgetDayModelClass?

    @Update
    suspend fun updateSingleDay(budgetDayModelClass: BudgetDayModelClass)

    //Transactions
    @Transaction
    suspend fun addTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        insert(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)

        val currentBudget = getCurrentBudget

        Log.d("TransactionDao", "$expense $income ${currentBudget?.budgetId}")
        currentBudget?.let {
            if (transactionClass.date >= it.budgetMonthStartDate && transactionClass.date <= it.budgetMonthEndDate) {
                var singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: 0f
                    it.totalIncome = income ?: 0f
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }

    @Transaction
    suspend fun updateTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        update(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)

        val currentBudget = getCurrentBudget

        Log.d("TransactionDao", "$expense $income ${currentBudget?.budgetId}")
        currentBudget?.let {
            if (transactionClass.date >= it.budgetMonthStartDate && transactionClass.date <= it.budgetMonthEndDate) {
                var singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: 0f
                    it.totalIncome = income ?: 0f
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }

    @Transaction
    suspend fun deleteTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        delete(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)

        val currentBudget = getCurrentBudget

        Log.d("TransactionDao", "$expense $income ${currentBudget?.budgetId}")
        currentBudget?.let {
            if (transactionClass.date >= it.budgetMonthStartDate && transactionClass.date <= it.budgetMonthEndDate) {
                var singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: 0f
                    it.totalIncome = income ?: 0f
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }
}
