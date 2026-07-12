package com.starlight.expedition.feature.quickstart

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.starlight.expedition.core.common.TimeFormat
import com.starlight.expedition.core.designsystem.StarlightCoverArt
import com.starlight.expedition.core.designsystem.component.CardActionButton
import com.starlight.expedition.core.designsystem.component.CardActionEmphasis
import com.starlight.expedition.core.designsystem.component.CoverFrame
import com.starlight.expedition.core.designsystem.component.MainActionButton
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Recommendation
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

/** 실제 표지 이미지가 없는 추천 게임에 색조 차이를 주어 표지가 바뀐 느낌을 전달합니다. */
private val recommendationTintByGameId: Map<String, Color> = mapOf(
    "hero-legend" to Color(0xFF9076FF),
    "galaxy-rescue" to Color(0xFFFFA65C),
    "light-puzzle" to Color(0xFF6DD6C6),
    "dragon-tower" to Color(0xFF7BD97A)
)

@Composable
fun QuickStartScreen(
    uiState: QuickStartUiState,
    onRandomRecommendation: () -> Unit,
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
        val continueGame = uiState.continueGame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.30f)
        ) {
            if (continueGame != null) {
                ContinueCard(game = continueGame, modifier = Modifier.fillMaxSize())
            }
        }

        val recommendation = uiState.recommendation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.90f)
        ) {
            if (recommendation != null) {
                RecommendedCard(
                    recommendation = recommendation,
                    onRandomRecommendation = onRandomRecommendation,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ContinueCard(
    game: Game,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val lifecycleOwner = LocalLifecycleOwner.current
    var coverIndex by remember { mutableIntStateOf(0) }

    // 이 카드가 조합(composition)에 남아있고, 앱이 최소 STARTED 상태일 때만 크로스페이드를 진행합니다.
    // 다른 화면으로 이동하면 이 Composable 자체가 조합에서 사라지며 자동으로 멈춥니다.
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                delay(3_500)
                coverIndex = (coverIndex + 1) % 2
            }
        }
    }

    Box(
        modifier = modifier
            .clip(StarlightTheme.shapes.featureCard)
            .background(
                Brush.linearGradient(
                    listOf(colors.cardGradientStart, colors.cardGradientMid, colors.cardGradientEnd)
                )
            )
            .border(1.dp, colors.cardBorder, StarlightTheme.shapes.featureCard)
    ) {
        Crossfade(
            targetState = coverIndex,
            animationSpec = tween(600),
            modifier = Modifier.fillMaxSize()
        ) { index ->
            CoverFrame(
                painter = painterResource(
                    if (index == 0) StarlightCoverArt.starlight else StarlightCoverArt.heroLegend
                ),
                contentDescription = if (index == 0) "별빛 모험대 게임 커버" else "용사의 전설 게임 커버",
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
            val playStatusText = if (game.lastPlayedAt != null) {
                "마지막 플레이 ${TimeFormat.relativeKorean(game.lastPlayedAt, now)} · ${game.progressLabel ?: "저장됨"}"
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

            // 실제 게임 실행은 1차 개발 범위에 포함되지 않아 동작을 연결하지 않습니다.
            MainActionButton(text = "이어서 하기", onClick = {})
        }
    }
}

@Composable
private fun RecommendedCard(
    recommendation: Recommendation,
    onRandomRecommendation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val game = recommendation.game
    val tintColor = recommendationTintByGameId[game.id]
    val colorFilter = tintColor?.let { ColorFilter.tint(it, BlendMode.Hue) }

    Box(
        modifier = modifier
            .clip(StarlightTheme.shapes.featureCard)
            .background(
                Brush.linearGradient(
                    listOf(colors.cardGradientStart, colors.cardGradientMid, colors.cardGradientEnd)
                )
            )
            .border(1.dp, colors.cardBorder, StarlightTheme.shapes.featureCard)
    ) {
        CoverFrame(
            painter = painterResource(StarlightCoverArt.heroLegend),
            contentDescription = "${game.titleKo} 게임 커버",
            colorFilter = colorFilter,
            modifier = Modifier.fillMaxSize()
        )

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
            // 게임 소개·바로 시작은 상세/실행 화면이 1차 개발 범위 밖이라 동작을 연결하지 않습니다.
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
