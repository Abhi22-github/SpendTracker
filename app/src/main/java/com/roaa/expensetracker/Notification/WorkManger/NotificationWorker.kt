package com.roaa.expensetracker.Notification.WorkManger

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.roaa.expensetracker.Notification.generalNotificationChannel
import com.roaa.expensetracker.Notification.sendNotification

class NotificationWorker(
    val context: Context,
     workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Send notification
       sendNotification(
           context = context,
           notificationChannel = generalNotificationChannel,
           title = "Expense Tracker",
           message = "Have you recorded your transactions today?"
       )
        return Result.success()
    }
}