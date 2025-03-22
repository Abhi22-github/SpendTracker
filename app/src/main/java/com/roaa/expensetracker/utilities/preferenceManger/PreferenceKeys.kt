package com.roaa.expensetracker.utilities.preferenceManger

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val PREFERENCES_NAME = "Settings"
val IS_APP_FIRST_STARTUP = booleanPreferencesKey("is_app_first_startup")
val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
val TOTAL_BUDGET_FOR_MONTH = floatPreferencesKey("total_budget_for_month")
val LAST_USED_EXPENSE_CATEGORY_ID = longPreferencesKey("last_used_expense_category_id")
val LAST_USED_INCOME_CATEGORY_ID = longPreferencesKey("last_used_income_category_id")
val LAST_USED_BANK_ID = longPreferencesKey("last_used_bank_id")
val IS_ONE_DEFAULT_CATEGORY_SET = booleanPreferencesKey("is_one_default_category_set")
val TOTAL_BUDGET_PER_DAY = floatPreferencesKey("total_budget_per_day")
val CURRENT_BUDGET_MONTH_NAME = stringPreferencesKey("current_month_name")
val CURRENT_BUDGET_DURATION = longPreferencesKey("current_budget_duration")
val CURRENT_BUDGET_START_DATE = longPreferencesKey("current_budget_start_date")
val CURRENT_BUDGET_END_DATE = longPreferencesKey("current_budget_end_date")
val IS_BUDGET_SET = booleanPreferencesKey("is_budget_set")
val THEME_MODE = stringPreferencesKey("theme_mode")
val SHOW_EXPERIMENTAL_COMPONENTS = booleanPreferencesKey("show_experimental_components")
val PRIMARY_BANK_ACCOUNT = longPreferencesKey("primary_bank_account")