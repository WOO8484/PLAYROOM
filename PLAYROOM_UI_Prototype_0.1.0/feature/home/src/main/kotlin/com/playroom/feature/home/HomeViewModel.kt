package com.playroom.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playroom.core.data.GameRepository
import com.playroom.core.data.GameRepositoryProvider
import com.playroom.core.data.pickRecommendation
import com.playroom.core.model.RecommendationOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** The 1번 카드 (이어서 하기) always shows this fixed game in this UI-test build. */
private const val CONTINUE_PLAYING_GAME_ID = 1
private const val INITIAL_RECOMMENDATION_ID = 2

class HomeViewModel(
    private val repository: GameRepository = GameRepositoryProvider.repository,
) : ViewModel() {

    private val recommendedGameId = MutableStateFlow(INITIAL_RECOMMENDATION_ID)
    private val recommendationOptions = MutableStateFlow(RecommendationOptions())
    private val detailGameId = MutableStateFlow<Int?>(null)
    private val isOptionsDialogOpen = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.games,
        recommendedGameId,
        recommendationOptions,
        detailGameId,
        isOptionsDialogOpen,
    ) { games, recommendedId, options, detailId, optionsOpen ->
        HomeUiState(
            continuePlayingGame = games.find { it.id == CONTINUE_PLAYING_GAME_ID },
            recommendedGame = games.find { it.id == recommendedId },
            recommendationOptions = options,
            detailGameId = detailId,
            isOptionsDialogOpen = optionsOpen,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    fun openDetail(gameId: Int) {
        detailGameId.value = gameId
    }

    fun closeDetail() {
        detailGameId.value = null
    }

    fun openOptionsDialog() {
        isOptionsDialogOpen.value = true
    }

    fun closeOptionsDialog() {
        isOptionsDialogOpen.value = false
    }

    fun updateRecommendationOptions(options: RecommendationOptions) {
        recommendationOptions.value = options
    }

    fun applyOptionsAndRecommend() {
        isOptionsDialogOpen.value = false
        pickNewRecommendation()
    }

    fun pickNewRecommendation() {
        val picked = repository.games.value.pickRecommendation(
            options = recommendationOptions.value,
            excludeId = recommendedGameId.value,
        )
        if (picked != null) recommendedGameId.value = picked.id
    }

    fun toggleFavorite(gameId: Int) {
        repository.toggleFavorite(gameId)
    }
}
