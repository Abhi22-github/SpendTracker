package com.roaa.expensetracker.notification.workManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.roaa.expensetracker.activity.ComposeMainActivity
import com.roaa.expensetracker.notification.budgetNotificationChannel
import com.roaa.expensetracker.notification.sendNotification

/**
 * budget notification Types
 * 1 -- budget end date message - Budget ending soon
 * 2 -- budget expiry message - Budget expired
 */
class BudgetRemainderNotificationWorker(val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        // Retrieve the budget ID passed as input
        val type = inputData.getInt("budgetNotificationType", -1)

        if(type == -1){
            return Result.failure()
        }
        if(type == 1){
            val intent = Intent(context, ComposeMainActivity::class.java).apply {
                putExtra("SHOW_BUDGET", true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            // Create a PendingIntent to launch the app
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            sendNotification(
                context = context,
                notificationChannel = budgetNotificationChannel,
                title = "Budget Ending Soon!",
                message = "Your budget expires tomorrow. Review your spending.",
                pendingIntent = pendingIntent
            )
        }else if(type == 2){
            val intent = Intent(context, ComposeMainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            // Create a PendingIntent to launch the app
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            sendNotification(
                context = context,
                notificationChannel = budgetNotificationChannel,
                title = "Budget Expired!",
                message = "Your budget has expired. Set your new budget now.",
                pendingIntent = pendingIntent
            )
        }


        return Result.success()
    }


}
