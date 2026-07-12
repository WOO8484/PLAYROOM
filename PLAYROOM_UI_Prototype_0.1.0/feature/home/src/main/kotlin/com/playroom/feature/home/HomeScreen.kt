package com.playroom.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.designsystem.component.PlayroomActionButton
import com.playroom.core.designsystem.component.PlayroomGameDetailDialog
import com.playroom.core.designsystem.component.PlayroomImageFrame
import com.playroom.core.designsystem.component.PlayroomPrimaryButton
import com.playroom.core.designsystem.component.coverImageResFor
import com.playroom.core.designsystem.component.playroomContentWidth
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.model.Game

/**
 * 메인 화면. Wires [HomeViewModel] to the two hero cards, the 4-button
 * action row, the shared game-detail popup and the 추천 옵션 popup.
 */
@Composable
fun HomeRoute(
    onNavigateToPlayer: (gameId: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        contentPadding = contentPadding,
        onContinuePlaying = { uiState.continuePlayingGame?.let { onNavigateToPlayer(it.id) } },
        onOpenDetail = viewModel::openDetail,
        onCloseDetail = viewModel::closeDetail,
        onPlayFromDetail = { gameId -> viewModel.closeDetail(); onNavigateToPlayer(gameId) },
        onRandomRecommend = viewModel::pickNewRecommendation,
        onOpenOptions = viewModel::openOptionsDialog,
        onCloseOptions = viewModel::closeOptionsDialog,
        onUpdateOptions = viewModel::updateRecommendationOptions,
        onApplyOptions = viewModel::applyOptionsAndRecommend,
        onPlayRecommended = { uiState.recommendedGame?.let { onNavigateToPlayer(it.id) } },
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    contentPadding: PaddingValues,
    onContinuePlaying: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    onCloseDetail: () -> Unit,
    onPlayFromDetail: (Int) -> Unit,
    onRandomRecommend: () -> Unit,
    onOpenOptions: () -> Unit,
    onCloseOptions: () -> Unit,
    onUpdateOptions: (com.playroom.core.model.RecommendationOptions) -> Unit,
    onApplyOptions: () -> Unit,
    onPlayRecommended: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular
    val cardGap = if (compact) PlayroomSpacing.CardGapCompact else PlayroomSpacing.CardGapRegular

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = horizontalPadding)
            .playroomContentWidth(),
        verticalArrangement = Arrangement.spacedBy(cardGap),
    ) {
        uiState.continuePlayingGame?.let { game ->
            HeroCard(game = game, compact = compact, onContinueClick = onContinuePlaying)
        }
        uiState.recommendedGame?.let { game ->
            RecommendCard(
                game = game,
                compact = compact,
                onInfoClick = { onOpenDetail(game.id) },
                onPlayClick = onPlayRecommended,
                onRandomClick = onRandomRecommend,
                onOptionsClick = onOpenOptions,
            )
        }
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.heightIn(min = PlayroomSpacing.Medium))
    }

    uiState.detailGameId?.let { gameId ->
        val game = listOfNotNull(uiState.continuePlayingGame, uiState.recommendedGame).find { it.id == gameId }
        if (game != null) {
            PlayroomGameDetailDialog(
                game = game,
                onDismiss = onCloseDetail,
                onPlayClick = { onPlayFromDetail(game.id) },
                coverImageRes = coverImageResFor(game.id),
            )
        }
    }

    if (uiState.isOptionsDialogOpen) {
        RecommendationOptionsDialog(
            options = uiState.recommendationOptions,
            onOptionsChange = onUpdateOptions,
            onDismiss = onCloseOptions,
            onApply = onApplyOptions,
        )
    }
}

@Composable
private fun HeroCard(game: Game, compact: Boolean, onContinueClick: () -> Unit) {
    val palette = PlayroomTheme.palette
    val minHeight = if (compact) 306.dp else 318.dp
    val coverRes = coverImageResFor(game.id)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clip(RoundedCornerShape(PlayroomCorner.Large))
            .background(Brush.linearGradient(palette.cardGradient)),
    ) {
        if (coverRes != null) {
            PlayroomImageFrame(
                imageRes = coverRes,
                rotationDegrees = -7f,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 12.dp)
                    .fillMaxWidth(0.53f)
                    .fillMaxHeight(0.85f),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.58f)
                .fillMaxHeight()
                .padding(PlayroomSpacing.ExtraLarge),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(text = "CONTINUE PLAYING", style = PlayroomType.Eyebrow, color = palette.primaryDark)
            Text(
                text = game.title,
                style = if (compact) PlayroomType.HeroTitleCompact else PlayroomType.HeroTitle,
                color = palette.textPrimary,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "마지막 플레이 ${game.lastPlayedLabel} · 자동 저장 완료",
                style = PlayroomType.CardMeta,
                color = palette.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = PlayroomSpacing.Medium),
            )
            PlayroomPrimaryButton(text = "▶ 이어서 하기", onClick = onContinueClick)
        }
    }
}

@Composable
private fun RecommendCard(
    game: Game,
    compact: Boolean,
    onInfoClick: () -> Unit,
    onPlayClick: () -> Unit,
    onRandomClick: () -> Unit,
    onOptionsClick: () -> Unit,
) {
    val palette = PlayroomTheme.palette
    val minHeight = if (compact) 273.dp else 287.dp
    val coverRes = coverImageResFor(game.id)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clip(RoundedCornerShape(PlayroomCorner.Large))
            .background(Brush.linearGradient(palette.cardGradient)),
    ) {
        if (coverRes != null) {
            PlayroomImageFrame(
                imageRes = coverRes,
                rotationDegrees = -7f,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = 10.dp)
                    .fillMaxWidth(0.51f)
                    .fillMaxHeight(0.86f),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.63f)
                .fillMaxHeight()
                .padding(PlayroomSpacing.Large),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(text = "오늘 뭐 하지?", style = PlayroomType.Eyebrow, color = palette.primaryDark)
            Text(
                text = game.title,
                style = if (compact) PlayroomType.RecommendTitleCompact else PlayroomType.RecommendTitle,
                color = palette.textPrimary,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = game.summary,
                style = PlayroomType.CardDescription,
                color = palette.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = PlayroomSpacing.Medium),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                PlayroomActionButton(text = "📖 게임 소개", onClick = onInfoClick)
                PlayroomActionButton(text = "▶ 바로 시작", onClick = onPlayClick, primary = true)
                PlayroomActionButton(text = "🔀 랜덤 추천", onClick = onRandomClick)
                PlayroomActionButton(text = "⚙ 옵션", onClick = onOptionsClick)
            }
        }
    }
}
