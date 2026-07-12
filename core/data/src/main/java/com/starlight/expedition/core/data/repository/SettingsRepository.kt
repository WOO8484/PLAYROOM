package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setAutoSaveEnabled(enabled: Boolean)
}
