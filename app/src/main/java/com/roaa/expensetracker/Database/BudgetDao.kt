package com.roaa.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    //Normal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetModelClass: BudgetModelClass): Long

    @Delete
    suspend fun delete(budgetModelClass: BudgetModelClass)

    @Update
    suspend fun update(budgetModelClass: BudgetModelClass)

    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    suspend fun getTotalAmountForDate(date: Long, type: String): Float?

    @Query("DELETE FROM budget_table")
    suspend fun deleteAllBudget()

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudget: Flow<BudgetModelClass>

    @get:Query("SELECT * FROM budget_table ORDER BY budgetId DESC")
    val allBudget: Flow<List<BudgetModelClass>>

    //Relations

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudgetWithDays: Flow<BudgetWithDayDetails?>

    @Query("SELECT * FROM budget_table WHERE budgetId == :budgetId ")
    fun getBudgetWithDays(budgetId: Long): Flow<BudgetWithDayDetails>


    // Transaction Supporting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(budgetDayModelClass: BudgetDayModelClass): Long

    //Transactions
    @Transaction
    suspend fun insertWithDayDetails(
        budgetModelClass: BudgetModelClass, validDatesListFromLong: List<Long>
    ) {
        val transactionId = insert(budgetModelClass)
        for (date in validDatesListFromLong) {
            val budgetDayClass = BudgetDayModelClass(
                budgetDayId = 0L,
                date = date,
                budgetAmount = budgetModelClass.budgetAmountPerDay,
                totalExpense = getTotalAmountForDate(date, EXPENSE) ?: 0f,
                totalIncome = getTotalAmountForDate(date, INCOME) ?: 0f,
                totalExpenseTransactionCount = 0L,
                totalIncomeTransactionCount = 0L,
                budgetId = transactionId
            )
            insertDays(budgetDayClass)
        }

    }
}
