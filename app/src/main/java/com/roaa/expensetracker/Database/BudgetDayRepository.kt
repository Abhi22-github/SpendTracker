package com.roaa.expensetracker.Database

import com.roaa.expensetracker.Model.BudgetDayModelClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetDayRepository @Inject constructor(private val budgetDayDao: BudgetDayDao) {


    // below method is to read all category from database.
    val allDaysForAllBudgets: Flow<List<BudgetDayModelClass>> = budgetDayDao.allDays

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