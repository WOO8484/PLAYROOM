package com.starlight.expedition.feature.gamelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.component.EmptyResultState
import com.starlight.expedition.core.designsystem.component.FavoriteTrailingButton
import com.starlight.expedition.core.designsystem.component.GameListRow
import com.starlight.expedition.core.designsystem.component.GameSearchField
import com.starlight.expedition.core.designsystem.component.GenreChip
import com.starlight.expedition.core.designsystem.component.PageHeading
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre

/** 게임리스트 화면 전용 부제입니다. 확정 GUI 문구를 게임별로 그대로 옮깁니다. */
private val gameListTagById: Map<String, String> = mapOf(
    "starlight-odyssey" to "모험",
    "hero-legend" to "판타지",
    "light-puzzle" to "두뇌",
    "galaxy-rescue" to "슈팅",
    "dragon-tower" to "방치형",
    "moonlight-run" to "러너"
)

@Composable
fun GameListScreen(
    uiState: GameListUiState,
    onQueryChange: (String) -> Unit,
    onGenreSelected: (GameGenre?) -> Unit,
    onToggleFavorite: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = StarlightTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = spacing.screenHorizontal)
            .padding(top = 6.dp)
    ) {
        PageHeading(title = "게임리스트", description = "원하는 게임을 빠르게 찾아보세요.")

        GameSearchField(
            value = uiState.query,
            onValueChange = onQueryChange,
            placeholder = "게임 이름 검색",
            modifier = Modifier.padding(bottom = 11.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GenreChip(
                    label = "전체",
                    isSelected = uiState.selectedGenre == null,
                    onClick = { onGenreSelected(null) }
                )
            }
            items(items = GameGenre.entries) { genre ->
                GenreChip(
                    label = genre.displayName,
                    isSelected = uiState.selectedGenre == genre,
                    onClick = { onGenreSelected(genre) }
                )
            }
        }

        if (uiState.games.isEmpty() && !uiState.loading) {
            EmptyResultState(message = "검색 결과가 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = spacing.scrollListContentBottomPadding),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                items(items = uiState.games, key = Game::id) { game ->
                    GameListRow(
                        titleKo = game.titleKo,
                        subtitle = "${game.genre.displayName} · ${gameListTagById[game.id] ?: game.genre.displayName}"
                    ) {
                        FavoriteTrailingButton(
                            isFavorite = game.isFavorite,
                            titleKo = game.titleKo,
                            onClick = { onToggleFavorite(game.id, !game.isFavorite) }
                        )
                    }
                }
            }
        }
    }
}
