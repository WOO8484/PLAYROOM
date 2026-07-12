package com.starlight.expedition.feature.gamelist

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre

data class GameListUiState(
    val loading: Boolean = false,
    val games: List<Game> = emptyList(),
    val query: String = "",
    val selectedGenre: GameGenre? = null,
    val errorMessage: String? = null
)
