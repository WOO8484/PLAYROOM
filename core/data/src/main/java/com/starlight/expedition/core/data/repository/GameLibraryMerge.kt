package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.Game

/**
 * 재검색 결과를 기존 라이브러리에 병합하는 순수 로직입니다(실행지시서 21.2절).
 * 같은 폴더의 이전 게임 중 이번에 다시 발견되면 즐겨찾기·플레이 기록을 유지하고,
 * 발견되지 않으면 삭제 대상으로 봅니다. 다른 폴더의 게임은 건드리지 않습니다.
 */
object GameLibraryMerge {

    data class MergeResult(
        val mergedGames: List<Game>,
        val addedCount: Int,
        val updatedCount: Int,
        val removedCount: Int
    )

    fun mergeFolderScan(
        existingGames: List<Game>,
        folderId: String,
        freshGamesForFolder: List<Game>
    ): MergeResult {
        val otherFolderGames = existingGames.filterNot { it.sourceFolderId == folderId }
        val previousFolderGamesById = existingGames.filter { it.sourceFolderId == folderId }.associateBy { it.id }
        val freshById = freshGamesForFolder.associateBy { it.id }

        val merged = freshGamesForFolder.map { fresh ->
            val previous = previousFolderGamesById[fresh.id]
            if (previous != null) {
                // 즐겨찾기는 별도 FavoritesRepository가 id 기준으로 담당하므로 여기서는 건드리지 않습니다.
                fresh.copy(
                    addedAtEpochMillis = previous.addedAtEpochMillis,
                    lastPlayedAt = previous.lastPlayedAt,
                    totalPlayMinutes = previous.totalPlayMinutes,
                    playCount = previous.playCount,
                    progressLabel = previous.progressLabel
                )
            } else {
                fresh
            }
        }

        val addedCount = freshById.keys.count { it !in previousFolderGamesById }
        val updatedCount = freshById.keys.count { it in previousFolderGamesById }
        val removedCount = previousFolderGamesById.keys.count { it !in freshById }

        return MergeResult(
            mergedGames = otherFolderGames + merged,
            addedCount = addedCount,
            updatedCount = updatedCount,
            removedCount = removedCount
        )
    }

    fun removeFolder(existingGames: List<Game>, folderId: String): List<Game> =
        existingGames.filterNot { it.sourceFolderId == folderId }
}
