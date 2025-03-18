package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.roaa.expensetracker.model.BankAmountCorrectionsClass

@Dao
interface BankAccountCorrectionsDao {
    //Normal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bankAmountCorrectionsClass: BankAmountCorrectionsClass): Long

    @Delete
    suspend fun delete(bankAmountCorrectionsClass: BankAmountCorrectionsClass)

    @Update
    suspend fun update(bankAmountCorrectionsClass: BankAmountCorrectionsClass)

}
