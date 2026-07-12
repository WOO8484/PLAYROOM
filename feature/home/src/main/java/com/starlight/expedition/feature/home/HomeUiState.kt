package com.starlight.expedition.feature.home

import com.starlight.expedition.core.model.Game

data class HomeUiState(
    val loading: Boolean = true,
    val recentGames: List<Game> = emptyList(),
    val favoriteCount: Int = 0,
    val todayPlayMinutesLabel: String = "0분",
    val errorMessage: String? = null
)
