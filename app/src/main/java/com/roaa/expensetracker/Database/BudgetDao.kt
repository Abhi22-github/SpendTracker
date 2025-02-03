package com.roaa.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.roaa.expensetracker.Model.BudgetModelClass
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetModelClass: BudgetModelClass)

    @Delete
    suspend fun delete(budgetModelClass: BudgetModelClass)

    @Update
    suspend fun update(budgetModelClass: BudgetModelClass)

    @Query("DELETE FROM budget_table")
    suspend fun deleteAllBudget()

    @get:Query("SELECT * FROM budget_table WHERE budgetStatus = 1")
    val getCurrentBudget: Flow<BudgetModelClass>

    @get:Query("SELECT * FROM budget_table ORDER BY budgetId DESC")
    val allBudget: Flow<List<BudgetModelClass>>
}
