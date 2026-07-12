package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.data.local.SampleGameData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakeFavoritesRepository(
    initialFavorites: Set<String>
) : FavoritesRepository {

    private val favorites = MutableStateFlow(initialFavorites)

    override fun observeFavoriteIds() = favorites

    override suspend fun setFavorite(gameId: String, favorite: Boolean) {
        favorites.value = if (favorite) favorites.value + gameId else favorites.value - gameId
    }
}

class GameRepositoryImplTest {

    @Test
    fun `observeGames reflects favorites repository state`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(setOf(SampleGameData.CONTINUE_GAME_ID))
        val repository = GameRepositoryImpl(fakeFavorites)

        val games = repository.observeGames().first()
        val continueGame = games.first { it.id == SampleGameData.CONTINUE_GAME_ID }
        val other = games.first { it.id != SampleGameData.CONTINUE_GAME_ID }

        assertTrue(continueGame.isFavorite)
        assertTrue(!other.isFavorite)
    }

    @Test
    fun `setFavorite toggles state through favorites repository`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(emptySet())
        val repository = GameRepositoryImpl(fakeFavorites)
        val targetId = SampleGameData.games[1].id

        repository.setFavorite(targetId, true)

        val games = repository.observeGames().first()
        assertTrue(games.first { it.id == targetId }.isFavorite)
    }

    @Test
    fun `observeRecentGames returns at most three games sorted by last played`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(SampleGameData.defaultFavoriteIds)
        val repository = GameRepositoryImpl(fakeFavorites)

        val recent = repository.observeRecentGames().first()

        assertTrue(recent.size <= 3)
        assertEquals(recent, recent.sortedByDescending { it.lastPlayedAt })
    }
}
