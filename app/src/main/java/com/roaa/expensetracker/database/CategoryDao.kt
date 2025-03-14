package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.model.TransactionClass
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryClass: CategoryClass)

    @Delete
    suspend fun delete(categoryClass: CategoryClass)

    @Update
    suspend fun update(categoryClass: CategoryClass)

    @Query("SELECT * FROM category_table WHERE categoryType = :expense AND isActive = 1 ORDER BY categoryName")
    fun getOnlyExpenseCategories(expense: String): Flow<List<CategoryClass>>

    @Query("SELECT * FROM category_table WHERE categoryType = :income AND isActive = 1 ORDER BY categoryName")
    fun getOnlyIncomeCategories(income: String): Flow<List<CategoryClass>>

    @Query("DELETE FROM category_table")
    suspend fun deleteAllCategory()

    @get:Query("SELECT * FROM category_table ORDER BY categoryName DESC")
    val allCategory: Flow<List<CategoryClass>>

    //Transaction Supporting
    @Query("SELECT * FROM transaction_table where categoryId == :categoryId")
    fun getTransactionListForCategoryId(categoryId: Long): List<TransactionClass>

    @Update
    suspend fun update(transactionClass: TransactionClass)

    @Delete
    suspend fun delete(transactionClass: TransactionClass)

    //Transactions
    @Transaction
    suspend fun migrateTransactionToAnotherCategoryAndDeleteIt(
        firstCategory: CategoryClass,
        secondCategory: CategoryClass
    ) {
        val transactionsList = getTransactionListForCategoryId(firstCategory.categoryId)
        transactionsList.forEach { transaction ->
            val updatedTransaction =
                transaction.copy(categoryId = secondCategory.categoryId)
            update(updatedTransaction)
        }
        delete(firstCategory)
    }

    @Transaction
    suspend fun deleteCategoryWithTransactions(
        categoryClass: CategoryClass,
    ) {
        val transactionsList = getTransactionListForCategoryId(categoryClass.categoryId)
        transactionsList.forEach { transaction ->
            delete(transaction)
        }
        delete(categoryClass)
    }
}
