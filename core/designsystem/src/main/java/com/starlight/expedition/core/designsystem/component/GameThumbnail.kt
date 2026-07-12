package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 게임 제목을 기준으로 목록 썸네일에 표시할 상징 이모지를 반환합니다.
 * 확정 GUI가 실제 표지 대신 이모지 아이콘을 사용하는 화면(홈/즐겨찾기/게임리스트)에 적용합니다.
 */
fun symbolicEmojiFor(titleKo: String): String = when (titleKo) {
    "별빛 모험대" -> "\uD83C\uDF0C"
    "용사의 전설" -> "\uD83C\uDFF0"
    "빛의 퍼즐" -> "\uD83E\uDDE9"
    "은하 구조대" -> "\uD83D\uDEF8"
    "드래곤 타워" -> "\uD83D\uDC09"
    "달빛 달리기" -> "\uD83C\uDF19"
    else -> "\uD83C\uDFAE"
}

@Composable
fun GameThumbnail(
    titleKo: String,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .clip(StarlightTheme.shapes.thumbnail)
            .background(
                Brush.linearGradient(
                    colors = listOf(colors.thumbnailGradientStart, colors.thumbnailGradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1f, 1f)
                )
            )
            .semantics { contentDescription = "$titleKo 아이콘" },
        contentAlignment = Alignment.Center
    ) {
        Text(text = symbolicEmojiFor(titleKo), fontSize = (size.value * 0.42f).sp)
    }
}
