package com.example.expensetracker.Utilities.PreferenceManger

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val PREFERENCES_NAME = "Settings"
val TOTAL_BUDGET = floatPreferencesKey("total_budget")
val IS_BUDGET_SET = booleanPreferencesKey("is_budget_set")
val THEME_MODE = stringPreferencesKey("theme_mode")
val SHOW_FORECAST = booleanPreferencesKey("show_forecast")