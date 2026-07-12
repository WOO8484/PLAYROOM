package com.playroom.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playroom.core.data.GameRepository
import com.playroom.core.data.GameRepositoryProvider
import com.playroom.core.data.search
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val repository: GameRepository = GameRepositoryProvider.repository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val detailGameId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<SearchUiState> = combine(
        repository.games,
        query,
        detailGameId,
    ) { games, queryValue, detailId ->
        SearchUiState(
            query = queryValue,
            results = games.search(queryValue),
            detailGameId = detailId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState(),
    )

    fun updateQuery(newQuery: String) {
        query.value = newQuery
    }

    fun openDetail(gameId: Int) {
        detailGameId.value = gameId
    }

    fun closeDetail() {
        detailGameId.value = null
    }

    fun toggleFavorite(gameId: Int) {
        repository.toggleFavorite(gameId)
    }
}
