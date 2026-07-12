package com.playroom.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playroom.core.data.GameRepository
import com.playroom.core.data.GameRepositoryProvider
import com.playroom.core.data.filterByLibraryFilter
import com.playroom.core.model.LibraryFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val FAKE_SCAN_DELAY_MS = 1_000L
private const val FAKE_FOUND_GAME_COUNT = 8

class LibraryViewModel(
    private val repository: GameRepository = GameRepositoryProvider.repository,
) : ViewModel() {

    private val filter = MutableStateFlow<LibraryFilter>(LibraryFilter.All)
    private val detailGameId = MutableStateFlow<Int?>(null)
    private val isImportDialogOpen = MutableStateFlow(false)
    private val isScanning = MutableStateFlow(false)

    val uiState: StateFlow<LibraryUiState> = combine(
        repository.games,
        filter,
        detailGameId,
        isImportDialogOpen,
        isScanning,
    ) { games, filterValue, detailId, importOpen, scanning ->
        LibraryUiState(
            games = games.filterByLibraryFilter(filterValue),
            totalCount = games.size,
            filter = filterValue,
            detailGameId = detailId,
            isImportDialogOpen = importOpen,
            isScanning = scanning,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LibraryUiState(),
    )

    fun selectFilter(newFilter: LibraryFilter) {
        filter.value = newFilter
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

    fun openImportDialog() {
        isImportDialogOpen.value = true
    }

    fun closeImportDialog() {
        isImportDialogOpen.value = false
        isScanning.value = false
    }

    /** Simulated folder scan — no real file access, matches work order §21. */
    fun startFakeScan(onFinished: (foundCount: Int) -> Unit) {
        if (isScanning.value) return
        viewModelScope.launch {
            isScanning.value = true
            delay(FAKE_SCAN_DELAY_MS)
            isScanning.value = false
            isImportDialogOpen.value = false
            onFinished(FAKE_FOUND_GAME_COUNT)
        }
    }
}
