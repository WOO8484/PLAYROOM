package com.playroom.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.data.isLaunchable
import com.playroom.core.designsystem.R
import com.playroom.core.designsystem.component.CountBadge
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomEmptyState
import com.playroom.core.designsystem.component.PlayroomFilterChip
import com.playroom.core.designsystem.component.PlayroomGameCard
import com.playroom.core.designsystem.component.PlayroomGameDetailDialog
import com.playroom.core.designsystem.component.PlayroomLibraryBanner
import com.playroom.core.designsystem.component.PlayroomSecondaryButton
import com.playroom.core.designsystem.component.coverImageResFor
import com.playroom.core.designsystem.component.playroomContentWidth
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.model.Game
import com.playroom.core.model.GameSystem
import com.playroom.core.model.LibraryFilter
import kotlinx.coroutines.launch

@Composable
fun LibraryRoute(
    onNavigateToPlayer: (gameId: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalPlayroomSnackbarHostState.current
    val scope = rememberCoroutineScope()

    LibraryScreen(
        uiState = uiState,
        contentPadding = contentPadding,
        onSelectFilter = viewModel::selectFilter,
        onOpenDetail = viewModel::openDetail,
        onCloseDetail = viewModel::closeDetail,
        onToggleFavorite = viewModel::toggleFavorite,
        onOpenImportDialog = viewModel::openImportDialog,
        onCloseImportDialog = viewModel::closeImportDialog,
        onStartScan = {
            viewModel.startFakeScan { found ->
                scope.launch { snackbarHostState.showSnackbar("게임 ${found}개를 찾았습니다") }
            }
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
private fun LibraryScreen(
    uiState: LibraryUiState,
    contentPadding: PaddingValues,
    onSelectFilter: (LibraryFilter) -> Unit,
    onOpenDetail: (Int) -> Unit,
    onCloseDetail: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onOpenImportDialog: () -> Unit,
    onCloseImportDialog: () -> Unit,
    onStartScan: () -> Unit,
    onPlayGame: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular
    val cardGap = if (compact) PlayroomSpacing.CardGapCompact else PlayroomSpacing.CardGapRegular
    val fullSpan: androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxLineSpan) }

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
                    Text(text = "게임룸", style = PlayroomType.SectionTitle, color = palette.textPrimary)
                    Text(
                        text = "표지를 누르거나 게임 소개를 확인하세요",
                        style = PlayroomType.SectionSubtitle,
                        color = palette.textSecondary,
                    )
                }
                CountBadge(text = "게임 ${uiState.totalCount}개")
            }
        }

        item(span = fullSpan) {
            PlayroomLibraryBanner(
                eyebrow = "MY LIBRARY",
                title = "내 게임을 한눈에",
                description = "기종별 분류와 한글 게임 소개를 함께 제공합니다.",
                imageRes = R.drawable.card_art_starlight,
                modifier = Modifier.padding(top = PlayroomSpacing.Medium),
            )
        }

        item(span = fullSpan) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PlayroomSpacing.Medium, bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small)) {
                    PlayroomFilterChip("전체", uiState.filter == LibraryFilter.All, { onSelectFilter(LibraryFilter.All) })
                    PlayroomFilterChip("즐겨찾기", uiState.filter == LibraryFilter.Favorites, { onSelectFilter(LibraryFilter.Favorites) })
                }
                PlayroomSecondaryButton(text = "폴더 관리", onClick = onOpenImportDialog)
            }
        }

        item(span = fullSpan) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = PlayroomSpacing.Small),
                horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
            ) {
                listOf(GameSystem.GBA, GameSystem.SNES, GameSystem.PS1, GameSystem.MEGA_DRIVE).forEach { system ->
                    PlayroomFilterChip(
                        label = system.label,
                        selected = uiState.filter == LibraryFilter.BySystem(system),
                        onClick = { onSelectFilter(LibraryFilter.BySystem(system)) },
                    )
                }
            }
        }

        if (uiState.games.isEmpty()) {
            item(span = fullSpan) {
                PlayroomEmptyState(message = "조건에 맞는 게임이 없습니다.")
            }
        } else {
            items(uiState.games, key = { it.id }) { game ->
                PlayroomGameCard(
                    game = game,
                    onInfoClick = { onOpenDetail(game.id) },
                    onPlayClick = { onPlayGame(game) },
                    onFavoriteToggle = { onToggleFavorite(game.id) },
                    coverImageRes = coverImageResFor(game.id),
                )
            }
        }
    }

    uiState.detailGameId?.let { gameId ->
        val game = uiState.games.find { it.id == gameId }
        if (game != null) {
            PlayroomGameDetailDialog(
                game = game,
                onDismiss = onCloseDetail,
                onPlayClick = { onCloseDetail(); onPlayGame(game) },
                coverImageRes = coverImageResFor(game.id),
            )
        }
    }

    if (uiState.isImportDialogOpen) {
        ImportGameFolderDialog(
            isScanning = uiState.isScanning,
            onDismiss = onCloseImportDialog,
            onStartScan = onStartScan,
        )
    }
}
