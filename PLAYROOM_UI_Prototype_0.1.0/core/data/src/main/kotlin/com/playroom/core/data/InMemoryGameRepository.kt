package com.playroom.core.data

import com.playroom.core.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** In-memory [GameRepository] backed by [FakeGameData]; resets on process death. */
class InMemoryGameRepository(
    initialGames: List<Game> = FakeGameData.games,
) : GameRepository {

    private val _games = MutableStateFlow(initialGames)
    override val games: StateFlow<List<Game>> = _games.asStateFlow()

    override fun getGame(gameId: Int): Game? = _games.value.find { it.id == gameId }

    override fun toggleFavorite(gameId: Int) {
        _games.update { current ->
            current.map { game ->
                if (game.id == gameId) game.copy(isFavorite = !game.isFavorite) else game
            }
        }
    }
}
