package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.data.local.GameLibraryFileDto
import com.starlight.expedition.core.data.local.GameLibraryFileStore
import com.starlight.expedition.core.data.local.toDomain
import com.starlight.expedition.core.data.local.toDto
import com.starlight.expedition.core.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 실제 폴더 검색으로 만들어진 로컬 게임 라이브러리를 화면에 제공합니다.
 * 샘플 데이터를 사용하지 않으며, [GameLibraryFileStore]에 저장된 내용을 그대로 반영합니다.
 *
 * [replaceAllGames]는 [GameFolderRepository]만 호출해야 합니다. 화면 ViewModel은
 * [GameRepository] 인터페이스만 바라보므로 이 메서드를 호출할 수 없습니다.
 */
class GameRepositoryImpl(
    private val fileStore: GameLibraryFileStore,
    private val favoritesRepository: FavoritesRepository,
    repositoryScope: CoroutineScope
) : GameRepository {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    init {
        repositoryScope.launch {
            val result = fileStore.read()
            val restored = when (result) {
                is GameLibraryFileStore.ReadResult.Success -> result.dto.games.map { it.toDomain() }
                is GameLibraryFileStore.ReadResult.NotFound -> emptyList()
                is GameLibraryFileStore.ReadResult.Corrupted -> emptyList()
            }
            _games.value = restored
            _isLoaded.value = true
        }
    }

    override fun observeGames(): Flow<List<Game>> =
        combine(_games, favoritesRepository.observeFavoriteIds()) { games, favoriteIds ->
            games.map { game -> game.copy(isFavorite = game.id in favoriteIds) }
        }

    override fun observeRecentGames(limit: Int): Flow<List<Game>> =
        observeGames().map { games ->
            games.filter { it.lastPlayedAt != null }
                .sortedByDescending { it.lastPlayedAt }
                .take(limit)
        }

    override fun observeGame(gameId: String): Flow<Game?> =
        observeGames().map { games -> games.firstOrNull { it.id == gameId } }

    override suspend fun setFavorite(gameId: String, favorite: Boolean) {
        favoritesRepository.setFavorite(gameId, favorite)
    }

    /** 폴더 검색·삭제 이후 병합이 끝난 전체 목록으로 교체하고 파일에 저장합니다. */
    suspend fun replaceAllGames(games: List<Game>): Result<Unit> {
        _games.value = games
        val dto = GameLibraryFileDto(
            savedAtEpochMillis = System.currentTimeMillis(),
            games = games.map { it.toDto() }
        )
        return fileStore.write(dto)
    }

    /** [GameFolderRepository]가 재검색 병합 계산에 사용하는 현재 스냅숏입니다. */
    fun currentGamesSnapshot(): List<Game> = _games.value
}
