package com.playroom.feature.player

import com.playroom.core.model.Game

data class PlayerUiState(
    val game: Game? = null,
    val isPaused: Boolean = false,
)
