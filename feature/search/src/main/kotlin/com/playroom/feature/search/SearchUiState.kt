package com.playroom.feature.search

import com.playroom.core.model.Game

data class SearchUiState(
    val query: String = "",
    val results: List<Game> = emptyList(),
    val detailGameId: Int? = null,
)
