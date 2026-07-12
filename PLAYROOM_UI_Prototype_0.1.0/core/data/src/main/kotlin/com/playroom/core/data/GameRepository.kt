package com.playroom.core.data

import com.playroom.core.model.Game
import kotlinx.coroutines.flow.StateFlow

/**
 * Single source of truth for the (fake) game library and favorite state,
 * shared by every feature module. No real file-system or network access
 * happens here — this is the UI-test stage described in the work order.
 */
interface GameRepository {
    val games: StateFlow<List<Game>>

    fun getGame(gameId: Int): Game?

    fun toggleFavorite(gameId: Int)
}
