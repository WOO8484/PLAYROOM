package com.playroom.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.data.isLaunchable
import com.playroom.core.designsystem.R
import com.playroom.core.designsystem.component.CountBadge
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomEmptyState
import com.playroom.core.designsystem.component.PlayroomGameCard
import com.playroom.core.designsystem.component.PlayroomGameDetailDialog
import com.playroom.core.designsystem.component.PlayroomLibraryBanner
import com.playroom.core.designsystem.component.coverImageResFor
import com.playroom.core.designsystem.component.playroomContentWidth
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.model.Game
import kotlinx.coroutines.launch

@Composable
fun FavoritesRoute(
    onNavigateToPlayer: (gameId: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalPlayroomSnackbarHostState.current
    val scope = rememberCoroutineScope()

    FavoritesScreen(
        uiState = uiState,
        contentPadding = contentPadding,
        onOpenDetail = viewModel::openDetail,
        onCloseDetail = viewModel::closeDetail,
        onRemoveFavorite = { gameId ->
            viewModel.removeFavorite(gameId)
            scope.launch { snackbarHostState.showSnackbar("즐겨찾기에서 제거했습니다") }
        },
        onPlayGame = { game ->
            if (game.isLaunchable()) {
                onNavigateToPlayer(game.id)
            } else {
                scope.launch { snackbarHostState.showSnackbar("실행 전 파일 확인이 필요합니다") }
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun FavoritesScreen(
    uiState: FavoritesUiState,
    contentPadding: PaddingValues,
    onOpenDetail: (Int) -> Unit,
    onCloseDetail: () -> Unit,
    onRemoveFavorite: (Int) -> Unit,
    onPlayGame: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular
    val cardGap = if (compact) PlayroomSpacing.CardGapCompact else PlayroomSpacing.CardGapRegular
    val fullSpan: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxLineSpan) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .playroomContentWidth(),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = PlayroomSpacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(cardGap),
        verticalArrangement = Arrangement.spacedBy(cardGap),
    ) {
        item(span = fullSpan) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(text = "즐겨찾기", style = PlayroomType.SectionTitle, color = palette.textPrimary)
                    Text(
                        text = "자주 플레이하는 게임만 모았습니다",
                        style = PlayroomType.SectionSubtitle,
                        color = palette.textSecondary,
                    )
                }
                CountBadge(text = "${uiState.favoriteGames.size}개")
            }
        }

        item(span = fullSpan) {
            PlayroomLibraryBanner(
                eyebrow = "FAVORITES",
                title = "좋아하는 게임",
                description = "별표를 누르면 언제든 이 화면에 추가됩니다.",
                imageRes = R.drawable.card_art_hero,
                modifier = Modifier.padding(top = PlayroomSpacing.Medium, bottom = PlayroomSpacing.Small),
            )
        }

        if (uiState.favoriteGames.isEmpty()) {
            item(span = fullSpan) {
                PlayroomEmptyState(message = "즐겨찾기한 게임이 없습니다.")
            }
        } else {
            items(uiState.favoriteGames, key = { it.id }) { game ->
                PlayroomGameCard(
                    game = game,
                    onInfoClick = { onOpenDetail(game.id) },
                    onPlayClick = { onPlayGame(game) },
                    onFavoriteToggle = { onRemoveFavorite(game.id) },
                    coverImageRes = coverImageResFor(game.id),
                )
            }
        }
    }

    uiState.detailGameId?.let { gameId ->
        val game = uiState.favoriteGames.find { it.id == gameId }
        if (game != null) {
            PlayroomGameDetailDialog(
                game = game,
                onDismiss = onCloseDetail,
                onPlayClick = { onCloseDetail(); onPlayGame(game) },
                coverImageRes = coverImageResFor(game.id),
            )
        }
    }
}
