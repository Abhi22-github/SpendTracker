package com.roaa.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.roaa.expensetracker.Model.BudgetDayModelClass
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetDayModelClass: BudgetDayModelClass)

    @Delete
    suspend fun delete(budgetDayModelClass: BudgetDayModelClass)

    @Update
    suspend fun update(budgetDayModelClass: BudgetDayModelClass)

    @Query("DELETE FROM budget_day_table")
    suspend fun deleteAllBudgetDays()

    @Query("SELECT * FROM budget_day_table WHERE date BETWEEN :startDate AND :endDate ORDER BY budgetId DESC")
    fun budgetSpecificDays(startDate: Long, endDate: Long): Flow<List<BudgetDayModelClass>>

    @get:Query("SELECT * FROM budget_day_table ORDER BY budgetId DESC")
    val allDays: Flow<List<BudgetDayModelClass>>

    //Transaction Supporting
    
}
