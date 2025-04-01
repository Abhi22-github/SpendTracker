package com.roaa.expensetracker.notification.workManager

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

fun scheduleBudgetExpiryStatus(
    context: Context,
    date: LocalDate,
    budgetId: Long
) {

    // for setting up budget status to false
    val budgetStatusTimeMillis =
        date.plusDays(1L).atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val statusDelay = budgetStatusTimeMillis - System.currentTimeMillis()
    if (statusDelay <= 0) return // Skip scheduling if the time has passed

    val budgetStatusWorkRequest =
        OneTimeWorkRequestBuilder<BudgetExpiryWorker>().setInitialDelay(
            statusDelay,
            TimeUnit.MILLISECONDS
        ).setInputData(workDataOf("budget_id" to budgetId))
            .build()

    WorkManager.getInstance(context)
        .enqueueUniqueWork(
            "Budget expiry $budgetStatusTimeMillis",
            ExistingWorkPolicy.REPLACE,
            budgetStatusWorkRequest
        )

    // for setting up budget expiry Notification
    val budgetExpiryNotificationTimeMillis =
        date.plusDays(1L).atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val notificationDelay = budgetExpiryNotificationTimeMillis - System.currentTimeMillis()
    if (notificationDelay <= 0) return // Skip scheduling if the time has passed

    val budgetNotificationWorkRequest =
        OneTimeWorkRequestBuilder<BudgetRemainderNotificationWorker>().setInitialDelay(
            notificationDelay,
            TimeUnit.MILLISECONDS
        ).setInputData(workDataOf("budgetNotificationType" to 2))
            .build()

    WorkManager.getInstance(context)
        .enqueueUniqueWork(
            "Budget expiry (expired) $budgetExpiryNotificationTimeMillis",
            ExistingWorkPolicy.REPLACE,
            budgetNotificationWorkRequest
        )


}
