package com.starlight.expedition.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starlight.expedition.core.data.repository.GameFolderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameFolderViewModel(
    private val folderRepository: GameFolderRepository
) : ViewModel() {

    val uiState: StateFlow<GameFolderUiState> = combine(
        folderRepository.observeFolders(),
        folderRepository.observeScanState()
    ) { folders, scanState ->
        GameFolderUiState(folders = folders, scanState = scanState)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GameFolderUiState())

    /** 다이얼로그가 열릴 때마다 실제 권한 상태를 다시 확인합니다. */
    fun onDialogOpened() {
        viewModelScope.launch { folderRepository.refreshPermissionStates() }
    }

    fun onFolderPicked(treeUriString: String) {
        val displayName = folderRepository.displayNameOf(treeUriString)
        viewModelScope.launch { folderRepository.addFolder(treeUriString, displayName) }
    }

    fun onRemoveFolder(folderId: String) {
        viewModelScope.launch { folderRepository.removeFolder(folderId) }
    }

    fun onRescanFolder(folderId: String) {
        folderRepository.startScan(folderId)
    }

    fun onRescanAll() {
        folderRepository.startScan(null)
    }

    fun onCancelScan() {
        folderRepository.cancelScan()
    }
}
