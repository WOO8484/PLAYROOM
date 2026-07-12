package com.playroom.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.data.isLaunchable
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomEmptyState
import com.playroom.core.designsystem.component.PlayroomGameCard
import com.playroom.core.designsystem.component.PlayroomGameDetailDialog
import com.playroom.core.designsystem.component.coverImageResFor
import com.playroom.core.designsystem.component.playroomContentWidth
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.model.Game
import kotlinx.coroutines.launch

/** 검색 overlay: reached from the top bar's search button, closes on back press. */
@Composable
fun SearchRoute(
    onNavigateToPlayer: (gameId: Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalPlayroomSnackbarHostState.current
    val scope = rememberCoroutineScope()

    BackHandler(onBack = onClose)

    SearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::updateQuery,
        onClose = onClose,
        onOpenDetail = viewModel::openDetail,
        onCloseDetail = viewModel::closeDetail,
        onToggleFavorite = viewModel::toggleFavorite,
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
private fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    onCloseDetail: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onPlayGame: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular
    val cardGap = if (compact) PlayroomSpacing.CardGapCompact else PlayroomSpacing.CardGapRegular
    val fullSpan: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxLineSpan) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .playroomContentWidth(),
            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = PlayroomSpacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(cardGap),
            verticalArrangement = Arrangement.spacedBy(cardGap),
        ) {
            item(span = fullSpan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
                ) {
                    OutlinedTextField(
                        value = uiState.query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        placeholder = { Text("게임 제목이나 기종 검색") },
                        singleLine = true,
                        shape = RoundedCornerShape(PlayroomCorner.Medium),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = palette.surface,
                            unfocusedContainerColor = palette.surface,
                        ),
                    )
                    IconButton(
                        onClick = { keyboardController?.hide(); onClose() },
                        modifier = Modifier
                            .size(PlayroomSpacing.MinTouchTarget)
                            .background(palette.surface, RoundedCornerShape(PlayroomCorner.Medium)),
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "검색 닫기", tint = palette.textPrimary)
                    }
                }
            }

            if (uiState.results.isEmpty()) {
                item(span = fullSpan) {
                    PlayroomEmptyState(message = "검색 결과가 없습니다.")
                }
            } else {
                items(uiState.results, key = { it.id }) { game ->
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
    }

    uiState.detailGameId?.let { gameId ->
        val game = uiState.results.find { it.id == gameId }
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
