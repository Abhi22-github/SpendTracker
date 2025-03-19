package com.roaa.expensetracker.notification.workManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.roaa.expensetracker.activity.ComposeMainActivity
import com.roaa.expensetracker.notification.generalNotificationChannel
import com.roaa.expensetracker.notification.sendNotification

class DailyNotificationWorker(
    val context: Context,
     workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Send notification
        val intent = Intent(context, ComposeMainActivity::class.java).apply {
            putExtra("SHOW_ADD_TRANSACTION", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Create a PendingIntent to launch the app
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

       sendNotification(
           context = context,
           notificationChannel = generalNotificationChannel,
           title = "Expense Tracker",
           message = "Have you recorded your transactions today? ",
           pendingIntent = pendingIntent
       )
        return Result.success()
    }
}