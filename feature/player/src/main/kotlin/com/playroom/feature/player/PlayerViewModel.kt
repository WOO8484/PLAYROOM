package com.playroom.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playroom.core.data.GameRepository
import com.playroom.core.data.GameRepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    private val gameId: Int,
    private val repository: GameRepository = GameRepositoryProvider.repository,
) : ViewModel() {

    private val isPaused = MutableStateFlow(false)

    val uiState: StateFlow<PlayerUiState> = combine(repository.games, isPaused) { games, paused ->
        PlayerUiState(game = games.find { it.id == gameId }, isPaused = paused)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlayerUiState(),
    )

    fun openPauseMenu() {
        isPaused.value = true
    }

    fun closePauseMenu() {
        isPaused.value = false
    }
}
