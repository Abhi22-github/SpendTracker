package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.BudgetModelClass
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.model.uiDataModels.TotalAmountClass
import com.roaa.expensetracker.model.uiDataModels.TotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface TransactionDao {
    // Normal

    @Insert
    suspend fun insert(transactionClass: TransactionClass)

    @Delete
    suspend fun delete(transactionClass: TransactionClass)

    @Update
    suspend fun update(transactionClass: TransactionClass)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransaction()

    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    fun getTotalAmountForDate(date: Long, type: String): Flow<BigDecimal?>

    @Query("SELECT COALESCE(SUM(amount),0) FROM transaction_table WHERE date = :date AND type = :type AND id NOT IN (SELECT id FROM transaction_table WHERE date = :date AND type = :type ORDER BY dateWithTime DESC LIMIT 1)")
    fun getTotalAmountForDateExcludingLast(date: Long, type: String): Flow<BigDecimal?>

    @Query("SELECT date,SUM(amount) AS totalAmount FROM transaction_table where date >= :startDate and date <= :endDate and type == :type")
    fun getTotalAmountByDateRangeAndCategoryType(
        startDate: Long,
        endDate: Long,
        type: String
    ): Flow<TotalAmountClass>

    @Query("SELECT date,SUM(amount) AS totalAmount FROM transaction_table where date >= :startDate and date <= :endDate and type == :type and includeInRespectiveBudget ==:budgetStatus")
    fun getTotalAmountByDateRangeCategoryTypeAndBudgetStatus(
        startDate: Long,
        endDate: Long,
        type: String,
        budgetStatus: Boolean
    ): Flow<TotalAmountClass>

    @Query("SELECT date,SUM(CASE WHEN type == \"Expense\" then amount else 0 END) AS totalExpense,SUM(CASE WHEN type == \"Income\" then amount else 0 END) AS totalIncome from transaction_table where date >= :startDate and date <= :endDate group by date")
    fun getListOfTotalAmountPerDayForRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TotalExpenseIncomeClass>>

    @Query("SELECT date,SUM(CASE WHEN type == \"Expense\" then amount else 0 END) AS totalExpense,SUM(CASE WHEN type == \"Income\" then amount else 0 END) AS totalIncome from transaction_table where date >= :startDate and date <= :endDate and bankAccountId == :bankAccountId group by date")
    fun getListOfTotalAmountPerDayForRangeForBankAccountId(
        startDate: Long,
        endDate: Long,
        bankAccountId: Long
    ): Flow<List<TotalExpenseIncomeClass>>

    //Relations
    @get:Query("SELECT * FROM transaction_table ORDER BY dateWithTime DESC")
    val allTransactions: Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transaction_table where date == :date ORDER BY dateWithTime DESC")
    fun getAllTransactionsForDate(date: Long): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transaction_table where bankAccountId == :bankAccountId")
    fun getAllTransactionForBankAccountId(bankAccountId: Long): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transaction_table where date >= :startDate AND date<= :endDate ORDER BY dateWithTime DESC")
    fun getAllTransactionsForPeriod(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>>

    //Transaction Supporting
    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type")
    fun getTotalAmountForDateWithoutFlow(date: Long, type: String): BigDecimal?

    //update bank
    @Update
    suspend fun update(bankAccountsClass: BankAccountsClass)

    @Query("SELECT SUM(amount) FROM transaction_table where date >= :startDate AND date<= :endDate and type == :type and bankAccountId == :bankAccountID")
    fun getTotalAmountForBankWithDateWithoutFlow(
        startDate: Long,
        endDate: Long,
        type: String,
        bankAccountID: Long
    ): BigDecimal?

    @Query("SELECT * FROM bank_accounts WHERE bankAccountId = :id")
    fun getSingleBankAccountWithoutFlow(id: Long): BankAccountsClass

    @Query("SELECT SUM(amount) FROM transaction_table where date == :date and type == :type and includeInRespectiveBudget ==:includeInBudget")
    fun getTotalAmountForDateForBudgetOptTransactionWithoutFlow(
        date: Long,
        type: String,
        includeInBudget: Boolean
    ): BigDecimal?

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudget: BudgetModelClass?

    @Query("SELECT * FROM budget_day_table WHERE date == :date AND budgetId == :budgetId")
    fun getSingleBudgetDay(date: Long, budgetId: Long): BudgetDayModelClass?

    @Update
    suspend fun updateSingleDay(budgetDayModelClass: BudgetDayModelClass)

    //Transactions
    @Transaction
    suspend fun addTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        insert(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, INCOME)

        val bankAccount = getSingleBankAccountWithoutFlow(transactionClass.bankAccountId)

        val bankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO
        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome )
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        update(updatedBank)

        val currentBudget = getCurrentBudget

        currentBudget?.let {
            if (transactionClass.date >= it.budgetStartDate && transactionClass.date <= it.budgetEndDate) {
                val singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: BigDecimal.ZERO
                    it.totalIncome = income ?: BigDecimal.ZERO
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }

    }

    @Transaction
    suspend fun updateTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        update(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, INCOME)

        val bankAccount = getSingleBankAccountWithoutFlow(transactionClass.bankAccountId)

        val bankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO
        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome )
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        update(updatedBank)

        val currentBudget = getCurrentBudget

        currentBudget?.let {
            if (transactionClass.date >= it.budgetStartDate && transactionClass.date <= it.budgetEndDate) {
                val singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: BigDecimal.ZERO
                    it.totalIncome = income ?: BigDecimal.ZERO
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }

    @Transaction
    suspend fun updateTransactionOnlyForBudgetSwitchAndPropagateChanges(transactionClass: TransactionClass) {
        update(transactionClass)
        val expense = getTotalAmountForDateForBudgetOptTransactionWithoutFlow(
            transactionClass.date,
            EXPENSE,
            true
        )
        val income =
            getTotalAmountForDateForBudgetOptTransactionWithoutFlow(
                transactionClass.date,
                INCOME,
                true
            )

        val currentBudget = getCurrentBudget

        currentBudget?.let {
            if (transactionClass.date >= it.budgetStartDate && transactionClass.date <= it.budgetEndDate) {
                val singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: BigDecimal.ZERO
                    it.totalIncome = income ?: BigDecimal.ZERO
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }

    @Transaction
    suspend fun deleteTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        delete(transactionClass)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)

        val bankAccount = getSingleBankAccountWithoutFlow(transactionClass.bankAccountId)

        val bankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.accountAddedDate,
            transactionClass.date,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO
        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome )
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        update(updatedBank)

        val currentBudget = getCurrentBudget

        currentBudget?.let {
            if (transactionClass.date >= it.budgetStartDate && transactionClass.date <= it.budgetEndDate) {
                val singleDay = getSingleBudgetDay(transactionClass.date, it.budgetId)
                singleDay?.let {
                    it.totalExpense = expense ?: BigDecimal.ZERO
                    it.totalIncome = income ?: BigDecimal.ZERO
                }
                singleDay?.let { updateSingleDay(it) }
            }
        }
    }
}
