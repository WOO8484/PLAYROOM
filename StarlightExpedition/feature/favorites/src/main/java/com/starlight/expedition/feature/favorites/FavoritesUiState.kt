package com.starlight.expedition.feature.favorites

import com.starlight.expedition.core.model.Game

data class FavoritesUiState(
    val loading: Boolean = true,
    val favorites: List<Game> = emptyList(),
    val errorMessage: String? = null
)
