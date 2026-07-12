package com.playroom.feature.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomDialog
import com.playroom.core.designsystem.component.PlayroomPrimaryButton
import com.playroom.core.designsystem.component.PlayroomSecondaryButton
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import kotlinx.coroutines.launch

/**
 * Fully fake game screen — no real emulation. Back press always opens/closes
 * the pause menu here; the only way out is the explicit 게임 종료 button,
 * per work order §22/§24.
 */
@Composable
fun PlayerRoute(
    gameId: Int,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PlayerViewModel(gameId = gameId) }
        },
    ),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalPlayroomSnackbarHostState.current
    val scope = rememberCoroutineScope()

    BackHandler(enabled = !uiState.isPaused) { viewModel.openPauseMenu() }

    PlayerScreen(
        uiState = uiState,
        modifier = modifier,
        onPauseClick = viewModel::openPauseMenu,
        onResumeClick = viewModel::closePauseMenu,
        onQuickSave = { scope.launch { snackbarHostState.showSnackbar("빠른 저장을 완료했습니다") } },
        onQuickLoad = { scope.launch { snackbarHostState.showSnackbar("빠른 저장을 불러왔습니다") } },
        onDisplaySettings = { scope.launch { snackbarHostState.showSnackbar("화면 조절 설정입니다") } },
        onButtonSettings = { scope.launch { snackbarHostState.showSnackbar("버튼 조절 설정입니다") } },
        onGamepadSettings = { scope.launch { snackbarHostState.showSnackbar("게임패드 설정입니다") } },
        onExitGame = {
            viewModel.closePauseMenu()
            scope.launch { snackbarHostState.showSnackbar("자동 저장 후 게임을 종료했습니다") }
            onExit()
        },
    )
}

@Composable
private fun PlayerScreen(
    uiState: PlayerUiState,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onQuickSave: () -> Unit,
    onQuickLoad: () -> Unit,
    onDisplaySettings: () -> Unit,
    onButtonSettings: () -> Unit,
    onGamepadSettings: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(palette.cardGradient)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.SportsEsports,
                contentDescription = null,
                tint = palette.primary.copy(alpha = 0.55f),
                modifier = Modifier.size(120.dp),
            )
        }

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            IconButton(
                onClick = onPauseClick,
                modifier = Modifier
                    .padding(PlayroomSpacing.Medium)
                    .size(PlayroomSpacing.MinTouchTarget)
                    .background(palette.surface.copy(alpha = 0.85f), RoundedCornerShape(PlayroomCorner.Medium)),
            ) {
                Icon(imageVector = Icons.Filled.Pause, contentDescription = "일시정지", tint = palette.textPrimary)
            }
        }

        uiState.game?.let { game ->
            Text(
                text = game.title,
                style = PlayroomType.CardTitle,
                color = palette.textPrimary,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(top = PlayroomSpacing.Medium),
            )
        }
    }

    if (uiState.isPaused) {
        PauseMenuDialog(
            gameTitle = uiState.game?.title.orEmpty(),
            onResume = onResumeClick,
            onQuickSave = onQuickSave,
            onQuickLoad = onQuickLoad,
            onDisplaySettings = onDisplaySettings,
            onButtonSettings = onButtonSettings,
            onGamepadSettings = onGamepadSettings,
            onExit = onExitGame,
        )
    }
}

@Composable
private fun PauseMenuDialog(
    gameTitle: String,
    onResume: () -> Unit,
    onQuickSave: () -> Unit,
    onQuickLoad: () -> Unit,
    onDisplaySettings: () -> Unit,
    onButtonSettings: () -> Unit,
    onGamepadSettings: () -> Unit,
    onExit: () -> Unit,
) {
    val palette = PlayroomTheme.palette

    PlayroomDialog(onDismissRequest = onResume) {
        Text(text = "게임 일시정지", style = PlayroomType.SectionTitle, color = palette.textPrimary)
        if (gameTitle.isNotEmpty()) {
            Text(
                text = gameTitle,
                style = PlayroomType.BodyMedium,
                color = palette.textSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = PlayroomSpacing.Large),
            )
        }

        val menuItemGap = PlayroomSpacing.Small

        Column(verticalArrangement = Arrangement.spacedBy(menuItemGap)) {
            Row(horizontalArrangement = Arrangement.spacedBy(menuItemGap)) {
                PlayroomPrimaryButton(text = "▶ 계속하기", onClick = onResume, modifier = Modifier.weight(1f))
                PlayroomSecondaryButton(text = "\uD83D\uDCBE 빠른 저장", onClick = onQuickSave, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(menuItemGap)) {
                PlayroomSecondaryButton(text = "\u21BB 빠른 불러오기", onClick = onQuickLoad, modifier = Modifier.weight(1f))
                PlayroomSecondaryButton(text = "화면 조절", onClick = onDisplaySettings, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(menuItemGap)) {
                PlayroomSecondaryButton(text = "버튼 조절", onClick = onButtonSettings, modifier = Modifier.weight(1f))
                PlayroomSecondaryButton(text = "게임패드", onClick = onGamepadSettings, modifier = Modifier.weight(1f))
            }
            PlayroomSecondaryButton(
                text = "게임 종료",
                onClick = onExit,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
