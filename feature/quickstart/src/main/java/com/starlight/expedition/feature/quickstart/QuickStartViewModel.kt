package com.starlight.expedition.feature.quickstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.data.repository.RecommendationRepository
import com.starlight.expedition.core.model.Game
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

    private var currentGames: List<Game> = emptyList()

    init {
        viewModelScope.launch {
            gameRepository.observeGames()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(loading = false, errorMessage = throwable.message ?: "게임 정보를 불러오지 못했습니다.")
                    }
                }
                .collect { games ->
                    currentGames = games
                    // 실제로 플레이한 기록이 있는 게임 중 가장 최근 게임만 "이어 하기"로 사용합니다.
                    // 실제 실행 기능이 아직 없으므로, 검색으로 막 찾은 게임을 이어 하기로 표시하지 않습니다.
                    val continueGame = games.filter { it.lastPlayedAt != null }
                        .maxByOrNull { it.lastPlayedAt!! }

                    _uiState.update {
                        it.copy(
                            loading = false,
                            continueGame = continueGame,
                            hasAnyGames = games.isNotEmpty(),
                            errorMessage = null
                        )
                    }

                    val currentRecommendationStillValid = _uiState.value.recommendation
                        ?.let { recommendation -> games.any { it.id == recommendation.game.id } }
                        ?: false
                    if (!currentRecommendationStillValid) {
                        requestRecommendation()
                    }
                }
        }
    }

    /** 랜덤 선택 버튼 동작입니다. 현재 이어 하기 게임과 직전 추천 게임을 제외하고 다시 추천합니다. */
    fun requestRecommendation() {
        viewModelScope.launch {
            val excluded = setOfNotNull(
                _uiState.value.continueGame?.id,
                _uiState.value.recommendation?.game?.id
            )
            runCatching { recommendationRepository.recommendNext(currentGames, excluded) }
                .onSuccess { recommendation -> _uiState.update { it.copy(recommendation = recommendation) } }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "추천을 불러오지 못했습니다.") }
                }
        }
    }
}
