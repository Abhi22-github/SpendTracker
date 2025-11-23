package com.roaa.expensetracker

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Check if the build is Debug or Release
        val isDebuggable = (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0

        // Enable Crashlytics only in Release mode
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!isDebuggable)

        // Optional: Disable Firebase Analytics for Debug
        Firebase.analytics.setAnalyticsCollectionEnabled(!isDebuggable)
    }
}