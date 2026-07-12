package com.playroom.feature.settings

/** 화면 방향 cycle values (자동 → 가로 → 세로 → 자동 …). Local to this screen only. */
enum class ScreenOrientationOption(val label: String) {
    AUTO("자동"),
    LANDSCAPE("가로"),
    PORTRAIT("세로"),
    ;

    fun next(): ScreenOrientationOption {
        val values = entries
        return values[(ordinal + 1) % values.size]
    }
}

data class SettingsUiState(
    val autoSaveEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val gamepadAutoDetectEnabled: Boolean = true,
    val screenOrientation: ScreenOrientationOption = ScreenOrientationOption.AUTO,
)
