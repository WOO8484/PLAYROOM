package com.starlight.expedition.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.observeGames()
                .map { games -> games.filter { it.isFavorite } }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(loading = false, errorMessage = throwable.message ?: "즐겨찾기를 불러오지 못했습니다.")
                    }
                }
                .collect { favorites ->
                    _uiState.update { it.copy(loading = false, favorites = favorites, errorMessage = null) }
                }
        }
    }

    fun toggleFavorite(gameId: String, favorite: Boolean) {
        viewModelScope.launch {
            gameRepository.setFavorite(gameId, favorite)
        }
    }
}
