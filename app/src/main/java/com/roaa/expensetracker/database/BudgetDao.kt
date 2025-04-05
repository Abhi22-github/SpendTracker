package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.roaa.expensetracker.composable.utils.DistributionMethod
import com.roaa.expensetracker.database.relations.BudgetWithDayDetails
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.BudgetModelClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.toLong
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Dao
interface BudgetDao {
    //Normal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetModelClass: BudgetModelClass): Long

    @Delete
    suspend fun delete(budgetModelClass: BudgetModelClass)

    @Update
    suspend fun update(budgetModelClass: BudgetModelClass)

    @Query("SELECT SUM(amount) FROM transaction_table where date = :date and type = :type")
    suspend fun getTotalAmountForDate(date: Long, type: String): BigDecimal?

    @Query("DELETE FROM budget_table")
    suspend fun deleteAllBudget()

    @Query("SELECT * FROM budget_table WHERE budgetId = :id")
    suspend fun getBudgetById(id: Long): BudgetModelClass

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudget: Flow<BudgetModelClass>

    @get:Query("SELECT * FROM budget_table ORDER BY budgetId DESC")
    val allBudget: Flow<List<BudgetModelClass>>

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudgetWithoutFlow: BudgetWithDayDetails?

    //Relations

    @get:Query("SELECT * FROM budget_table WHERE isActive = 1")
    val getCurrentBudgetWithDays: Flow<BudgetWithDayDetails?>

    @Query("SELECT * FROM budget_table WHERE budgetId == :budgetId ")
    fun getBudgetWithDays(budgetId: Long): Flow<BudgetWithDayDetails?>


    // Transaction Supporting

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(budgetDayModelClass: BudgetDayModelClass): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDays(budgetDayModelClass: BudgetDayModelClass)

    @Delete
    suspend fun removeDays(budgetDayModelClass: BudgetDayModelClass)

    @Query("SELECT * FROM budget_day_table WHERE date == :date AND budgetId == :budgetId")
    fun getSingleBudgetDay(date: Long, budgetId: Long): BudgetDayModelClass?

    @Query("UPDATE budget_day_table SET budgetAmount = :newBudgetAmount WHERE date = :date AND budgetDayId = :budgetDayId AND  budgetId = :budgetId ")
    suspend fun updateBudgetAmount(
        date: Long,
        budgetDayId: Long,
        budgetId: Long,
        newBudgetAmount: BigDecimal
    )

    //Transactions
    @Transaction
    suspend fun insertWithDayDetails(
        budgetModelClass: BudgetModelClass, validDatesListFromLong: List<Long>
    ) {
        val transactionId = insert(budgetModelClass)
        for (date in validDatesListFromLong) {
            val budgetDayClass = BudgetDayModelClass(
                budgetDayId = 0L,
                date = date,
                budgetAmount = budgetModelClass.budgetAmountPerDay,
                totalExpense = getTotalAmountForDate(date, EXPENSE) ?: BigDecimal.ZERO,
                totalIncome = getTotalAmountForDate(date, INCOME) ?: BigDecimal.ZERO,
                totalExpenseTransactionCount = 0L,
                totalIncomeTransactionCount = 0L,
                budgetId = transactionId
            )
            insertDays(budgetDayClass)
        }
        var currentBudget = getCurrentBudgetWithoutFlow
        if (currentBudget != null) {
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

    @Transaction
    suspend fun updateWithDayDetails(
        budgetModelClass: BudgetModelClass,
        validDatesListFromLong: List<Long>,
        validDatesListFromPreviousBudget: List<BudgetDayModelClass>
    ) {
        update(budgetModelClass)

        // Convert previous budget dates to a set for quick lookup
        val previousDatesSet = validDatesListFromPreviousBudget.map { it.date }.toSet()
        val newDatesSet = validDatesListFromLong.toSet()

        // Find dates that need to be removed (present in previous but not in new budget)
        val datesToRemove = validDatesListFromPreviousBudget.filter { it.date !in newDatesSet }

        // Find dates that need to be added (present in new budget but missing in previous)
        val datesToAdd = validDatesListFromLong.filter { it !in previousDatesSet }

        // Find dates to update (present in both lists, but we may need to update them)
        val datesToUpdate = validDatesListFromPreviousBudget.filter { it.date in newDatesSet }


        datesToRemove.forEach {
            removeDays(it)
        }

        datesToUpdate.forEach {
            val updatedObj = it.copy(
                budgetId = budgetModelClass.budgetId,
                budgetAmount = budgetModelClass.budgetAmountPerDay
            )
            updateDays(updatedObj)
        }

        // Insert new dates (convert from Long to BudgetDayModelClass)
        insertWithDayDetails(budgetModelClass, datesToAdd)

        var currentBudget = getCurrentBudgetWithoutFlow
        if (currentBudget != null) {
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
                }else{
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
}


