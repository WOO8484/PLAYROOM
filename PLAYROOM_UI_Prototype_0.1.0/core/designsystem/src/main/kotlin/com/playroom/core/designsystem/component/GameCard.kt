package com.playroom.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.model.Game

/**
 * The 2-column game card reused by 게임룸(library), 즐겨찾기(favorites) and
 * 검색(search). Tapping the cover opens the same detail popup as the
 * "게임 소개" button, matching the 게임룸 header hint text.
 */
@Composable
fun PlayroomGameCard(
    game: Game,
    onInfoClick: () -> Unit,
    onPlayClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes coverImageRes: Int? = null,
) {
    val palette = PlayroomTheme.palette

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PlayroomCorner.Large))
            .background(palette.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.05f)
                .clickable(onClick = onInfoClick),
        ) {
            if (coverImageRes != null) {
                Image(
                    painter = painterResource(coverImageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.28f)))),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(palette.cardGradient)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsEsports,
                        contentDescription = null,
                        tint = palette.primary.copy(alpha = 0.45f),
                        modifier = Modifier.size(44.dp),
                    )
                }
            }

            StatusBadge(
                status = game.runStatus,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(PlayroomSpacing.Small),
            )

            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(PlayroomSpacing.MinTouchTarget)
                    .background(palette.surface.copy(alpha = 0.75f), RoundedCornerShape(PlayroomCorner.Pill)),
            ) {
                Icon(
                    imageVector = if (game.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = if (game.isFavorite) "즐겨찾기 해제" else "즐겨찾기 추가",
                    tint = if (game.isFavorite) palette.primary else palette.textSecondary,
                )
            }
        }

        Column(modifier = Modifier.padding(PlayroomSpacing.Medium)) {
            Text(
                text = game.title,
                style = PlayroomType.CardTitle,
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier.padding(top = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
            ) {
                Text(text = game.system.label, style = PlayroomType.CardCaption, color = palette.textSecondary)
                Text(text = game.lastPlayedLabel, style = PlayroomType.CardCaption, color = palette.textSecondary)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PlayroomSpacing.Small),
                horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
            ) {
                PlayroomSecondaryButton(
                    text = "게임 소개",
                    onClick = onInfoClick,
                    modifier = Modifier.weight(1f),
                )
                PlayroomPrimaryButton(
                    text = "실행",
                    onClick = onPlayClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
