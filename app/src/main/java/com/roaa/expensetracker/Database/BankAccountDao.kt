package com.roaa.expensetracker.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.roaa.expensetracker.Model.BankAccountsClass
import kotlinx.coroutines.flow.Flow

@Dao
interface BankAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bankAccountsClass: BankAccountsClass)

    @Delete
    suspend fun delete(bankAccountsClass: BankAccountsClass)

    @Update
    suspend fun update(bankAccountsClass: BankAccountsClass)

    @Query("SELECT * FROM bank_accounts WHERE id = :id")
    fun getSingleBankAccount(id: Long): Flow<BankAccountsClass>

    @Query("DELETE FROM bank_accounts")
    suspend fun deleteAllBankAccounts()

    @get:Query("SELECT * FROM bank_accounts ")
    val allBankAccountsClass: Flow<List<BankAccountsClass>>

    @get:Query("SELECT * FROM bank_accounts WHERE accountType != 'CASH'")
    val allBankAccountsClassExceptCash: Flow<List<BankAccountsClass>>
}