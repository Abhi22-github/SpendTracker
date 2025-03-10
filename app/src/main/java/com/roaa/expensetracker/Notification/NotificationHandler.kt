package com.roaa.expensetracker.Notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.PreferenceManger.PREFERENCES_NAME


@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    // Check if the permission is already granted
    val notificationPermissionState = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
    }

    LaunchedEffect(notificationPermissionState) {
        if (isFirstStartup(context) && notificationPermissionState != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            setFirstStartupCompleted(context)
        } else {
            onPermissionGranted()
        }
    }
}

fun createNotificationChannel(
    context: Context,
    notificationChannel: NotificationChannelInfoClass
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            notificationChannel.notificationChannelId, // Channel ID
            notificationChannel.notificationChannelName, // Channel name
            NotificationManager.IMPORTANCE_DEFAULT // Importance level
        ).apply {
            description = notificationChannel.notificationChannelDescription
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun sendNotification(context: Context, notificationChannel: NotificationChannelInfoClass, title: String, message: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationId = 1 // Unique ID for the notification
    val notificationBuilder = NotificationCompat.Builder(context, notificationChannel.notificationChannelId)
        .setSmallIcon(R.drawable.notification_icon) // Notification icon
        .setContentTitle(title) // Title of the notification
        .setContentText(message) // Message of the notification
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true) // Automatically dismiss the notification when tapped

    notificationManager.notify(notificationId, notificationBuilder.build())
}

fun isFirstStartup(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("is_first_startup", true)
}

fun setFirstStartupCompleted(context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("is_first_startup", false).apply()
}