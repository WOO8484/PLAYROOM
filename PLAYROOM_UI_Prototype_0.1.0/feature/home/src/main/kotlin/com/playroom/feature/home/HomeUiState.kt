package com.playroom.feature.home

import com.playroom.core.model.Game
import com.playroom.core.model.RecommendationOptions

data class HomeUiState(
    val continuePlayingGame: Game? = null,
    val recommendedGame: Game? = null,
    val recommendationOptions: RecommendationOptions = RecommendationOptions(),
    val detailGameId: Int? = null,
    val isOptionsDialogOpen: Boolean = false,
)
