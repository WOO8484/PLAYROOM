package com.starlight.expedition.core.data.repository

import android.content.Context
import android.net.Uri
import com.starlight.expedition.core.data.local.GameFolderPreferences
import com.starlight.expedition.core.data.local.toDomain
import com.starlight.expedition.core.data.local.toDto
import com.starlight.expedition.core.data.scanner.GameDocumentScanner
import com.starlight.expedition.core.data.scanner.Sha256
import com.starlight.expedition.core.model.FolderPermissionState
import com.starlight.expedition.core.model.GameScanState
import com.starlight.expedition.core.model.GameScanSummary
import com.starlight.expedition.core.model.GameSourceFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 폴더 등록·삭제·검색을 담당합니다. 검색 자체는 [GameDocumentScanner]에 위임하고,
 * 이전 상태와의 병합은 [GameLibraryMerge]에 위임합니다. 이 클래스는 그 결과를
 * [GameRepositoryImpl]에 커밋하는 조정자 역할만 합니다.
 *
 * 검색 코루틴은 [applicationScope](Application 수명)에서 실행되므로,
 * 다이얼로그를 닫거나 탭을 이동해도 검색이 계속됩니다(실행지시서 17절).
 */
class GameFolderRepositoryImpl(
    private val context: Context,
    private val folderPreferences: GameFolderPreferences,
    private val scanner: GameDocumentScanner,
    private val gameRepository: GameRepositoryImpl,
    private val applicationScope: CoroutineScope
) : GameFolderRepository {

    private val _folders = MutableStateFlow<List<GameSourceFolder>>(emptyList())
    private val _scanState = MutableStateFlow<GameScanState>(GameScanState.Idle)

    private var currentScanJob: Job? = null

    init {
        applicationScope.launch {
            val storedFolders = folderPreferences.observeFolders().first()
            _folders.value = storedFolders.map { dto ->
                dto.toDomain(permissionState = FolderPermissionState.UNKNOWN, lastErrorMessage = null)
            }
            refreshPermissionStates()

            // 이전 실행이 검색 도중 종료됐다면(진행 요약이 없다면) 다음 실행에서 안내합니다.
            val anyScanned = _folders.value.any { it.lastScannedAtEpochMillis != null }
            if (_folders.value.isNotEmpty() && !anyScanned) {
                _scanState.value = GameScanState.Interrupted(folderId = null)
            }
        }
    }

    override fun observeFolders(): Flow<List<GameSourceFolder>> = _folders

    override fun observeScanState(): StateFlow<GameScanState> = _scanState.asStateFlow()

    override suspend fun addFolder(treeUri: String, displayName: String): Result<GameSourceFolder> {
        return try {
            val id = Sha256.of(treeUri)
            val now = System.currentTimeMillis()
            val folder = GameSourceFolder(
                id = id,
                treeUri = treeUri,
                displayName = displayName,
                addedAtEpochMillis = now,
                lastScannedAtEpochMillis = null,
                lastKnownGameCount = 0,
                enabled = true,
                permissionState = FolderPermissionState.GRANTED,
                lastErrorMessage = null
            )
            val updated = _folders.value.filterNot { it.id == id } + folder
            _folders.value = updated
            persistFolders(updated)
            startScan(id)
            Result.success(folder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFolder(folderId: String): Result<Unit> {
        return try {
            val updatedFolders = _folders.value.filterNot { it.id == folderId }
            _folders.value = updatedFolders
            persistFolders(updatedFolders)

            val remainingGames = GameLibraryMerge.removeFolder(gameRepository.currentGamesSnapshot(), folderId)
            gameRepository.replaceAllGames(remainingGames)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun startScan(folderId: String?) {
        if (currentScanJob?.isActive == true) {
            return
        }
        val targets = if (folderId != null) {
            _folders.value.filter { it.id == folderId }
        } else {
            _folders.value.filter { it.enabled }
        }
        if (targets.isEmpty()) {
            return
        }

        currentScanJob = applicationScope.launch {
            for (folder in targets) {
                scanSingleFolder(folder)
            }
        }
    }

    override fun cancelScan() {
        if (currentScanJob?.isActive == true) {
            currentScanJob?.cancel()
            _scanState.value = GameScanState.Cancelled
        }
    }

    override suspend fun refreshPermissionStates() {
        val persisted = context.contentResolver.persistedUriPermissions
        val grantedUris = persisted.filter { it.isReadPermission }.map { it.uri.toString() }.toSet()

        val updated = _folders.value.map { folder ->
            val state = if (folder.treeUri in grantedUris) FolderPermissionState.GRANTED else FolderPermissionState.LOST
            folder.copy(permissionState = state)
        }
        _folders.value = updated
        persistFolders(updated)
    }

    private suspend fun scanSingleFolder(folder: GameSourceFolder) {
        val hasPermission = context.contentResolver.persistedUriPermissions.any {
            it.uri.toString() == folder.treeUri && it.isReadPermission
        }
        if (!hasPermission) {
            markFolder(folder.id) { it.copy(permissionState = FolderPermissionState.LOST) }
            _scanState.value = GameScanState.Failed(
                folderId = folder.id,
                message = "폴더 접근 권한이 사라졌습니다. 폴더를 다시 선택해 주세요.",
                recoverable = true
            )
            return
        }

        _scanState.value = GameScanState.Scanning(
            folderId = folder.id,
            folderName = folder.displayName,
            visitedCount = 0,
            candidateCount = 0,
            currentName = null
        )

        val startedAt = System.currentTimeMillis()
        try {
            val outcome = scanner.scanFolder(folder) { visited, candidates, currentName ->
                _scanState.value = GameScanState.Scanning(
                    folderId = folder.id,
                    folderName = folder.displayName,
                    visitedCount = visited,
                    candidateCount = candidates,
                    currentName = currentName
                )
            }

            val mergeResult = GameLibraryMerge.mergeFolderScan(
                existingGames = gameRepository.currentGamesSnapshot(),
                folderId = folder.id,
                freshGamesForFolder = outcome.games
            )
            gameRepository.replaceAllGames(mergeResult.mergedGames)

            markFolder(folder.id) {
                it.copy(
                    lastScannedAtEpochMillis = System.currentTimeMillis(),
                    lastKnownGameCount = outcome.games.size,
                    permissionState = FolderPermissionState.GRANTED,
                    lastErrorMessage = null
                )
            }

            _scanState.value = GameScanState.Completed(
                summary = GameScanSummary(
                    scannedFiles = outcome.scannedFiles,
                    supportedCandidates = outcome.supportedCandidates,
                    addedGames = mergeResult.addedCount,
                    updatedGames = mergeResult.updatedCount,
                    removedGames = mergeResult.removedCount,
                    skippedFiles = outcome.skippedFiles,
                    unclassifiedFiles = outcome.unclassifiedFiles,
                    matchedCovers = outcome.matchedCovers,
                    durationMillis = System.currentTimeMillis() - startedAt
                )
            )
        } catch (e: SecurityException) {
            markFolder(folder.id) { it.copy(permissionState = FolderPermissionState.LOST, lastErrorMessage = e.message) }
            _scanState.value = GameScanState.Failed(
                folderId = folder.id,
                message = "폴더 접근 권한이 사라졌습니다. 폴더를 다시 선택해 주세요.",
                recoverable = true
            )
        } catch (e: Exception) {
            markFolder(folder.id) { it.copy(lastErrorMessage = e.message) }
            _scanState.value = GameScanState.Failed(
                folderId = folder.id,
                message = "폴더를 검색하지 못했습니다. 잠시 후 다시 시도해 주세요.",
                recoverable = true
            )
        }
    }

    private suspend fun markFolder(folderId: String, transform: (GameSourceFolder) -> GameSourceFolder) {
        val updated = _folders.value.map { if (it.id == folderId) transform(it) else it }
        _folders.value = updated
        persistFolders(updated)
    }

    private suspend fun persistFolders(folders: List<GameSourceFolder>) {
        folderPreferences.saveFolders(folders.map { it.toDto() })
    }

    /** 사용자가 SAF로 선택한 트리 URI의 표시 이름을 가져올 때 사용합니다. */
    override fun displayNameOf(treeUri: String): String = scanner.displayNameOf(Uri.parse(treeUri))
}
