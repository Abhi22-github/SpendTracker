package com.roaa.expensetracker.Utilities.PreferenceManger

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.roaa.expensetracker.Composables.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class PreferenceManager @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore = context.dataStore

    // Save float value
    suspend fun saveFloatValue(value: Float, key: Preferences.Key<Float>) {
        dataStore.edit { preferences ->
            preferences[key] = value
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
    suspend fun saveLongValue(value: Long, key: Preferences.Key<Long>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve Boolean value
    fun getLongValue(key: Preferences.Key<Long>): Flow<Long> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: 1L
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
}