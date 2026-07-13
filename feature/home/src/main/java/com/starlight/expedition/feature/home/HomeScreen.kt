package com.starlight.expedition.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.common.TimeFormat
import com.starlight.expedition.core.designsystem.component.GameListRow
import com.starlight.expedition.core.designsystem.component.PageHeading
import com.starlight.expedition.core.designsystem.component.PlayTrailingButton
import com.starlight.expedition.core.designsystem.component.SummaryCard
import com.starlight.expedition.core.designsystem.component.scaleClickable
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Game
import kotlinx.datetime.Clock

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val spacing = StarlightTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = spacing.screenHorizontal)
            .padding(top = 6.dp, bottom = spacing.navSafeHeight)
    ) {
        PageHeading(title = "홈", description = "오늘의 플레이를 한눈에 확인하세요.")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                value = uiState.recentGames.size.toString(),
                label = "최근 게임",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                value = uiState.favoriteCount.toString(),
                label = "즐겨찾기",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                value = uiState.todayPlayMinutesLabel,
                label = "오늘 플레이",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "최근 플레이", style = StarlightTheme.typography.sectionTitle, color = colors.textPrimary)
            Text(
                text = "전체 보기",
                style = StarlightTheme.typography.textButton,
                color = colors.primary,
                modifier = Modifier
                    .scaleClickable(onClick = onViewAllClick)
                    .padding(7.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            uiState.recentGames.forEachIndexed { index, game ->
                RecentGameRow(game = game, modifier = Modifier.fillMaxWidth().weight(1f))
                if (index != uiState.recentGames.lastIndex) {
                    Spacer(modifier = Modifier.height(11.dp))
                }
            }
        }
    }
}

@Composable
private fun RecentGameRow(game: Game, modifier: Modifier = Modifier) {
    val now = remember { Clock.System.now() }
    val lastPlayedAt = game.lastPlayedAt
    val subtitle = if (lastPlayedAt != null) {
        "${TimeFormat.relativeKorean(lastPlayedAt, now)} · ${game.progressLabel ?: "저장됨"}"
    } else {
        "아직 플레이 기록이 없습니다"
    }

    GameListRow(
        titleKo = game.titleKo,
        subtitle = subtitle,
        modifier = modifier
    ) {
        // 실제 게임 실행은 1차 개발 범위 밖이라 동작을 연결하지 않습니다.
        PlayTrailingButton(contentDescription = "${game.titleKo} 이어서 하기", onClick = {})
    }
}
