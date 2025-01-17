package com.example.expensetracker.Utilities.PreferenceManger

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
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

    suspend fun saveBooleanValue(value: Boolean, key: Preferences.Key<Boolean>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Retrieve float value
    fun getBooleanValue(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: false
            }
    }
}