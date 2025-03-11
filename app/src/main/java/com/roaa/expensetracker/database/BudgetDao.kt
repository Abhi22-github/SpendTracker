package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.database.Relations.BudgetWithDayDetails
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

    @Query("SELECT SUM(amount) FROM transaction_table where date = :date and type = :type")
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
    fun getBudgetWithDays(budgetId: Long): Flow<BudgetWithDayDetails?>


    // Transaction Supporting

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(budgetDayModelClass: BudgetDayModelClass): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDays(budgetDayModelClass: BudgetDayModelClass)

    @Delete
    suspend fun removeDays(budgetDayModelClass: BudgetDayModelClass)

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

    @Transaction
    suspend fun updateWithDayDetails(
        budgetModelClass: BudgetModelClass,
        validDatesListFromLong: List<Long>,
        validDatesListFromPreviousBudget: List<BudgetDayModelClass>
    ) {
        update(budgetModelClass)

        // Convert previous budget dates to a set for quick lookup
        val previousDatesSet = validDatesListFromPreviousBudget.map { it.date }.toSet()
        val newDatesSet = validDatesListFromLong.toSet()

        // Find dates that need to be removed (present in previous but not in new budget)
        val datesToRemove = validDatesListFromPreviousBudget.filter { it.date !in newDatesSet }

        // Find dates that need to be added (present in new budget but missing in previous)
        val datesToAdd = validDatesListFromLong.filter { it !in previousDatesSet }

        // Find dates to update (present in both lists, but we may need to update them)
        val datesToUpdate = validDatesListFromPreviousBudget.filter { it.date in newDatesSet }


        datesToRemove.forEach {
            removeDays(it)
        }

        datesToUpdate.forEach {
            val updatedObj = it.copy(
                budgetId = budgetModelClass.budgetId,
                budgetAmount = budgetModelClass.budgetAmountPerDay
            )
            updateDays(updatedObj)
        }

        // Insert new dates (convert from Long to BudgetDayModelClass)
        insertWithDayDetails(budgetModelClass, datesToAdd)
    }
}

//val difference =
//    validDatesListFromPreviousBudget.filterNot { it.date in validDatesListFromLong }
//difference.forEach {
//    val tempObj = it.copy(budgetId = 0L)
//    updateDays(tempObj)
//}
