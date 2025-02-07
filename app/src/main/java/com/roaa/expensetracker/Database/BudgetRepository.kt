package com.roaa.expensetracker.Database

import android.content.Context
import com.roaa.expensetracker.Database.AppDatabase.Companion.getInstance
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import com.roaa.expensetracker.Model.BudgetModelClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

class BudgetRepository(@ApplicationContext applicationContext: Context) {
    private val budgetDao: BudgetDao

    // below method is to read all category from database.
    val getCurrentBudget: Flow<BudgetModelClass>
    val getCurrentBudgetWithDays: Flow<BudgetWithDayDetails?>
    val allBudget: Flow<List<BudgetModelClass>>

    init {
        val database = getInstance(applicationContext)
        budgetDao = database.budgetDao()
        getCurrentBudget = budgetDao.getCurrentBudget
        getCurrentBudgetWithDays = budgetDao.getCurrentBudgetWithDays
        allBudget = budgetDao.allBudget
    }

    // creating a method to insert the data to our database.
    suspend fun insert(budgetModelClass: BudgetModelClass): Long {
        return budgetDao.insert(budgetModelClass)
    }

    suspend fun insertWithDetails(
        budgetModelClass: BudgetModelClass,
        validDatesListFromLong: List<Long>
    ) {
        budgetDao.insertWithDayDetails(budgetModelClass, validDatesListFromLong)
    }


    // creating a method to update data in database.
    suspend fun update(budgetModelClass: BudgetModelClass) {
        budgetDao.update(budgetModelClass)
    }

    // creating a method to delete the data in our database.
    suspend fun delete(budgetModelClass: BudgetModelClass) {
        budgetDao.delete(budgetModelClass)
    }

    // below is the method to delete all the category.
    suspend fun deleteAllBudgets() {
        budgetDao.deleteAllBudget()
    }
}