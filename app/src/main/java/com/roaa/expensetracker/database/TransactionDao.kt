package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.composable.utils.DistributionMethod
import com.roaa.expensetracker.database.relations.BudgetWithDayDetails
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.model.uiDataModels.InfoStatClass
import com.roaa.expensetracker.model.uiDataModels.TotalAmountClass
import com.roaa.expensetracker.model.uiDataModels.TotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.toLong
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

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

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    fun getSingleTransactionWithoutFlow(id: Long): TransactionWithDetails

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

    @Query("SELECT COUNT(*) AS transactionCount,SUM(CASE WHEN type == \"Expense\" then amount else 0 END) AS totalExpense,SUM(CASE WHEN type == \"Income\" then amount else 0 END) AS totalIncome from transaction_table where date >= :startDate and date <= :endDate and categoryID == :categoryId ")
    fun getSpecificCategoryStatistics(
        categoryId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<InfoStatClass>

    @Query("SELECT COUNT(*) AS transactionCount,SUM(CASE WHEN type == \"Expense\" then amount else 0 END) AS totalExpense,SUM(CASE WHEN type == \"Income\" then amount else 0 END) AS totalIncome from transaction_table where date >= :startDate and date <= :endDate and bankAccountId == :bankId ")
    fun getSpecificBankStatistics(
        bankId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<InfoStatClass>

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

    @Query("SELECT SUM(amount) FROM transaction_table where dateWithTime >= :startDate AND dateWithTime<= :endDate and type == :type and bankAccountId == :bankAccountID")
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
    val getCurrentBudget: BudgetWithDayDetails?

    @Query("SELECT * FROM budget_day_table WHERE date == :date AND budgetId == :budgetId")
    fun getSingleBudgetDay(date: Long, budgetId: Long): BudgetDayModelClass?

    @Query("UPDATE budget_day_table SET budgetAmount = :newBudgetAmount WHERE date = :date AND budgetDayId = :budgetDayId AND  budgetId = :budgetId ")
    suspend fun updateBudgetAmount(
        date: Long,
        budgetDayId: Long,
        budgetId: Long,
        newBudgetAmount: BigDecimal
    )

    @Update
    suspend fun updateSingleDay(budgetDayModelClass: BudgetDayModelClass)

    //Transactions
    @Transaction
    suspend fun addTransactionAndPropagateChanges(transactionClass: TransactionClass): Boolean {
        insert(transactionClass)
        var isBudgetPercentageReached = false
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, INCOME)

        val bankAccount = getSingleBankAccountWithoutFlow(transactionClass.bankAccountId)

        val bankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO
        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome)
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        if (transactionClass.dateWithTime >= bankAccount.balanceLastUpdatedTimeStamp) {
            update(updatedBank)
        }

        var currentBudget = getCurrentBudget
        if (currentBudget != null) {
            if (transactionClass.date >= currentBudget.budgetSummary.budgetStartDate && transactionClass.date <= currentBudget.budgetSummary.budgetEndDate) {
                //adding expenses to respective days
                isBudgetPercentageReached =
                    addExpenseToBudgetDays(currentBudget, transactionClass, expense, income)

                currentBudget = getCurrentBudget
                when (DistributionMethod.fromNumberToObject(
                    currentBudget?.budgetSummary?.restDistributionType ?: 0
                )) {
                    DistributionMethod.DEFAULT -> {}
                    DistributionMethod.DISTRIBUTION -> {
                        evenDistributionLogicForBudget(currentBudget)
                    }

                    DistributionMethod.SPILLOVER -> {
                        spillOverDistributionLogicForBudget(currentBudget)
                    }
                }

            }
        }

        return isBudgetPercentageReached
    }

    //to add expense to respective budget days and also to check if budget is over to send notification to user
    suspend fun addExpenseToBudgetDays(
        currentBudget: BudgetWithDayDetails?,
        transactionClass: TransactionClass,
        expense: BigDecimal?,
        income: BigDecimal?
    ): Boolean {
        var isBudgetPercentageReached = false
        currentBudget?.let {
            val singleDay = getSingleBudgetDay(transactionClass.date, it.budgetSummary.budgetId)
            singleDay?.let {
                it.totalExpense = expense ?: BigDecimal.ZERO
                it.totalIncome = income ?: BigDecimal.ZERO
            }
            singleDay?.let { updateSingleDay(it) }

            //This code is only for Notification usage database read only
            val currentExpenseLocal =
                it.budgetAllDays.fold(BigDecimal.ZERO) { acc, i ->
                    acc + i.totalExpense
                }
            val effectivePercentageAmount = it.budgetSummary.totalBudgetAmount.divide(
                BigDecimal(100), 2,
                RoundingMode.HALF_UP
            ).multiply(it.budgetSummary.notificationForBudgetUsage.toDouble().toBigDecimal())
            if (effectivePercentageAmount < currentExpenseLocal) {
                //you have only left 20% of your budget
                isBudgetPercentageReached = true
            }
        }
        return isBudgetPercentageReached
    }

    //default distribution logic for budget
    suspend fun defaultDistributionLogicForBudget(currentBudget: BudgetWithDayDetails?) {
        currentBudget?.let {
            val budgetDays = it.budgetAllDays.toMutableList().sortedBy { it.date }.toMutableList()
            budgetDays.forEach { innerIt ->
                updateBudgetAmount(
                    date = innerIt.date,
                    budgetDayId = innerIt.budgetDayId,
                    budgetId = innerIt.budgetId,
                    newBudgetAmount = it.budgetSummary.budgetAmountPerDay
                )
            }
        }
    }

    //even distribution logic for budget to distribute remaining amount equally
    suspend fun evenDistributionLogicForBudget(currentBudget: BudgetWithDayDetails?) {
        currentBudget?.let {
            val budgetDays = it.budgetAllDays.toMutableList().sortedBy { it.date }.toMutableList()
            var totalExpense = BigDecimal.ZERO
            for (i in 0 until budgetDays.size) {
                if (budgetDays[i].date < LocalDate.now().toLong()) {
                    val dayTotal = getSingleBudgetDay(
                        budgetDays[i].date,
                        budgetDays[i].budgetId
                    )?.totalExpense ?: BigDecimal.ZERO
                    totalExpense += dayTotal
                    val newValue =
                        if (budgetDays[i].budgetAmount < it.budgetSummary.budgetAmountPerDay) it.budgetSummary.budgetAmountPerDay else budgetDays[i].budgetAmount
                    if (dayTotal < newValue) {
                        val remAmount = it.budgetSummary.totalBudgetAmount.minus(totalExpense)
                        //val remAmount = newValue.minus(dayTotal)
                        val remainingDays = (budgetDays.size - (i + 1)).toBigDecimal()
                        if (remainingDays > BigDecimal.ZERO) {
                            val amountForEachDay =
                                remAmount.divide(remainingDays, RoundingMode.HALF_UP)

                            for (j in (i + 1) until budgetDays.size) {
                                budgetDays[j] = budgetDays[j].copy(
                                    budgetAmount = amountForEachDay
                                )
                                // updateSingleDay(budgetDays[j].copy(budgetAmount = amountForEachDay))
                            }
                        }
                    }
                }
            }

            budgetDays.forEach {
                updateBudgetAmount(
                    date = it.date,
                    budgetDayId = it.budgetDayId,
                    budgetId = it.budgetId,
                    newBudgetAmount = it.budgetAmount
                )
            }
        }
    }

    //Spillover Budget Distribution Logic
    suspend fun spillOverDistributionLogicForBudget(currentBudget: BudgetWithDayDetails?) {
        currentBudget?.let {
            val budgetDays = it.budgetAllDays.toMutableList().sortedBy { it.date }.toMutableList()
//            var totalExpense = BigDecimal.ZERO
            for (i in 0 until budgetDays.size) {
                // if (budgetDays[i].date < LocalDate.now().toLong()) {
                val dayTotal = getSingleBudgetDay(
                    budgetDays[i].date,
                    budgetDays[i].budgetId
                )?.totalExpense ?: BigDecimal.ZERO
                // totalExpense += dayTotal
                val newValue =
                    if (budgetDays[i].budgetAmount < it.budgetSummary.budgetAmountPerDay) it.budgetSummary.budgetAmountPerDay else budgetDays[i].budgetAmount
                if (dayTotal < newValue) {
                    val remAmount = it.budgetSummary.budgetAmountPerDay.minus(dayTotal)
                    val remainingDays = (budgetDays.size - (i + 1)).toBigDecimal()
                    if (remainingDays > BigDecimal.ZERO) {
//                            val amountForEachDay =
//                                remAmount.divide(remainingDays, RoundingMode.HALF_UP)

                        //for (j in (i + 1) until budgetDays.size) {
                        budgetDays[i + 1] = budgetDays[i + 1].copy(
                            budgetAmount = budgetDays[i].budgetAmount + remAmount
                        )
                        // }
                    }
                } else {
                    for (j in (i + 1) until budgetDays.size) {
                        budgetDays[j] = budgetDays[j].copy(
                            budgetAmount = it.budgetSummary.budgetAmountPerDay
                        )
                    }
                }
            }

            budgetDays.forEach {
                updateBudgetAmount(
                    date = it.date,
                    budgetDayId = it.budgetDayId,
                    budgetId = it.budgetId,
                    newBudgetAmount = it.budgetAmount
                )
            }
        }
    }

    @Transaction
    suspend fun updateTransactionAndPropagateChanges(transactionClass: TransactionClass) {
        val oldTransactionData = getSingleTransactionWithoutFlow(transactionClass.id)
        update(transactionClass)
        val oldDateExpense =
            getTotalAmountForDateWithoutFlow(oldTransactionData.transaction.date, EXPENSE)
        val oldDateIncome =
            getTotalAmountForDateWithoutFlow(oldTransactionData.transaction.date, INCOME)
        val expense = getTotalAmountForDateWithoutFlow(transactionClass.date, EXPENSE)
        val income = getTotalAmountForDateWithoutFlow(transactionClass.date, INCOME)

        val oldBankAccount =
            getSingleBankAccountWithoutFlow(oldTransactionData.BankAccount.bankAccountId)
        val bankAccount = getSingleBankAccountWithoutFlow(transactionClass.bankAccountId)

        val oldBankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            EXPENSE,
            oldBankAccount.bankAccountId
        ) ?: BigDecimal.ZERO

        val oldBankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            INCOME,
            oldBankAccount.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankExpense = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val oldBankRemBalance = oldBankAccount.initialAmount - (oldBankExpense - oldBankIncome)
        val updateOldBank = oldBankAccount.copy(
            currentAmount = oldBankRemBalance
        )

        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome)
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        if (transactionClass.dateWithTime >= oldBankAccount.balanceLastUpdatedTimeStamp) {
            update(updateOldBank)
        }
        if (transactionClass.dateWithTime >= bankAccount.balanceLastUpdatedTimeStamp) {
            update(updatedBank)
        }

        var currentBudget = getCurrentBudget

        if (currentBudget != null) {
            if (transactionClass.date >= currentBudget.budgetSummary.budgetStartDate && transactionClass.date <= currentBudget.budgetSummary.budgetEndDate) {
                //adding expenses to respective days
                addExpenseToBudgetDays(
                    currentBudget,
                    oldTransactionData.transaction,
                    oldDateExpense,
                    oldDateIncome
                )
                addExpenseToBudgetDays(currentBudget, transactionClass, expense, income)
                currentBudget = getCurrentBudget
                when (DistributionMethod.fromNumberToObject(
                    currentBudget?.budgetSummary?.restDistributionType ?: 0
                )) {
                    DistributionMethod.DEFAULT -> {}
                    DistributionMethod.DISTRIBUTION -> {
                        evenDistributionLogicForBudget(currentBudget)
                    }

                    DistributionMethod.SPILLOVER -> {
                        spillOverDistributionLogicForBudget(currentBudget)
                    }
                }

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

        var currentBudget = getCurrentBudget
        if (currentBudget != null) {
            if (transactionClass.date >= currentBudget.budgetSummary.budgetStartDate && transactionClass.date <= currentBudget.budgetSummary.budgetEndDate) {
                //adding expenses to respective days

                addExpenseToBudgetDays(currentBudget, transactionClass, expense, income)
                currentBudget = getCurrentBudget
                when (DistributionMethod.fromNumberToObject(
                    currentBudget?.budgetSummary?.restDistributionType ?: 0
                )) {
                    DistributionMethod.DEFAULT -> {}
                    DistributionMethod.DISTRIBUTION -> {
                        evenDistributionLogicForBudget(currentBudget)
                    }

                    DistributionMethod.SPILLOVER -> {
                        spillOverDistributionLogicForBudget(currentBudget)
                    }
                }

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
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            EXPENSE,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO

        val bankIncome = getTotalAmountForBankWithDateWithoutFlow(
            bankAccount.balanceLastUpdatedTimeStamp,
            transactionClass.dateWithTime,
            INCOME,
            transactionClass.bankAccountId
        ) ?: BigDecimal.ZERO
        val remBalance = bankAccount.initialAmount - (bankExpense - bankIncome)
        val updatedBank = bankAccount.copy(
            currentAmount = remBalance
        )
        if (transactionClass.dateWithTime >= bankAccount.balanceLastUpdatedTimeStamp) {
            update(updatedBank)
        }

        var currentBudget = getCurrentBudget
        if (currentBudget != null) {
            if (transactionClass.date >= currentBudget.budgetSummary.budgetStartDate && transactionClass.date <= currentBudget.budgetSummary.budgetEndDate) {
                //adding expenses to respective days

                addExpenseToBudgetDays(currentBudget, transactionClass, expense, income)
                currentBudget = getCurrentBudget
                when (DistributionMethod.fromNumberToObject(
                    currentBudget?.budgetSummary?.restDistributionType ?: 0
                )) {
                    DistributionMethod.DEFAULT -> {}
                    DistributionMethod.DISTRIBUTION -> {
                        evenDistributionLogicForBudget(currentBudget)
                    }

                    DistributionMethod.SPILLOVER -> {
                        spillOverDistributionLogicForBudget(currentBudget)
                    }
                }

            }
        }
    }
}
