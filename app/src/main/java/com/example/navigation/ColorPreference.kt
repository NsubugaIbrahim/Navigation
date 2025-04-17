package com.example.navigation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ColorPreference(private val context: Context) {
    private val APP_COLOR_KEY = longPreferencesKey("app_color")

    val appColor: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[APP_COLOR_KEY] ?: 0xFFFFC107 // Default color
        }

    suspend fun saveAppColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[APP_COLOR_KEY] = color
        }
    }
} 