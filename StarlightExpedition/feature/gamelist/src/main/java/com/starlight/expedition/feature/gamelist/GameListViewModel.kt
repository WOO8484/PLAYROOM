package com.starlight.expedition.feature.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.common.SearchUtils
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.model.GameGenre
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
    private val selectedGenre = MutableStateFlow<GameGenre?>(null)
    private val loading = MutableStateFlow(true)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(GameListUiState())
    val uiState: StateFlow<GameListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                gameRepository.observeGames().catch { throwable ->
                    loading.value = false
                    errorMessage.value = throwable.message ?: "게임리스트를 불러오지 못했습니다."
                    emit(emptyList())
                },
                query,
                selectedGenre
            ) { games, currentQuery, currentGenre ->
                val filtered = games.filter { game ->
                    SearchUtils.matches(game.titleKo, currentQuery) &&
                        (currentGenre == null || game.genre == currentGenre)
                }
                GameListUiState(
                    loading = false,
                    games = filtered,
                    query = currentQuery,
                    selectedGenre = currentGenre,
                    errorMessage = errorMessage.value
                )
            }.collect { state -> _uiState.update { state } }
        }
    }

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }

    fun onGenreSelected(genre: GameGenre?) {
        selectedGenre.value = genre
    }

    fun toggleFavorite(gameId: String, favorite: Boolean) {
        viewModelScope.launch {
            gameRepository.setFavorite(gameId, favorite)
        }
    }
}
