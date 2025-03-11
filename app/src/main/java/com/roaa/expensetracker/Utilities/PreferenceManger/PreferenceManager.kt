package com.roaa.expensetracker.Utilities.PreferenceManger

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.roaa.expensetracker.Composables.ThemeMode
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.BankAccountsSerializer
import com.roaa.expensetracker.Model.UiDateModels.CurrencyClass
import com.roaa.expensetracker.Model.UiDateModels.CurrencyClassSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
val Context.dataStoreBank by dataStore("bank_file.json", serializer = BankAccountsSerializer)
val Context.dataStoreCurrency by dataStore(
    "currency_file.json",
    serializer = CurrencyClassSerializer
)

class PreferenceManager @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore = context.dataStore
    private val dataStoreBank = context.dataStoreBank
    private val dataStoreCurrency = context.dataStoreCurrency

    // Save float value
    suspend fun saveFloatValue(value: Float, key: Preferences.Key<Float>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getPrimaryBankAccount(): Flow<BankAccountsClass> =
        context.dataStoreBank.data

    suspend fun setPrimaryBankAccount(bankAccountsClass: BankAccountsClass) {
        context.dataStoreBank.updateData {
            it.copy(
                bankAccountId = bankAccountsClass.bankAccountId,
                initialAmount = bankAccountsClass.initialAmount,
                currentAmount = bankAccountsClass.currentAmount,
                bankName = bankAccountsClass.bankName,
                cardColorNumber = bankAccountsClass.cardColorNumber,
                cardIconNumber = bankAccountsClass.cardIconNumber,
                accountType = bankAccountsClass.accountType
            )
        }
    }

    // Retrieve float value
    fun getFloatValue(key: Preferences.Key<Float>): Flow<Float> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: 1f
            }
    }

    //save Boolean value
    suspend fun saveBooleanValue(value: Boolean, key: Preferences.Key<Boolean>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve Boolean value
    fun getBooleanValue(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: false
            }
    }

    //save Boolean value
    suspend fun saveStringValue(value: String, key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve Boolean value
    fun getStringValue(key: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }
    }

    //save Long value
    suspend fun saveLongValue(value: Long, key: Preferences.Key<Long>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve Long value
    fun getLongValue(key: Preferences.Key<Long>): Flow<Long> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: 0L
            }
    }

    // Save Theme value
    suspend fun saveThemeValue(value: String, key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve Theme value
    fun getThemeValue(key: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ThemeMode.SYSTEM.toString()
            }
    }

    fun getCurrency(): Flow<CurrencyClass> =
        dataStoreCurrency.data

    suspend fun setCurrency(currencyClass: CurrencyClass) {
        dataStoreCurrency.updateData {
            it.copy(
                countryName = currencyClass.countryName,
                currencyCode = currencyClass.currencyCode,
                currencyDisplayName = currencyClass.currencyDisplayName,
                currencySymbol = currencyClass.currencySymbol,
                flag = currencyClass.flag
            )
        }
    }
}