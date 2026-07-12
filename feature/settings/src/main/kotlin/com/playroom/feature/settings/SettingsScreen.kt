package com.playroom.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomSecondaryButton
import com.playroom.core.designsystem.component.playroomContentWidth
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomPalette
import com.playroom.core.designsystem.theme.PlayroomPalettes
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.model.AppSkin
import kotlinx.coroutines.launch

@Composable
fun SettingsRoute(
    currentSkin: AppSkin,
    onSkinSelected: (AppSkin) -> Unit,
    onManageGameFolder: () -> Unit,
    onReplayOnboarding: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalPlayroomSnackbarHostState.current
    val scope = rememberCoroutineScope()

    fun notify(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    SettingsScreen(
        uiState = uiState,
        currentSkin = currentSkin,
        contentPadding = contentPadding,
        onManageGameFolder = onManageGameFolder,
        onRescan = { notify("게임 목록이 최신 상태입니다") },
        onBackup = { notify("저장 데이터 ZIP 백업을 준비했습니다") },
        onToggleAutoSave = viewModel::toggleAutoSave,
        onToggleVibration = viewModel::toggleVibration,
        onCycleOrientation = viewModel::cycleOrientation,
        onSkinSelected = { skin -> onSkinSelected(skin); notify("${skin.label} 스킨을 적용했습니다") },
        onToggleGamepadAutoDetect = viewModel::toggleGamepadAutoDetect,
        onGamepadButtonTest = { notify("게임패드 버튼 테스트 화면입니다") },
        onReplayOnboarding = onReplayOnboarding,
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    currentSkin: AppSkin,
    contentPadding: PaddingValues,
    onManageGameFolder: () -> Unit,
    onRescan: () -> Unit,
    onBackup: () -> Unit,
    onToggleAutoSave: () -> Unit,
    onToggleVibration: () -> Unit,
    onCycleOrientation: () -> Unit,
    onSkinSelected: (AppSkin) -> Unit,
    onToggleGamepadAutoDetect: () -> Unit,
    onGamepadButtonTest: () -> Unit,
    onReplayOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = horizontalPadding, vertical = PlayroomSpacing.Medium)
            .playroomContentWidth(),
    ) {
        Text(text = "설정", style = PlayroomType.SectionTitle, color = palette.textPrimary)
        Text(
            text = "복잡한 기능은 숨기고 자주 쓰는 항목만 정리했습니다",
            style = PlayroomType.SectionSubtitle,
            color = palette.textSecondary,
            modifier = Modifier.padding(bottom = PlayroomSpacing.Large),
        )

        SettingSection(title = "게임 관리") {
            SettingActionRow(
                title = "게임 폴더",
                subtitle = "/내 파일/게임룸 · 8개 게임",
                actionLabel = "관리",
                onClick = onManageGameFolder,
            )
            SettingActionRow(
                title = "게임 다시 검색",
                subtitle = "추가·삭제된 게임과 메타정보를 확인합니다",
                actionLabel = "검색",
                onClick = onRescan,
            )
            SettingActionRow(
                title = "저장 데이터 백업",
                subtitle = "세이브 파일을 ZIP으로 내보냅니다",
                actionLabel = "백업",
                onClick = onBackup,
            )
        }

        SettingSection(title = "플레이 환경") {
            SettingToggleRow(
                title = "자동 저장",
                subtitle = "게임 종료와 백그라운드 전환 시 저장",
                checked = uiState.autoSaveEnabled,
                onToggle = onToggleAutoSave,
            )
            SettingToggleRow(
                title = "진동",
                subtitle = "가상 버튼 입력 시 가볍게 진동",
                checked = uiState.vibrationEnabled,
                onToggle = onToggleVibration,
            )
            SettingActionRow(
                title = "화면 방향",
                subtitle = "게임에 맞춰 자동 전환",
                actionLabel = uiState.screenOrientation.label,
                onClick = onCycleOrientation,
            )
        }

        SettingSection(title = "앱 스킨") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
            ) {
                AppSkin.entries.forEach { skin ->
                    SkinSwatch(
                        skin = skin,
                        selected = skin == currentSkin,
                        onClick = { onSkinSelected(skin) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        SettingSection(title = "게임패드") {
            SettingToggleRow(
                title = "자동 인식",
                subtitle = "블루투스·USB 게임패드 연결 시 자동 적용",
                checked = uiState.gamepadAutoDetectEnabled,
                onToggle = onToggleGamepadAutoDetect,
            )
            SettingActionRow(
                title = "버튼 테스트",
                subtitle = "연결한 패드의 입력을 확인합니다",
                actionLabel = "열기",
                onClick = onGamepadButtonTest,
            )
        }

        PlayroomSecondaryButton(
            text = "환영 화면 다시 보기",
            onClick = onReplayOnboarding,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Small, bottom = PlayroomSpacing.ExtraLarge),
        )
    }
}

@Composable
private fun SettingSection(title: String, content: @Composable () -> Unit) {
    val palette = PlayroomTheme.palette
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = PlayroomSpacing.Large)
            .clip(RoundedCornerShape(PlayroomCorner.Large))
            .background(palette.surface)
            .padding(PlayroomSpacing.Large),
    ) {
        Text(
            text = title,
            style = PlayroomType.CardTitle,
            color = palette.textPrimary,
            modifier = Modifier.padding(bottom = PlayroomSpacing.Small),
        )
        content()
    }
}

@Composable
private fun SettingToggleRow(title: String, subtitle: String, checked: Boolean, onToggle: () -> Unit) {
    val palette = PlayroomTheme.palette
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PlayroomSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = PlayroomType.BodyMedium, color = palette.textPrimary)
            Text(text = subtitle, style = PlayroomType.Caption, color = palette.textSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(checkedTrackColor = palette.primary),
        )
    }
}

@Composable
private fun SettingActionRow(title: String, subtitle: String, actionLabel: String, onClick: () -> Unit) {
    val palette = PlayroomTheme.palette
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PlayroomSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = PlayroomType.BodyMedium, color = palette.textPrimary)
            Text(text = subtitle, style = PlayroomType.Caption, color = palette.textSecondary)
        }
        PlayroomSecondaryButton(text = actionLabel, onClick = onClick)
    }
}

@Composable
private fun SkinSwatch(skin: AppSkin, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val palette = PlayroomTheme.palette
    val skinPalette = paletteFor(skin)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(PlayroomCorner.Medium))
            .background(if (selected) palette.chipSelected else palette.surfaceSoft)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) palette.primary else palette.outline,
                shape = RoundedCornerShape(PlayroomCorner.Medium),
            )
            .padding(PlayroomSpacing.Small)
            .clickableNoRipple(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .clip(RoundedCornerShape(PlayroomCorner.Small))
                .background(Brush.linearGradient(skinPalette.cardGradient)),
        )
        Text(
            text = skin.label,
            style = PlayroomType.Caption,
            color = palette.textPrimary,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

private fun paletteFor(skin: AppSkin): PlayroomPalette = when (skin) {
    AppSkin.LIGHT -> PlayroomPalettes.Light
    AppSkin.DARK -> PlayroomPalettes.Dark
    AppSkin.RETRO -> PlayroomPalettes.Retro
    AppSkin.SIMPLE -> PlayroomPalettes.Simple
}

@Composable
private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = this.then(
    Modifier.clickable(
        indication = null,
        interactionSource = androidx.compose.runtime.remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        onClick = onClick,
    ),
)
