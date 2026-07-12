package com.playroom.feature.favorites

import com.playroom.core.model.Game

data class FavoritesUiState(
    val favoriteGames: List<Game> = emptyList(),
    val detailGameId: Int? = null,
)
