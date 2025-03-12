package com.roaa.expensetracker.notification.workManager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        24, TimeUnit.HOURS // Repeat every 24 hours
    )
        .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS) // Delay until 8 PM
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_notification",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )
}

private fun getInitialDelay(): Long {
    val currentTime = System.currentTimeMillis()
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    if (calendar.timeInMillis < currentTime) {
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Schedule for the next day if time has passed
    }
    return calendar.timeInMillis - currentTime
}