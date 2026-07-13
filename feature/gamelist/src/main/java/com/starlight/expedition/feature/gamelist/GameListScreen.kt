package com.starlight.expedition.feature.gamelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.data.image.LocalCoverImageLoader
import com.starlight.expedition.core.designsystem.component.EmptyResultState
import com.starlight.expedition.core.designsystem.component.FavoriteTrailingButton
import com.starlight.expedition.core.designsystem.component.GameCoverImage
import com.starlight.expedition.core.designsystem.component.GameListRow
import com.starlight.expedition.core.designsystem.component.GameSearchField
import com.starlight.expedition.core.designsystem.component.GenreChip
import com.starlight.expedition.core.designsystem.component.PageHeading
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Platform

@Composable
fun GameListScreen(
    uiState: GameListUiState,
    imageLoader: LocalCoverImageLoader,
    onQueryChange: (String) -> Unit,
    onPlatformSelected: (Platform?) -> Unit,
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
            placeholder = "게임 이름, 파일명으로 검색",
            modifier = Modifier.padding(bottom = 11.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GenreChip(
                    label = "전체",
                    isSelected = uiState.selectedPlatform == null,
                    onClick = { onPlatformSelected(null) }
                )
            }
            items(items = uiState.availablePlatforms) { platform ->
                GenreChip(
                    label = platform.displayName,
                    isSelected = uiState.selectedPlatform == platform,
                    onClick = { onPlatformSelected(platform) }
                )
            }
        }

        if (uiState.games.isEmpty() && !uiState.loading) {
            EmptyResultState(
                message = if (uiState.availablePlatforms.isEmpty()) {
                    "등록된 게임이 없습니다. 상단의 폴더 아이콘에서 게임 폴더를 추가해 주세요."
                } else {
                    "검색 결과가 없습니다."
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = spacing.scrollListContentBottomPadding),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                items(items = uiState.games, key = Game::id) { game ->
                    GameListRow(
                        titleKo = game.titleKo,
                        subtitle = game.fileName,
                        leading = {
                            GameCoverImage(
                                coverUri = game.coverUri,
                                platform = game.platform,
                                imageLoader = imageLoader,
                                contentDescription = "${game.titleKo} 커버",
                                modifier = Modifier.size(60.dp)
                            )
                        }
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
