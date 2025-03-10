package com.roaa.expensetracker.Notification

val GENERAL_CHANNEL_ID = "general_notifications"
val GENERAL_CHANNEL_NAME = "General Notifications"
val GENERAL_CHANNEL_DESCRIPTION = "used for general notifications"

val BUDGET_CHANNEL_ID = "budget_notifications"
val BUDGET_CHANNEL_NAME = "Budget Notifications"
val BUDGET_CHANNEL_DESCRIPTION = "used for notifications related to budget"


val generalNotificationChannel = NotificationChannelInfoClass(
    GENERAL_CHANNEL_ID,
    GENERAL_CHANNEL_NAME, GENERAL_CHANNEL_DESCRIPTION
)

val budgetNotificationChannel = NotificationChannelInfoClass(
    BUDGET_CHANNEL_ID,
    BUDGET_CHANNEL_NAME, BUDGET_CHANNEL_DESCRIPTION
)


val notificationChannelList = listOf(
    generalNotificationChannel, budgetNotificationChannel
)