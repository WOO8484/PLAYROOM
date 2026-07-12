package com.starlight.expedition.core.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * 즐겨찾기로 등록된 게임 id 집합을 영구 저장합니다.
 * 게임 목록 자체는 GameRepository가 담당하고, 이 Repository는 즐겨찾기 여부만 책임집니다.
 */
interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<Set<String>>
    suspend fun setFavorite(gameId: String, favorite: Boolean)
}
