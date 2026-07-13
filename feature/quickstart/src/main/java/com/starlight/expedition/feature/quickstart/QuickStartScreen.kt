package com.starlight.expedition.feature.quickstart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.common.TimeFormat
import com.starlight.expedition.core.data.image.LocalCoverImageLoader
import com.starlight.expedition.core.designsystem.component.CardActionButton
import com.starlight.expedition.core.designsystem.component.CardActionEmphasis
import com.starlight.expedition.core.designsystem.component.CoverFrame
import com.starlight.expedition.core.designsystem.component.GameCoverImage
import com.starlight.expedition.core.designsystem.component.MainActionButton
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Recommendation
import kotlinx.datetime.Clock

@Composable
fun QuickStartScreen(
    uiState: QuickStartUiState,
    imageLoader: LocalCoverImageLoader,
    onRandomRecommendation: () -> Unit,
    onAddFolderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = StarlightTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = spacing.screenHorizontal)
            .padding(top = 6.dp, bottom = spacing.navSafeHeight),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.30f)
        ) {
            val continueGame = uiState.continueGame
            when {
                continueGame != null -> ContinueCard(
                    game = continueGame,
                    imageLoader = imageLoader,
                    modifier = Modifier.fillMaxSize()
                )
                uiState.hasAnyGames -> EmptyFeatureCard(
                    label = "이어 하기",
                    title = "아직 플레이 기록이 없습니다",
                    description = "게임리스트에서 게임을 선택해 시작해 보세요.",
                    onAddFolderClick = null,
                    modifier = Modifier.fillMaxSize()
                )
                else -> EmptyFeatureCard(
                    label = "이어 하기",
                    title = "등록된 게임이 없습니다",
                    description = "상단의 폴더 아이콘에서 게임 폴더를 추가해 주세요.",
                    onAddFolderClick = onAddFolderClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.90f)
        ) {
            val recommendation = uiState.recommendation
            when {
                recommendation != null -> RecommendedCard(
                    recommendation = recommendation,
                    imageLoader = imageLoader,
                    onRandomRecommendation = onRandomRecommendation,
                    modifier = Modifier.fillMaxSize()
                )
                uiState.hasAnyGames -> EmptyFeatureCard(
                    label = "오늘 뭐 하지?",
                    title = "추천할 게임이 없습니다",
                    description = "게임리스트에서 게임을 찾아보세요.",
                    onAddFolderClick = null,
                    modifier = Modifier.fillMaxSize()
                )
                else -> EmptyFeatureCard(
                    label = "오늘 뭐 하지?",
                    title = "등록된 게임이 없습니다",
                    description = "상단의 폴더 아이콘에서 게임 폴더를 추가해 주세요.",
                    onAddFolderClick = onAddFolderClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun EmptyFeatureCard(
    label: String,
    title: String,
    description: String,
    onAddFolderClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors

    Column(
        modifier = modifier
            .clip(StarlightTheme.shapes.featureCard)
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, StarlightTheme.shapes.featureCard)
            .padding(horizontal = 26.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = label, style = StarlightTheme.typography.featureLabel, color = colors.primaryVariant)
        Text(
            text = title,
            style = StarlightTheme.typography.featureTitleSecondary,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
        )
        Text(text = description, style = StarlightTheme.typography.featureDescription, color = colors.textMuted)

        if (onAddFolderClick != null) {
            CardActionButton(
                text = "게임 폴더 추가",
                onClick = onAddFolderClick,
                emphasis = CardActionEmphasis.PRIMARY,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun ContinueCard(
    game: Game,
    imageLoader: LocalCoverImageLoader,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors

    Box(
        modifier = modifier
            .clip(StarlightTheme.shapes.featureCard)
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, StarlightTheme.shapes.featureCard)
    ) {
        CoverFrame(
            decorationBrush = Brush.linearGradient(
                listOf(colors.cardGradientStart, colors.cardGradientMid, colors.cardGradientEnd)
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            GameCoverImage(
                coverUri = game.coverUri,
                platform = game.platform,
                imageLoader = imageLoader,
                contentDescription = "${game.titleKo} 게임 커버",
                shape = RectangleShape,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.59f)
                .fillMaxSize()
                .padding(start = 26.dp, top = 24.dp, bottom = 21.dp)
        ) {
            Text(text = "이어 하기", style = StarlightTheme.typography.featureLabel, color = colors.primaryVariant)
            Text(
                text = game.titleKo,
                style = StarlightTheme.typography.featureTitle,
                color = colors.textPrimary,
                maxLines = 1,
                modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
            )
            Column {
                game.descriptionLines.forEach { line ->
                    Text(text = line, style = StarlightTheme.typography.featureDescription, color = colors.textMuted)
                }
            }

            val now = remember { Clock.System.now() }
            val lastPlayedAt = game.lastPlayedAt
            val playStatusText = if (lastPlayedAt != null) {
                "마지막 플레이 ${TimeFormat.relativeKorean(lastPlayedAt, now)} · ${game.progressLabel ?: "저장됨"}"
            } else {
                "아직 플레이 기록이 없습니다"
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(colors.textMuted)
                )
                Text(
                    text = playStatusText,
                    style = StarlightTheme.typography.playStatus,
                    color = colors.textMuted,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            Box(modifier = Modifier.weight(1f))

            // 실제 게임 실행은 이번 업데이트 범위에 포함되지 않아 동작을 연결하지 않습니다.
            MainActionButton(text = "이어서 하기", onClick = {})
        }
    }
}

@Composable
private fun RecommendedCard(
    recommendation: Recommendation,
    imageLoader: LocalCoverImageLoader,
    onRandomRecommendation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val game = recommendation.game

    Box(
        modifier = modifier
            .clip(StarlightTheme.shapes.featureCard)
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, StarlightTheme.shapes.featureCard)
    ) {
        CoverFrame(
            decorationBrush = Brush.linearGradient(
                listOf(colors.cardGradientStart, colors.cardGradientMid, colors.cardGradientEnd)
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            GameCoverImage(
                coverUri = game.coverUri,
                platform = game.platform,
                imageLoader = imageLoader,
                contentDescription = "${game.titleKo} 게임 커버",
                shape = RectangleShape,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.60f)
                .fillMaxSize()
                .padding(start = 26.dp, top = 17.dp, bottom = 70.dp)
        ) {
            Text(text = "오늘 뭐 하지?", style = StarlightTheme.typography.featureLabel, color = colors.primaryVariant)
            Text(
                text = game.titleKo,
                style = StarlightTheme.typography.featureTitleSecondary,
                color = colors.textPrimary,
                maxLines = 1,
                modifier = Modifier.padding(top = 9.dp, bottom = 5.dp)
            )
            Column {
                game.descriptionLines.forEach { line ->
                    Text(text = line, style = StarlightTheme.typography.featureDescription, color = colors.textMuted)
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 24.dp, end = 18.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // 게임 소개·바로 시작은 상세/실행 화면이 이번 업데이트 범위 밖이라 동작을 연결하지 않습니다.
            CardActionButton(
                text = "\u25A4 게임 소개",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            CardActionButton(
                text = "\u25B6 바로 시작",
                onClick = {},
                emphasis = CardActionEmphasis.PRIMARY,
                modifier = Modifier.weight(1f)
            )
            CardActionButton(
                text = "\u21BB 랜덤 선택",
                onClick = onRandomRecommendation,
                emphasis = CardActionEmphasis.RANDOM,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
