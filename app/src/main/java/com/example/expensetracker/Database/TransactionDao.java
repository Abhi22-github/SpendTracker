package com.example.expensetracker.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetracker.Model.TotalAmountClass;
import com.example.expensetracker.Model.TransactionClass;

import java.util.List;

@androidx.room.Dao
public interface TransactionDao {

    @Insert
    void insert(TransactionClass transactionClass);


    @Delete
    void delete(TransactionClass transactionClass);

    @Update
    void update(TransactionClass transactionClass);

    @Query("DELETE FROM transaction_table")
    void deleteAllTransaction();

    @Query("SELECT * FROM transaction_table ORDER BY dateWithTime DESC")
    LiveData<List<TransactionClass>> getAllTransactions();

    @Query("SELECT date,SUM(amount) AS totalAmount FROM transaction_table where date == :date and type == :category")
    TotalAmountClass getTotalAmountByDateAndCategoryType(Long date,String category);

    @Query("SELECT date,SUM(amount) AS totalAmount FROM transaction_table where date >= :startDate and date <= :endDate and type == :category")
    LiveData<TotalAmountClass> getTotalAmountByDateRangeAndCategoryType(Long startDate,Long endDate,String category);
}
