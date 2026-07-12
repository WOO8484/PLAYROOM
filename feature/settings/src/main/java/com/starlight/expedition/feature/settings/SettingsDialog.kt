package com.starlight.expedition.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.starlight.expedition.core.designsystem.component.SettingRow
import com.starlight.expedition.core.designsystem.component.SettingsCloseButton
import com.starlight.expedition.core.designsystem.component.SettingsSwitch
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 설정 화면은 중앙 고정 Dialog로 구현합니다. 빠른 시작 화면에는 설정 버튼이 없으므로
 * 이 Dialog는 홈·즐겨찾기·게임리스트에서 톱니바퀴를 눌렀을 때만 호출됩니다.
 */
@Composable
fun SettingsDialog(
    uiState: SettingsUiState,
    onDismiss: () -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onAutoSaveChange: (Boolean) -> Unit,
    onDarkModeChange: (Boolean) -> Unit
) {
    val colors = StarlightTheme.colors

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(StarlightTheme.shapes.settingsPanel)
                .background(colors.surface)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "설정", style = StarlightTheme.typography.settingsTitle, color = colors.textPrimary)
                SettingsCloseButton(onClick = onDismiss)
            }

            SettingRow(
                icon = "\u266A",
                title = "효과음",
                description = "버튼 및 게임 시작 소리"
            ) {
                SettingsSwitch(
                    checked = uiState.soundEnabled,
                    onCheckedChange = onSoundChange,
                    contentDescription = "효과음 켜기 또는 끄기"
                )
            }
            HorizontalDivider(color = colors.line)

            SettingRow(
                icon = "\u2713",
                title = "자동 저장",
                description = "게임 종료 시 진행 상황 저장"
            ) {
                SettingsSwitch(
                    checked = uiState.autoSaveEnabled,
                    onCheckedChange = onAutoSaveChange,
                    contentDescription = "자동 저장 켜기 또는 끄기"
                )
            }
            HorizontalDivider(color = colors.line)

            SettingRow(
                icon = if (uiState.darkModeEnabled) "\u2600" else "\u263E",
                title = "다크 모드",
                description = "눈이 편안한 어두운 화면"
            ) {
                SettingsSwitch(
                    checked = uiState.darkModeEnabled,
                    onCheckedChange = onDarkModeChange,
                    contentDescription = "다크 모드 켜기 또는 끄기"
                )
            }
            HorizontalDivider(color = colors.line)

            SettingRow(
                icon = "\u2726",
                title = "앱 정보",
                description = "별빛 탐험대 네이티브 1.0.0"
            ) {
                Text(text = "\u203A", color = colors.textMuted)
            }
        }
    }
}
