package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.GameScanState
import com.starlight.expedition.core.model.GameSourceFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GameFolderRepository {
    fun observeFolders(): Flow<List<GameSourceFolder>>
    fun observeScanState(): StateFlow<GameScanState>

    suspend fun addFolder(treeUri: String, displayName: String): Result<GameSourceFolder>
    suspend fun removeFolder(folderId: String): Result<Unit>

    fun startScan(folderId: String? = null)
    fun cancelScan()

    /** 앱 시작 및 다이얼로그를 열 때 등록 폴더들의 실제 권한 상태를 다시 확인합니다. */
    suspend fun refreshPermissionStates()

    /** 사용자가 SAF로 방금 선택한 트리 URI의 표시 이름을 가져옵니다. */
    fun displayNameOf(treeUri: String): String
}
