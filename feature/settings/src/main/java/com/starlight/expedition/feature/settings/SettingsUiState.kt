package com.starlight.expedition.feature.settings

data class SettingsUiState(
    val darkModeEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
    val autoSaveEnabled: Boolean = true
)
