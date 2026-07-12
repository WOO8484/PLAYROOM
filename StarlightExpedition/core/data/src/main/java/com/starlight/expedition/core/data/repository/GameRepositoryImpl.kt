package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.data.local.SampleGameData
import com.starlight.expedition.core.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 로컬 샘플 게임 목록에 FavoritesRepository의 즐겨찾기 상태를 결합해 제공합니다.
 */
class GameRepositoryImpl(
    private val favoritesRepository: FavoritesRepository
) : GameRepository {

    private val baseGames: List<Game> = SampleGameData.games

    override fun observeGames(): Flow<List<Game>> =
        favoritesRepository.observeFavoriteIds().map { favoriteIds ->
            baseGames.map { game -> game.copy(isFavorite = game.id in favoriteIds) }
        }

    override fun observeRecentGames(): Flow<List<Game>> =
        observeGames().map { games ->
            games
                .filter { it.lastPlayedAt != null }
                .sortedByDescending { it.lastPlayedAt }
                .take(3)
        }

    override suspend fun setFavorite(gameId: String, favorite: Boolean) {
        favoritesRepository.setFavorite(gameId, favorite)
    }
}
