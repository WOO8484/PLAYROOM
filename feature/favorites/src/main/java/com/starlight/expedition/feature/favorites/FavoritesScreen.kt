package com.starlight.expedition.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.component.EmptyResultState
import com.starlight.expedition.core.designsystem.component.FavoriteTrailingButton
import com.starlight.expedition.core.designsystem.component.GameListRow
import com.starlight.expedition.core.designsystem.component.PageHeading
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.designsystem.theme.contentBottomSafePadding
import com.starlight.expedition.core.model.Game

/** 즐겨찾기 화면 전용 상태 문구입니다. 확정 GUI 문구를 게임별로 그대로 옮깁니다. */
private val favoriteStatusById: Map<String, String> = mapOf(
    "starlight-odyssey" to "모험 RPG · 최근 실행",
    "hero-legend" to "판타지 RPG · 저장됨",
    "galaxy-rescue" to "슈팅 · 최고 점수 8,420",
    "light-puzzle" to "퍼즐 · 18단계",
    "dragon-tower" to "방치형 · 보상 대기 중"
)

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onToggleFavorite: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val spacing = StarlightTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.appBackground)
            .padding(horizontal = spacing.screenHorizontal)
            .padding(top = 6.dp)
    ) {
        PageHeading(title = "즐겨찾기", description = "자주 하는 게임만 모아봤어요.")

        if (uiState.favorites.isEmpty() && !uiState.loading) {
            EmptyResultState(message = "즐겨찾기한 게임이 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = spacing.contentBottomSafePadding()),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                items(items = uiState.favorites, key = Game::id) { game ->
                    GameListRow(
                        titleKo = game.titleKo,
                        subtitle = favoriteStatusById[game.id] ?: game.genre.displayName
                    ) {
                        FavoriteTrailingButton(
                            isFavorite = true,
                            titleKo = game.titleKo,
                            onClick = { onToggleFavorite(game.id, false) }
                        )
                    }
                }
            }
        }
    }
}
