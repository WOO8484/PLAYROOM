package com.starlight.expedition.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.component.CardActionButton
import com.starlight.expedition.core.designsystem.component.CardActionEmphasis
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.FolderPermissionState
import com.starlight.expedition.core.model.GameSourceFolder

private fun permissionStateLabel(state: FolderPermissionState): String = when (state) {
    FolderPermissionState.GRANTED -> "권한 정상"
    FolderPermissionState.LOST -> "권한 다시 연결 필요"
    FolderPermissionState.UNKNOWN -> "권한 확인 중"
}

private fun lastScannedLabel(epochMillis: Long?): String {
    if (epochMillis == null) return "아직 검색하지 않음"
    val minutesAgo = (System.currentTimeMillis() - epochMillis) / 60_000
    return when {
        minutesAgo < 1 -> "방금 검색함"
        minutesAgo < 60 -> "${minutesAgo}분 전 검색"
        minutesAgo < 60 * 24 -> "${minutesAgo / 60}시간 전 검색"
        else -> "${minutesAgo / (60 * 24)}일 전 검색"
    }
}

@Composable
fun GameFolderManagementRow(
    folder: GameSourceFolder,
    scanInProgress: Boolean,
    onRescan: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(StarlightTheme.shapes.gameRow)
            .background(colors.surface.copy(alpha = 0.88f))
            .border(1.dp, colors.line, StarlightTheme.shapes.gameRow)
            .padding(15.dp)
    ) {
        Text(
            text = folder.displayName,
            style = StarlightTheme.typography.gameRowTitle,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "게임 ${folder.lastKnownGameCount}개 · ${lastScannedLabel(folder.lastScannedAtEpochMillis)}",
            style = StarlightTheme.typography.gameRowSubtitle,
            color = colors.textMuted,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = permissionStateLabel(folder.permissionState),
            style = StarlightTheme.typography.gameRowSubtitle,
            color = if (folder.permissionState == FolderPermissionState.LOST) colors.favoriteActive else colors.textMuted,
            modifier = Modifier.padding(top = 2.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardActionButton(
                text = if (scanInProgress) "검색 중…" else "다시 검색",
                onClick = { if (!scanInProgress) onRescan() },
                emphasis = CardActionEmphasis.PRIMARY,
                modifier = Modifier.weight(1f)
            )
            CardActionButton(
                text = "연결 해제",
                onClick = onDisconnect,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
