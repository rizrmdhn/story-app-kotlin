package com.rizrmdhn.core.data.source.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizrmdhn.core.common.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS)

class SettingPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private val themeKey = booleanPreferencesKey(Constants.THEME_KEY)
    private val authKey = stringPreferencesKey(Constants.AUTH_KEY)
    private val localeKey = stringPreferencesKey(Constants.LOCALE_KEY)
    private val locationKey = intPreferencesKey(Constants.LOCATION_KEY)

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkMode
        }
    }

    fun getAuthToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[authKey] ?: ""
        }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[authKey] = token
        }
    }

    suspend fun removeAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(authKey)
        }
    }

    fun getLocaleSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[localeKey] ?: "en"
        }
    }

    suspend fun saveLocaleSetting(locale: String) {
        dataStore.edit { preferences ->
            preferences[localeKey] = locale
        }
    }

    fun getLocationSetting(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[locationKey] ?: 0
        }
    }

    suspend fun saveLocationSetting(location: Int) {
        dataStore.edit { preferences ->
            preferences[locationKey] = location
        }
    }
}