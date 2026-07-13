package com.starlight.expedition.feature.library

import com.starlight.expedition.core.model.GameScanState
import com.starlight.expedition.core.model.GameSourceFolder

data class GameFolderUiState(
    val folders: List<GameSourceFolder> = emptyList(),
    val scanState: GameScanState = GameScanState.Idle
)
