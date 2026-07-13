package com.starlight.expedition.feature.gamelist

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Platform

data class GameListUiState(
    val loading: Boolean = false,
    val games: List<Game> = emptyList(),
    val query: String = "",
    val selectedPlatform: Platform? = null,
    val availablePlatforms: List<Platform> = emptyList(),
    val errorMessage: String? = null
)
