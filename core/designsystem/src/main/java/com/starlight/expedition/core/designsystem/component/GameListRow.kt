package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 홈 최근 플레이 / 즐겨찾기 / 게임리스트 화면이 공통으로 사용하는 게임 행입니다.
 * 오른쪽 끝 요소만 [trailing]으로 갈아 끼웁니다.
 */
@Composable
fun GameListRow(
    titleKo: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit
) {
    val colors = StarlightTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(StarlightTheme.shapes.gameRow)
            .background(colors.surface)
            .border(1.dp, colors.line, StarlightTheme.shapes.gameRow)
            .padding(horizontal = 15.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameThumbnail(titleKo = titleKo)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp)
        ) {
            Text(
                text = titleKo,
                style = StarlightTheme.typography.gameRowTitle,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = StarlightTheme.typography.gameRowSubtitle,
                color = colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        trailing()
    }
}

@Composable
fun PlayTrailingButton(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(StarlightTheme.shapes.playButton)
            .background(colors.primary)
            .scaleClickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = colors.onPrimary
        )
    }
}

@Composable
fun FavoriteTrailingButton(
    isFavorite: Boolean,
    titleKo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val description = if (isFavorite) "$titleKo 즐겨찾기 해제" else "$titleKo 즐겨찾기 등록"
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(StarlightTheme.shapes.favoriteButton)
            .scaleClickable(onClick = onClick)
            .semantics { this.contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
            contentDescription = null,
            tint = if (isFavorite) colors.favoriteActive else colors.favoriteInactive
        )
    }
}
