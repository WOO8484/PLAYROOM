package com.starlight.expedition.feature.quickstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.data.local.SampleGameData
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.data.repository.RecommendationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuickStartViewModel(
    private val gameRepository: GameRepository,
    private val recommendationRepository: RecommendationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickStartUiState())
    val uiState: StateFlow<QuickStartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.observeGames()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(loading = false, errorMessage = throwable.message ?: "게임 정보를 불러오지 못했습니다.")
                    }
                }
                .collect { games ->
                    val continueGame = games.firstOrNull { it.id == SampleGameData.CONTINUE_GAME_ID }
                    _uiState.update { it.copy(loading = false, continueGame = continueGame, errorMessage = null) }

                    if (_uiState.value.recommendation == null) {
                        requestRecommendation()
                    }
                }
        }
    }

    /** 랜덤 선택 버튼 동작입니다. 현재 추천과 이어 하기 게임을 제외하고 다시 추천합니다. */
    fun requestRecommendation() {
        viewModelScope.launch {
            val excluded = setOfNotNull(
                _uiState.value.continueGame?.id,
                _uiState.value.recommendation?.game?.id
            )
            runCatching { recommendationRepository.recommendNext(excluded) }
                .onSuccess { recommendation -> _uiState.update { it.copy(recommendation = recommendation) } }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "추천을 불러오지 못했습니다.") }
                }
        }
    }
}
