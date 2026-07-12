package com.playroom.core.model

/** Filter chip selection on the 게임룸 (library) screen. */
sealed interface LibraryFilter {
    data object All : LibraryFilter
    data object Favorites : LibraryFilter
    data class BySystem(val system: GameSystem) : LibraryFilter
}
