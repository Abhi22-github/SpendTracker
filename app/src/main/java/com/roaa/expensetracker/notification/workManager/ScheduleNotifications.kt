package com.roaa.expensetracker.notification.workManager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Create constraints (optional)
val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED)
    .setRequiresDeviceIdle(false).setRequiresBatteryNotLow(false).setRequiresCharging(false).build()

fun scheduleDailyNotification(context: Context) {
    val workManager = WorkManager.getInstance(context)

    // Calculate initial delay to 8 PM
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    if (Calendar.getInstance().after(calendar)) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

    // Create periodic work request
    val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
        24, // Repeat interval
        TimeUnit.HOURS
    ).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS).setConstraints(constraints).build()

    // Enqueue unique work
    workManager.enqueueUniquePeriodicWork(
        "daily_8pm_notification", ExistingPeriodicWorkPolicy.REPLACE, dailyWorkRequest
    )
}

fun scheduleBudgetReminder(context: Context, budgetEndDate: LocalDate) {

    // Schedule at 5:00 PM
    scheduleNotificationAtTime(context, budgetEndDate, 17, 0) // 17:00 (5 PM)

    // Schedule at 9:00 PM
    scheduleNotificationAtTime(context, budgetEndDate, 21, 0) // 21:00 (9 PM)
}

private fun scheduleNotificationAtTime(context: Context, date: LocalDate, hour: Int, minute: Int) {
    val notificationTimeMillis =
        date.atTime(hour, minute).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val delay = notificationTimeMillis - System.currentTimeMillis()
    if (delay <= 0) return // Skip scheduling if the time has passed

    val workRequest =
        OneTimeWorkRequestBuilder<BudgetRemainderNotificationWorker>().setInitialDelay(
            delay,
            TimeUnit.MILLISECONDS
        ).build()

    WorkManager.getInstance(context)
        .enqueueUniqueWork(
            "Budget $notificationTimeMillis",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
}