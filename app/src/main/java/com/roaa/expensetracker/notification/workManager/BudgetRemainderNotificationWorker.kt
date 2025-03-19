package com.roaa.expensetracker.notification.workManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.roaa.expensetracker.activity.ComposeMainActivity
import com.roaa.expensetracker.notification.budgetNotificationChannel
import com.roaa.expensetracker.notification.sendNotification

class BudgetRemainderNotificationWorker(val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
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

        return Result.success()
    }


}
