package com.roaa.expensetracker.Database

import android.content.Context
import com.roaa.expensetracker.Database.AppDatabase.Companion.getInstance
import com.roaa.expensetracker.Model.BudgetDayModelClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

class BudgetDayRepository(@ApplicationContext applicationContext: Context) {
    private val budgetDayDao: BudgetDayDao

    // below method is to read all category from database.
    val allDaysForAllBudgets: Flow<List<BudgetDayModelClass>>

    init {
        val database = getInstance(applicationContext)
        budgetDayDao = database.budgetDayDao()
        allDaysForAllBudgets = budgetDayDao.allDays
    }

    // creating a method to insert the data to our database.
    suspend fun insert(budgetDayModelClass: BudgetDayModelClass) {
        budgetDayDao.insert(budgetDayModelClass)
    }

    // creating a method to insert the data to our database.
    fun budgetSpecificDays(startDate: Long, endDate: Long) {
        budgetDayDao.budgetSpecificDays(startDate, endDate)
    }


    // creating a method to update data in database.
    suspend fun update(budgetDayModelClass: BudgetDayModelClass) {
        budgetDayDao.update(budgetDayModelClass)
    }

    // creating a method to delete the data in our database.
    suspend fun delete(budgetDayModelClass: BudgetDayModelClass) {
        budgetDayDao.delete(budgetDayModelClass)
    }

    // below is the method to delete all the category.
    suspend fun deleteAllBudgets() {
        budgetDayDao.deleteAllBudgetDays()
    }
}