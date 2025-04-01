package com.roaa.expensetracker.notification.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.roaa.expensetracker.database.AppDatabase
import dagger.assisted.AssistedInject

class BudgetExpiryWorker @AssistedInject constructor(
    val context: Context,
    params: WorkerParameters,
    private val appDatabase: AppDatabase
) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        // Retrieve the budget ID passed as input
        val budgetId = inputData.getLong("budget_id", -1)

        // Ensure that the budget ID is valid
        if (budgetId == -1L) {
            return Result.failure() // Invalid budget ID
        }

        val budget = appDatabase.budgetDao().getBudgetById(budgetId)
        val updatedBudget = budget.copy(isActive = false)
        appDatabase.budgetDao().update(updatedBudget)

        return Result.success()
    }


}