package com.starlight.expedition.feature.quickstart

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Recommendation

data class QuickStartUiState(
    val loading: Boolean = true,
    val continueGame: Game? = null,
    val recommendation: Recommendation? = null,
    val errorMessage: String? = null
)
