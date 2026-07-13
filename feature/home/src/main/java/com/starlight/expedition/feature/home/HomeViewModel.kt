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
 * 실제 게임 실행 연동이 아직 없어 세션 단위 플레이 시간을 측정할 수 없습니다.
 * 스캔한 게임을 실행한 것으로 간주하지 않으므로, 실제 연동 전까지는 항상 0분입니다.
 */
private const val NO_SESSION_TRACKING_LABEL = "0분"

class HomeViewModel(
    gameRepository: GameRepository,
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                gameRepository.observeGames(),
                gameRepository.observeRecentGames(limit = 3),
                favoritesRepository.observeFavoriteIds()
            ) { allGames, recentGames, favoriteIds ->
                HomeUiState(
                    loading = false,
                    recentGames = recentGames,
                    libraryGameCount = allGames.size,
                    favoriteCount = favoriteIds.size,
                    todayPlayMinutesLabel = NO_SESSION_TRACKING_LABEL
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
