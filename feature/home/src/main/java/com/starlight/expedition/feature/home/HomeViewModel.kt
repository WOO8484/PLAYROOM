package com.starlight.expedition.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.data.repository.FavoritesRepository
import com.starlight.expedition.core.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 오늘 플레이 시간은 세션 단위 실측 기록이 1차 개발 범위에 포함되지 않아,
 * 확정 GUI에 표시된 데모 수치를 그대로 사용합니다.
 */
private const val DEMO_TODAY_PLAY_MINUTES_LABEL = "42분"

class HomeViewModel(
    gameRepository: GameRepository,
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                gameRepository.observeRecentGames(),
                favoritesRepository.observeFavoriteIds()
            ) { recentGames, favoriteIds ->
                HomeUiState(
                    loading = false,
                    recentGames = recentGames,
                    favoriteCount = favoriteIds.size,
                    todayPlayMinutesLabel = DEMO_TODAY_PLAY_MINUTES_LABEL
                )
            }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(loading = false, errorMessage = throwable.message ?: "홈 정보를 불러오지 못했습니다.")
                    }
                }
                .collect { state -> _uiState.update { state } }
        }
    }
}
