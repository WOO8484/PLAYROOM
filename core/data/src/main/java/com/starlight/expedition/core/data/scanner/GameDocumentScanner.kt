package com.starlight.expedition.core.data.scanner

import android.content.Context
import android.net.Uri
import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre
import com.starlight.expedition.core.model.GameSourceFolder
import com.starlight.expedition.core.model.Platform
import kotlinx.coroutines.flow.collect

/**
 * 폴더 1개를 실제로 검색해 게임 후보를 만듭니다.
 * 이전 라이브러리 상태와의 비교(신규/수정/삭제 판정, 즐겨찾기·플레이 기록 유지)는
 * 이 클래스가 하지 않고 Repository가 담당합니다. 이 클래스는 "지금 폴더에 무엇이 있는지"만 답합니다.
 */
class GameDocumentScanner(
    private val context: Context,
    private val treeReader: DocumentsContractTreeReader = DocumentsContractTreeReader(context)
) {

    data class ScanOutcome(
        val games: List<Game>,
        val scannedFiles: Int,
        val supportedCandidates: Int,
        val skippedFiles: Int,
        val unclassifiedFiles: Int,
        val matchedCovers: Int
    )

    suspend fun scanFolder(
        folder: GameSourceFolder,
        onProgress: (visitedCount: Int, candidateCount: Int, currentName: String?) -> Unit
    ): ScanOutcome {
        val treeUri = Uri.parse(folder.treeUri)
        val allEntries = mutableListOf<ScannedFileEntry>()
        var visited = 0

        treeReader.walk(treeUri).collect { entry ->
            allEntries += entry
            visited++
            if (visited % 25 == 0) {
                onProgress(visited, allEntries.size, entry.fileName)
            }
        }
        onProgress(visited, allEntries.size, null)

        val imageEntries = allEntries.filter { ScanExclusionRules.isImageExtension(it.extensionLower) }
        val candidateEntries = allEntries.filter { entry ->
            !ScanExclusionRules.isHidden(entry.fileName) &&
                !ScanExclusionRules.isExcludedFileExtension(entry.extensionLower) &&
                !ScanExclusionRules.looksLikeBiosFileName(entry.fileName)
        }

        val groups = GameFileGrouping.group(candidateEntries)

        val imagesByFolder: Map<String, List<ScannedFileEntry>> = imageEntries.groupBy { it.folderKey }
        val coverFolderImages: List<ScannedFileEntry> = imageEntries.filter { entry ->
            entry.parentFolderNames.any { ScanExclusionRules.isCoverFolderName(it) }
        }

        val games = mutableListOf<Game>()
        var unclassified = 0
        var matchedCovers = 0
        val nowMillis = System.currentTimeMillis()

        for (group in groups) {
            val representative = group.representative
            val classification = PlatformClassifier.classify(
                fileName = representative.fileName,
                parentFolderNames = representative.parentFolderNames,
                hasMatchingCueSibling = false
            )

            if (PlatformClassifier.isAutoRegisterExcluded(representative.fileName, classification)) {
                unclassified++
                continue
            }
            if (classification.platform == Platform.UNKNOWN ||
                classification.confidence == ClassificationConfidence.UNKNOWN
            ) {
                unclassified++
            }

            val normalizedTitle = GameTitleNormalizer.normalize(representative.fileName)
            val id = Sha256.of("${folder.id}|${representative.documentId}")
            val romBaseName = representative.fileName.substringBeforeLast('.', representative.fileName)

            val sameFolderCandidates = (imagesByFolder[representative.folderKey] ?: emptyList()).map {
                LocalCoverMatcher.CoverCandidate(it.fileName.substringBeforeLast('.', it.fileName), it.documentUri)
            }
            var coverMatch = LocalCoverMatcher.findBestMatch(normalizedTitle, romBaseName, sameFolderCandidates)
            if (coverMatch == null) {
                val coverFolderCandidates = coverFolderImages.map {
                    LocalCoverMatcher.CoverCandidate(it.fileName.substringBeforeLast('.', it.fileName), it.documentUri)
                }
                coverMatch = LocalCoverMatcher.findBestMatch(normalizedTitle, romBaseName, coverFolderCandidates)
            }
            if (coverMatch != null) matchedCovers++

            games += Game(
                id = id,
                sourceFolderId = folder.id,
                documentUri = representative.documentUri,
                documentId = representative.documentId,
                fileName = representative.fileName,
                titleKo = normalizedTitle,
                originalTitle = romBaseName,
                normalizedTitle = normalizedTitle,
                genre = GameGenre.UNKNOWN,
                platform = classification.platform,
                platformConfidence = classification.confidence,
                classificationReason = classification.reason,
                fileExtension = representative.extensionLower,
                mimeType = representative.mimeType,
                sizeBytes = representative.sizeBytes,
                modifiedAtEpochMillis = representative.modifiedAtEpochMillis,
                coverUri = coverMatch?.uri,
                companionUris = group.companions.map { it.documentUri },
                isLaunchable = classification.launchable,
                descriptionLines = listOf("${classification.platform.displayName} · 폴더에서 자동으로 찾은 게임입니다."),
                isFavorite = false,
                addedAtEpochMillis = nowMillis,
                lastSeenAtEpochMillis = nowMillis,
                lastPlayedAt = null,
                totalPlayMinutes = 0,
                playCount = 0,
                progressLabel = null
            )
        }

        return ScanOutcome(
            games = games,
            scannedFiles = visited,
            supportedCandidates = groups.size,
            skippedFiles = allEntries.size - candidateEntries.size,
            unclassifiedFiles = unclassified,
            matchedCovers = matchedCovers
        )
    }

    fun displayNameOf(treeUri: Uri): String = treeReader.displayNameOf(treeUri)
}
