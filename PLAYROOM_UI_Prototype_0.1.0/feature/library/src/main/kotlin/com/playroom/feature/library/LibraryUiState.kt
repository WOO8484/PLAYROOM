package com.playroom.feature.library

import com.playroom.core.model.Game
import com.playroom.core.model.LibraryFilter

data class LibraryUiState(
    val games: List<Game> = emptyList(),
    val totalCount: Int = 0,
    val filter: LibraryFilter = LibraryFilter.All,
    val detailGameId: Int? = null,
    val isImportDialogOpen: Boolean = false,
    val isScanning: Boolean = false,
)
