package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.DailyBalancesClass
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.toLong
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

@Dao
interface BankAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bankAccountsClass: BankAccountsClass):Long

    @Delete
    suspend fun delete(bankAccountsClass: BankAccountsClass)

    @Update
    suspend fun update(bankAccountsClass: BankAccountsClass)

    @Query("SELECT * FROM bank_accounts WHERE bankAccountId = :id")
    fun getSingleBankAccount(id: Long): Flow<BankAccountsClass>

    @Query("DELETE FROM bank_accounts")
    suspend fun deleteAllBankAccounts()

    @get:Query("SELECT * FROM bank_accounts WHERE isActive == 1")
    val allBankAccountsClass: Flow<List<BankAccountsClass>>

    @get:Query("SELECT * FROM bank_accounts WHERE accountType != 'CASH' AND isActive == 1")
    val allBankAccountsClassExceptCash: Flow<List<BankAccountsClass>>


    //Transaction Supporting
    @Query("SELECT * FROM transaction_table where bankAccountId == :bankAccountId")
    fun getTransactionListForBankAccountId(bankAccountId: Long): List<TransactionClass>

    @Update
    suspend fun update(transactionClass: TransactionClass)

    @Delete
    suspend fun delete(transactionClass: TransactionClass)

    @Query("SELECT SUM(amount) FROM transaction_table where date = :date and type = :type")
    suspend fun getTotalAmountForDate(date: Long, type: String): BigDecimal?

    @Insert
    suspend fun insert(dailyBalancesClass: DailyBalancesClass): Long


    //Transactions
    @Transaction
    suspend fun insertWithDailyBalance(bankAccountsClass: BankAccountsClass) {
        val id = insert(bankAccountsClass)
        val dailyBalance = DailyBalancesClass(
            id = 0,
            bankAccountId = id,
            date = LocalDate.now().toLong(),
            remBankBalance = bankAccountsClass.currentAmount,
            dayTotalExpense = getTotalAmountForDate(LocalDate.now().toLong(), EXPENSE)
                ?: BigDecimal.ZERO,
            dayTotalIncome = getTotalAmountForDate(LocalDate.now().toLong(), EXPENSE)
                ?: BigDecimal.ZERO,
            isSetByUser = true
        )
        insert(dailyBalance)
    }


    @Transaction
    suspend fun migrateTransactionToAnotherBankAccountAndDeleteIt(
        firstBankAccount: BankAccountsClass,
        secondBankAccount: BankAccountsClass
    ) {
        val transactionsList = getTransactionListForBankAccountId(firstBankAccount.bankAccountId)
        transactionsList.forEach { transaction ->
            val updatedTransaction =
                transaction.copy(bankAccountId = secondBankAccount.bankAccountId)
            update(updatedTransaction)
        }
        delete(firstBankAccount)
    }

    @Transaction
    suspend fun deleteBankAccountWithTransactions(
        bankAccount: BankAccountsClass,
    ) {
        val transactionsList = getTransactionListForBankAccountId(bankAccount.bankAccountId)
        transactionsList.forEach { transaction ->
            delete(transaction)
        }
        delete(bankAccount)
    }
}