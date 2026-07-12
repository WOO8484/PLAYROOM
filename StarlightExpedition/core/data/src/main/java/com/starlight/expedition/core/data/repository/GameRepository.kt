package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeGames(): Flow<List<Game>>
    fun observeRecentGames(): Flow<List<Game>>
    suspend fun setFavorite(gameId: String, favorite: Boolean)
}
