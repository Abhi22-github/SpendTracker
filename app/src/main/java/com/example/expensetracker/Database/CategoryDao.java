package com.example.expensetracker.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Model.TransactionClass;

import java.util.List;

@androidx.room.Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryClass categoryClass);

    @Delete
    void delete(CategoryClass categoryClass);

    @Update
    void update(CategoryClass categoryClass);

    @Query("SELECT * FROM category_table WHERE categoryType = :expense ORDER BY categoryName")
    LiveData<List<CategoryClass>> getOnlyExpenseCategories(String expense);

    @Query("SELECT * FROM category_table WHERE categoryType = :income ORDER BY categoryName")
    LiveData<List<CategoryClass>> getOnlyIncomeCategories(String income);

    @Query("DELETE FROM category_table")
    void deleteAllCategory();

    @Query("SELECT * FROM category_table ORDER BY categoryName DESC")
    LiveData<List<CategoryClass>> getAllCategory();
}
