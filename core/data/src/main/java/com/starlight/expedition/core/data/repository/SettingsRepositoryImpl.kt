package com.starlight.expedition.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.starlight.expedition.core.data.local.PreferencesKeys
import com.starlight.expedition.core.data.local.starlightDataStore
import com.starlight.expedition.core.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val context: Context
) : SettingsRepository {

    override fun observeSettings(): Flow<AppSettings> =
        context.starlightDataStore.data.map { preferences ->
            AppSettings(
                darkModeEnabled = preferences[PreferencesKeys.DARK_MODE_ENABLED] ?: false,
                soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true,
                autoSaveEnabled = preferences[PreferencesKeys.AUTO_SAVE_ENABLED] ?: true
            )
        }

    override suspend fun setDarkMode(enabled: Boolean) {
        context.starlightDataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE_ENABLED] = enabled
        }
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        context.starlightDataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    override suspend fun setAutoSaveEnabled(enabled: Boolean) {
        context.starlightDataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_SAVE_ENABLED] = enabled
        }
    }
}
