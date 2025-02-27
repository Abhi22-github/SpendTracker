package com.roaa.expensetracker.Utilities.PreferenceManger

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val PREFERENCES_NAME = "Settings"
val TOTAL_BUDGET_FOR_MONTH = floatPreferencesKey("total_budget_for_month")
val TOTAL_BUDGET_PER_DAY = floatPreferencesKey("total_budget_per_day")
val CURRENT_BUDGET_MONTH_NAME = stringPreferencesKey("current_month_name")
val CURRENT_BUDGET_DURATION = longPreferencesKey("current_budget_duration")
val CURRENT_BUDGET_START_DATE = longPreferencesKey("current_budget_start_date")
val CURRENT_BUDGET_END_DATE = longPreferencesKey("current_budget_end_date")
val IS_BUDGET_SET = booleanPreferencesKey("is_budget_set")
val THEME_MODE = stringPreferencesKey("theme_mode")
val SHOW_EXPERIMENTAL_COMPONENTS = booleanPreferencesKey("show_experimental_components")
val PRIMARY_BANK_ACCOUNT = longPreferencesKey("primary_bank_account")