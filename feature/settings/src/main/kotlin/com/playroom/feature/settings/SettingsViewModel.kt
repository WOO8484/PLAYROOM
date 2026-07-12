package com.playroom.feature.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Holds only the toggles/cycles that are genuinely local to 설정
 * (자동 저장, 진동, 화면 방향, 게임패드 자동 인식). App-wide concerns — the
 * active [com.playroom.core.model.AppSkin] and whether onboarding has been
 * seen — are owned by `:app` and passed into [SettingsScreen] as
 * parameters, since they affect the whole app, not just this screen.
 */
class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleAutoSave() {
        _uiState.update { it.copy(autoSaveEnabled = !it.autoSaveEnabled) }
    }

    fun toggleVibration() {
        _uiState.update { it.copy(vibrationEnabled = !it.vibrationEnabled) }
    }

    fun toggleGamepadAutoDetect() {
        _uiState.update { it.copy(gamepadAutoDetectEnabled = !it.gamepadAutoDetectEnabled) }
    }

    fun cycleOrientation() {
        _uiState.update { it.copy(screenOrientation = it.screenOrientation.next()) }
    }
}
