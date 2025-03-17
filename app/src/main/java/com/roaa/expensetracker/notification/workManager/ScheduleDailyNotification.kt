package com.roaa.expensetracker.notification.workManager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context) {
    val workManager = WorkManager.getInstance(context)

    // Create constraints (optional)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresDeviceIdle(false)
        .setRequiresBatteryNotLow(false)
        .setRequiresCharging(false)
        .build()

    // Calculate initial delay to 8 PM
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    // If it's already past 8 PM, schedule for next day
    if (Calendar.getInstance().after(calendar)) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

    // Create periodic work request
    val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        24, // Repeat interval
        TimeUnit.HOURS
    )
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    // Enqueue unique work
    workManager.enqueueUniquePeriodicWork(
        "daily_8pm_notification",
        ExistingPeriodicWorkPolicy.REPLACE,
        dailyWorkRequest
    )
}