package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre
import com.starlight.expedition.core.model.Platform
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 재검색 병합(GameLibraryMerge)에 대한 저장소 단위 테스트입니다(실행지시서 35절 RepositoryTest).
 */
class RepositoryTest {

    private fun game(
        id: String,
        folderId: String = "folder-1",
        lastPlayedAt: Instant? = null,
        totalPlayMinutes: Int = 0,
        playCount: Int = 0,
        addedAtEpochMillis: Long = 1000L
    ) = Game(
        id = id,
        sourceFolderId = folderId,
        documentUri = "content://tree/$id",
        documentId = id,
        fileName = "$id.gba",
        titleKo = id,
        originalTitle = id,
        normalizedTitle = id,
        genre = GameGenre.UNKNOWN,
        platform = Platform.GAME_BOY_ADVANCE,
        platformConfidence = ClassificationConfidence.HIGH,
        classificationReason = "확장자 .gba",
        fileExtension = "gba",
        mimeType = null,
        sizeBytes = null,
        modifiedAtEpochMillis = null,
        coverUri = null,
        companionUris = emptyList(),
        isLaunchable = true,
        descriptionLines = emptyList(),
        isFavorite = false,
        addedAtEpochMillis = addedAtEpochMillis,
        lastSeenAtEpochMillis = addedAtEpochMillis,
        lastPlayedAt = lastPlayedAt,
        totalPlayMinutes = totalPlayMinutes,
        playCount = playCount,
        progressLabel = null
    )

    @Test
    fun `rescan upserts new and existing games`() {
        val existing = listOf(game("a"), game("b"))
        val fresh = listOf(game("a"), game("c"))

        val result = GameLibraryMerge.mergeFolderScan(existing, folderId = "folder-1", freshGamesForFolder = fresh)

        assertEquals(1, result.addedCount)
        assertEquals(1, result.updatedCount)
        assertEquals(1, result.removedCount)
        assertEquals(setOf("a", "c"), result.mergedGames.map { it.id }.toSet())
    }

    @Test
    fun `games missing from a successful rescan are removed`() {
        val existing = listOf(game("a"), game("b"), game("c"))
        val fresh = listOf(game("a"))

        val result = GameLibraryMerge.mergeFolderScan(existing, folderId = "folder-1", freshGamesForFolder = fresh)

        assertEquals(listOf("a"), result.mergedGames.map { it.id })
        assertEquals(2, result.removedCount)
    }

    @Test
    fun `play history is preserved across rescan for the same id`() {
        val playedAt = Instant.fromEpochMilliseconds(5_000L)
        val existing = listOf(game("a", lastPlayedAt = playedAt, totalPlayMinutes = 42, playCount = 3, addedAtEpochMillis = 10L))
        val fresh = listOf(game("a", addedAtEpochMillis = 999_999L))

        val result = GameLibraryMerge.mergeFolderScan(existing, folderId = "folder-1", freshGamesForFolder = fresh)

        val merged = result.mergedGames.single()
        assertEquals(playedAt, merged.lastPlayedAt)
        assertEquals(42, merged.totalPlayMinutes)
        assertEquals(3, merged.playCount)
        assertEquals(10L, merged.addedAtEpochMillis)
    }

    @Test
    fun `other folders are untouched by a folder rescan`() {
        val existing = listOf(game("a", folderId = "folder-1"), game("x", folderId = "folder-2"))
        val fresh = listOf(game("a", folderId = "folder-1"))

        val result = GameLibraryMerge.mergeFolderScan(existing, folderId = "folder-1", freshGamesForFolder = fresh)

        assertTrue(result.mergedGames.any { it.id == "x" && it.sourceFolderId == "folder-2" })
    }

    @Test
    fun `removing a folder removes only that folder's games`() {
        val existing = listOf(game("a", folderId = "folder-1"), game("x", folderId = "folder-2"))

        val result = GameLibraryMerge.removeFolder(existing, folderId = "folder-1")

        assertEquals(listOf("x"), result.map { it.id })
    }

    @Test
    fun `favorite id set is independent from merge and never dropped from another folder`() {
        // 즐겨찾기는 FavoritesRepository가 id 기준으로 별도 관리하므로,
        // 병합 결과에 다른 폴더 게임의 id가 그대로 남아 있으면 즐겨찾기 연결이 끊기지 않습니다.
        val existing = listOf(game("a", folderId = "folder-1"), game("fav-x", folderId = "folder-2"))
        val fresh = listOf(game("a", folderId = "folder-1"))

        val result = GameLibraryMerge.mergeFolderScan(existing, folderId = "folder-1", freshGamesForFolder = fresh)

        assertTrue(result.mergedGames.any { it.id == "fav-x" })
    }
}
