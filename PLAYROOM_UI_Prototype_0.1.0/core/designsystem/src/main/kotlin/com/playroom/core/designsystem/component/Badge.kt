package com.playroom.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.model.RunStatus
import com.playroom.core.model.RunStatusType

/** Small color-coded pill for a game's [RunStatus] ("바로 실행" / "BIOS 필요" / "파일 확인"). */
@Composable
fun StatusBadge(status: RunStatus, modifier: Modifier = Modifier) {
    val palette = com.playroom.core.designsystem.theme.PlayroomTheme.palette
    val color = when (status.type) {
        RunStatusType.READY -> palette.success
        RunStatusType.WARNING -> palette.warning
        RunStatusType.ERROR -> palette.danger
    }
    Text(
        text = status.label,
        style = PlayroomType.Badge,
        color = Color.White,
        modifier = modifier
            .background(color, RoundedCornerShape(PlayroomCorner.Pill))
            .padding(horizontal = 9.dp, vertical = 4.dp),
    )
}

/** Small neutral pill, e.g. "게임 8개" on the 게임룸 header. */
@Composable
fun CountBadge(text: String, modifier: Modifier = Modifier) {
    val palette = com.playroom.core.designsystem.theme.PlayroomTheme.palette
    Text(
        text = text,
        style = PlayroomType.Badge,
        color = palette.primaryDark,
        modifier = modifier
            .background(palette.chipSelected, RoundedCornerShape(PlayroomCorner.Pill))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
