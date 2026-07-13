package com.starlight.expedition.feature.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.common.SearchUtils
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameListViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val selectedPlatform = MutableStateFlow<Platform?>(null)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(GameListUiState())
    val uiState: StateFlow<GameListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                gameRepository.observeGames().catch { throwable ->
                    errorMessage.value = throwable.message ?: "게임리스트를 불러오지 못했습니다."
                    emit(emptyList())
                },
                query,
                selectedPlatform
            ) { games, currentQuery, currentPlatform ->
                val availablePlatforms = games.map { it.platform }.distinct().sortedBy { it.ordinal }
                val filtered = games.filter { game ->
                    matchesQuery(game, currentQuery) && (currentPlatform == null || game.platform == currentPlatform)
                }
                val sorted = filtered.sortedWith(
                    compareByDescending<Game> { it.lastPlayedAt != null }
                        .thenBy { it.titleKo }
                )
                GameListUiState(
                    loading = false,
                    games = sorted,
                    query = currentQuery,
                    selectedPlatform = currentPlatform,
                    availablePlatforms = availablePlatforms,
                    errorMessage = errorMessage.value
                )
            }.collect { state -> _uiState.update { state } }
        }
    }

    private fun matchesQuery(game: Game, currentQuery: String): Boolean {
        if (currentQuery.isBlank()) return true
        return SearchUtils.matches(game.titleKo, currentQuery) ||
            SearchUtils.matches(game.fileName, currentQuery) ||
            (game.originalTitle?.let { SearchUtils.matches(it, currentQuery) } ?: false) ||
            SearchUtils.matches(game.platform.displayName, currentQuery)
    }

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }

    fun onPlatformSelected(platform: Platform?) {
        selectedPlatform.value = platform
    }

    fun toggleFavorite(gameId: String, favorite: Boolean) {
        viewModelScope.launch {
            gameRepository.setFavorite(gameId, favorite)
        }
    }
}
