package com.playroom.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playroom.core.designsystem.component.PlayroomDialog
import com.playroom.core.designsystem.component.PlayroomPrimaryButton
import com.playroom.core.designsystem.component.PlayroomSecondaryButton
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType

/** 게임 폴더 추가 popup — fully fake: no file picker, no permissions, just a timed "scan". */
@Composable
fun ImportGameFolderDialog(
    isScanning: Boolean,
    onDismiss: () -> Unit,
    onStartScan: () -> Unit,
) {
    val palette = PlayroomTheme.palette

    PlayroomDialog(onDismissRequest = onDismiss) {
        Text(text = "게임 폴더 추가", style = PlayroomType.SectionTitle, color = palette.textPrimary)
        Text(
            text = "보유한 게임 파일이 있는 폴더를 선택하면 자동으로 검색하고 게임기별로 정리합니다.",
            style = PlayroomType.BodyMedium,
            color = palette.textSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = PlayroomSpacing.Large),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(palette.surfaceSoft, RoundedCornerShape(PlayroomCorner.Medium))
                .padding(PlayroomSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "\uD83D\uDCC1", fontSize = 42.sp)
            Text(
                text = "/내 파일/게임룸",
                style = PlayroomType.CardTitle,
                color = palette.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = PlayroomSpacing.Small),
            )
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = PlayroomSpacing.Medium),
                    color = palette.primary,
                )
                Text(
                    text = "검색 중...",
                    style = PlayroomType.Caption,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(top = PlayroomSpacing.Small),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Large),
            horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
        ) {
            PlayroomSecondaryButton(text = "취소", onClick = onDismiss, modifier = Modifier.weight(1f))
            PlayroomPrimaryButton(
                text = if (isScanning) "검색 중..." else "폴더 선택",
                onClick = onStartScan,
                enabled = !isScanning,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
