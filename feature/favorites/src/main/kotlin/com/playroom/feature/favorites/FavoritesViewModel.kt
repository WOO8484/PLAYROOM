package com.playroom.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playroom.core.data.GameRepository
import com.playroom.core.data.GameRepositoryProvider
import com.playroom.core.data.favorites
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    private val repository: GameRepository = GameRepositoryProvider.repository,
) : ViewModel() {

    private val detailGameId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<FavoritesUiState> = combine(repository.games, detailGameId) { games, detailId ->
        FavoritesUiState(favoriteGames = games.favorites(), detailGameId = detailId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesUiState(),
    )

    fun openDetail(gameId: Int) {
        detailGameId.value = gameId
    }

    fun closeDetail() {
        detailGameId.value = null
    }

    fun removeFavorite(gameId: Int) {
        repository.toggleFavorite(gameId)
    }
}
