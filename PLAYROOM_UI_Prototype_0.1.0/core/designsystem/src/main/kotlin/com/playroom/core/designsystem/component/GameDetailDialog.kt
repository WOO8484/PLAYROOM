package com.playroom.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.model.Game

/**
 * 게임 상세정보 popup shared by home / 게임룸 / 즐겨찾기 / 검색. Each feature only
 * owns *when* this is shown (its own dialog-open state) — the layout itself
 * lives here once, per the work order's "공통 팝업 UI는 :core:designsystem" rule.
 */
@Composable
fun PlayroomGameDetailDialog(
    game: Game,
    onDismiss: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes coverImageRes: Int? = null,
) {
    val palette = PlayroomTheme.palette

    PlayroomDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(PlayroomCorner.Large)),
        ) {
            if (coverImageRes != null) {
                Image(
                    painter = painterResource(coverImageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(palette.cardGradient)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsEsports,
                        contentDescription = null,
                        tint = palette.primary.copy(alpha = 0.5f),
                        modifier = Modifier.padding(PlayroomSpacing.Huge),
                    )
                }
            }
        }

        Text(
            text = "${game.system.label} · ${game.runStatus.label}",
            style = PlayroomType.Eyebrow,
            color = palette.primaryDark,
            modifier = Modifier.padding(top = PlayroomSpacing.Large),
        )
        Text(
            text = game.title,
            style = PlayroomType.SectionTitle,
            color = palette.textPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
        )
        Text(text = game.summary, style = PlayroomType.BodyMedium, color = palette.textSecondary)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Large),
            horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
        ) {
            DetailStat(label = "핵심 재미", value = game.coreFun, modifier = Modifier.weight(1f))
            DetailStat(label = "난이도", value = game.difficulty.label, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Small),
            horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
        ) {
            DetailStat(label = "예상 플레이", value = game.estimatedPlayTime, modifier = Modifier.weight(1f))
            DetailStat(label = "추천 대상", value = game.recommendedFor, modifier = Modifier.weight(1f))
        }

        DetailSection(title = "게임 소개", body = game.description)
        DetailSection(title = "초보자 팁", body = game.beginnerTip)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Large),
            horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
        ) {
            PlayroomSecondaryButton(text = "닫기", onClick = onDismiss, modifier = Modifier.weight(1f))
            PlayroomPrimaryButton(text = "▶ 게임 시작", onClick = onPlayClick, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DetailStat(label: String, value: String, modifier: Modifier = Modifier) {
    val palette = PlayroomTheme.palette
    Column(
        modifier = modifier
            .background(palette.surfaceSoft, RoundedCornerShape(PlayroomCorner.Small))
            .padding(horizontal = PlayroomSpacing.Small, vertical = PlayroomSpacing.Small),
    ) {
        Text(text = label, style = PlayroomType.Caption, color = palette.textSecondary)
        Text(
            text = value,
            style = PlayroomType.CardTitle,
            color = palette.textPrimary,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
private fun DetailSection(title: String, body: String) {
    val palette = PlayroomTheme.palette
    Column(modifier = Modifier.padding(top = PlayroomSpacing.Large)) {
        Text(text = title, style = PlayroomType.CardTitle, color = palette.textPrimary)
        Text(
            text = body,
            style = PlayroomType.BodyMedium,
            color = palette.textSecondary,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
