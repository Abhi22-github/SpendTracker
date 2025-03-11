package com.roaa.expensetracker.Database


import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Database.Relations.BudgetWithDayDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val budgetDao: BudgetDao) {

    // below method is to read all category from database.
    val getCurrentBudget: Flow<BudgetModelClass> = budgetDao.getCurrentBudget
    val getCurrentBudgetWithDays: Flow<BudgetWithDayDetails?> = budgetDao.getCurrentBudgetWithDays
    val allBudget: Flow<List<BudgetModelClass>> = budgetDao.allBudget

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

    suspend fun updateWithDetails(
        budgetModelClass: BudgetModelClass,
        validDatesListFromLong: List<Long>,
        validDatesListFromPreviousBudget: List<BudgetDayModelClass>
    ) {
        budgetDao.updateWithDayDetails(budgetModelClass, validDatesListFromLong,validDatesListFromPreviousBudget)
    }

    fun getBudgetWithDays(budgetId: Long): Flow<BudgetWithDayDetails?> {
        return budgetDao.getBudgetWithDays(budgetId)
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