package com.example.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.Model.CategoryClass
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryClass: CategoryClass)

    @Delete
    suspend fun delete(categoryClass: CategoryClass)

    @Update
    suspend fun update(categoryClass: CategoryClass)

    @Query("SELECT * FROM category_table WHERE categoryType = :expense ORDER BY categoryName")
    fun getOnlyExpenseCategories(expense: String): Flow<List<CategoryClass>>

    @Query("SELECT * FROM category_table WHERE categoryType = :income ORDER BY categoryName")
    fun getOnlyIncomeCategories(income: String): Flow<List<CategoryClass>>

    @Query("DELETE FROM category_table")
    suspend fun deleteAllCategory()

    @get:Query("SELECT * FROM category_table ORDER BY categoryName DESC")
    val allCategory: Flow<List<CategoryClass>>
}
