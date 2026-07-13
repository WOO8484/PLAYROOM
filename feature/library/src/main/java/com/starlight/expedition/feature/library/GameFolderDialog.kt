package com.starlight.expedition.feature.library

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.starlight.expedition.core.designsystem.component.CardActionButton
import com.starlight.expedition.core.designsystem.component.CardActionEmphasis
import com.starlight.expedition.core.designsystem.component.SettingsCloseButton
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.GameScanState
import com.starlight.expedition.core.model.GameSourceFolder

/**
 * 게임 폴더 관리 다이얼로그입니다. 중앙 고정 형태로만 구현하고, 바텀시트로 만들지 않습니다(실행지시서 8.2절).
 */
@Composable
fun GameFolderDialog(
    uiState: GameFolderUiState,
    onDismiss: () -> Unit,
    onDialogOpened: () -> Unit,
    onFolderPicked: (String) -> Unit,
    onRemoveFolder: (String) -> Unit,
    onRescanFolder: (String) -> Unit,
    onRescanAll: () -> Unit,
    onCancelScan: () -> Unit
) {
    val colors = StarlightTheme.colors
    val context = LocalContext.current

    var folderPendingRemoval by remember { mutableStateOf<GameSourceFolder?>(null) }

    val openDocumentTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { treeUri ->
        if (treeUri != null) {
            context.contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onFolderPicked(treeUri.toString())
        }
    }

    LaunchedEffect(Unit) {
        onDialogOpened()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .clip(StarlightTheme.shapes.settingsPanel)
                .background(colors.surface)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "게임 폴더 관리", style = StarlightTheme.typography.settingsTitle, color = colors.textPrimary)
                SettingsCloseButton(onClick = onDismiss)
            }

            ScanStatusBanner(
                scanState = uiState.scanState,
                onCancelScan = onCancelScan,
                modifier = Modifier.padding(top = 14.dp)
            )

            if (uiState.folders.isEmpty()) {
                Column(modifier = Modifier.padding(top = 20.dp, bottom = 6.dp)) {
                    Text(
                        text = "게임 폴더가 등록되지 않았습니다.",
                        style = StarlightTheme.typography.settingRowTitle,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "게임 파일이 들어 있는 폴더를 추가해 주세요.",
                        style = StarlightTheme.typography.settingRowSubtitle,
                        color = colors.textMuted,
                        modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
                    )
                    CardActionButton(
                        text = "게임 폴더 추가",
                        onClick = { openDocumentTreeLauncher.launch(null) },
                        emphasis = CardActionEmphasis.PRIMARY,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                val scanningFolderId = (uiState.scanState as? GameScanState.Scanning)?.folderId

                LazyColumn(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = uiState.folders, key = GameSourceFolder::id) { folder ->
                        GameFolderManagementRow(
                            folder = folder,
                            scanInProgress = uiState.scanState is GameScanState.Scanning &&
                                (scanningFolderId == null || scanningFolderId == folder.id),
                            onRescan = { onRescanFolder(folder.id) },
                            onDisconnect = { folderPendingRemoval = folder }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardActionButton(
                        text = "폴더 추가",
                        onClick = { openDocumentTreeLauncher.launch(null) },
                        modifier = Modifier.weight(1f)
                    )
                    CardActionButton(
                        text = "전체 다시 검색",
                        onClick = onRescanAll,
                        emphasis = CardActionEmphasis.PRIMARY,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    val target = folderPendingRemoval
    if (target != null) {
        RemoveFolderConfirmDialog(
            folder = target,
            onConfirm = {
                onRemoveFolder(target.id)
                folderPendingRemoval = null
            },
            onCancel = { folderPendingRemoval = null }
        )
    }
}

@Composable
private fun ScanStatusBanner(
    scanState: GameScanState,
    onCancelScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors

    when (scanState) {
        is GameScanState.Scanning -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(StarlightTheme.shapes.summaryCard)
                    .background(colors.primarySoft)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(24.dp),
                        color = colors.primary
                    )
                    Text(
                        text = "${scanState.folderName} 검색 중",
                        style = StarlightTheme.typography.settingRowTitle,
                        color = colors.textPrimary
                    )
                }
                Text(
                    text = "확인한 파일 ${scanState.visitedCount}개 · 게임 후보 ${scanState.candidateCount}개" +
                        (scanState.currentName?.let { " · $it" } ?: ""),
                    style = StarlightTheme.typography.settingRowSubtitle,
                    color = colors.textMuted,
                    modifier = Modifier.padding(top = 6.dp)
                )
                CardActionButton(
                    text = "취소",
                    onClick = onCancelScan,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        is GameScanState.Completed -> {
            val summary = scanState.summary
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(StarlightTheme.shapes.summaryCard)
                    .background(colors.primarySoft)
                    .padding(14.dp)
            ) {
                Text(
                    text = "게임 ${summary.scannedFiles}개 파일 확인",
                    style = StarlightTheme.typography.settingRowTitle,
                    color = colors.textPrimary
                )
                Text(
                    text = "신규 ${summary.addedGames}개 · 업데이트 ${summary.updatedGames}개 · " +
                        "삭제 ${summary.removedGames}개 · 미분류 ${summary.unclassifiedFiles}개",
                    style = StarlightTheme.typography.settingRowSubtitle,
                    color = colors.textMuted,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        is GameScanState.Failed -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(StarlightTheme.shapes.summaryCard)
                    .background(colors.emptyBackground)
                    .padding(14.dp)
            ) {
                Text(text = scanState.message, style = StarlightTheme.typography.settingRowTitle, color = colors.textPrimary)
            }
        }

        is GameScanState.Interrupted -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(StarlightTheme.shapes.summaryCard)
                    .background(colors.emptyBackground)
                    .padding(14.dp)
            ) {
                Text(
                    text = "지난 실행에서 검색이 끝나기 전에 앱이 종료됐습니다. 다시 검색해 주세요.",
                    style = StarlightTheme.typography.settingRowSubtitle,
                    color = colors.textMuted
                )
            }
        }

        GameScanState.Cancelled -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(StarlightTheme.shapes.summaryCard)
                    .background(colors.emptyBackground)
                    .padding(14.dp)
            ) {
                Text(text = "검색을 취소했습니다.", style = StarlightTheme.typography.settingRowSubtitle, color = colors.textMuted)
            }
        }

        GameScanState.Idle -> Unit
    }
}

@Composable
private fun RemoveFolderConfirmDialog(
    folder: GameSourceFolder,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val colors = StarlightTheme.colors

    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(StarlightTheme.shapes.settingsPanel)
                .background(colors.surface)
                .padding(22.dp)
        ) {
            Text(text = "폴더 연결을 해제할까요?", style = StarlightTheme.typography.settingsTitle, color = colors.textPrimary)
            Text(
                text = "\"${folder.displayName}\" 폴더 연결을 해제하면 이 폴더에서 찾은 게임 목록과 " +
                    "즐겨찾기·플레이 기록도 함께 사라질 수 있습니다.",
                style = StarlightTheme.typography.settingRowSubtitle,
                color = colors.textMuted,
                modifier = Modifier.padding(top = 10.dp, bottom = 18.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CardActionButton(text = "취소", onClick = onCancel, modifier = Modifier.weight(1f))
                CardActionButton(
                    text = "연결 해제",
                    onClick = onConfirm,
                    emphasis = CardActionEmphasis.PRIMARY,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
