package com.roaa.expensetracker.Database

import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Utilities.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {

    // below method is to read all category from database.
    val allCategories: Flow<List<CategoryClass>> = categoryDao.allCategory

    fun getOnlyExpenseCategories(): Flow<List<CategoryClass>> {
        return categoryDao.getOnlyExpenseCategories(Constants.EXPENSE)
    }

     fun getOnlyIncomeCategories(): Flow<List<CategoryClass>> {
        return categoryDao.getOnlyExpenseCategories(Constants.INCOME)
    }

    // creating a method to insert the data to our database. 
    suspend fun insert(categoryClass: CategoryClass) {
        categoryDao.insert(categoryClass)
    }

    // creating a method to update data in database. 
    suspend fun update(categoryClass: CategoryClass) {
        categoryDao.update(categoryClass)
    }

    // creating a method to delete the data in our database. 
    suspend fun delete(categoryClass: CategoryClass) {
        categoryDao.delete(categoryClass = categoryClass)
    }

    // below is the method to delete all the category. 
    suspend fun deleteAllTransaction() {
        categoryDao.deleteAllCategory()
    }


}
