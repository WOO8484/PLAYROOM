package com.starlight.expedition.core.data.local

import java.io.File
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class GameLibraryFileStoreTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var store: GameLibraryFileStore

    @Before
    fun setUp() {
        store = GameLibraryFileStore(tempFolder.root)
    }

    @Test
    fun `read returns NotFound when file does not exist`() = runBlocking {
        val result = store.read()
        assertTrue(result is GameLibraryFileStore.ReadResult.NotFound)
    }

    @Test
    fun `write then read round trips empty library`() = runBlocking {
        val dto = GameLibraryFileDto(savedAtEpochMillis = 111L, games = emptyList())
        val writeResult = store.write(dto)
        assertTrue(writeResult.isSuccess)

        val result = store.read()
        assertTrue(result is GameLibraryFileStore.ReadResult.Success)
        val success = result as GameLibraryFileStore.ReadResult.Success
        assertEquals(0, success.dto.games.size)
        assertEquals(GameLibraryFileDto.SCHEMA_VERSION, success.dto.schemaVersion)
    }

    @Test
    fun `write then read round trips games`() = runBlocking {
        val game = sampleGameDto()
        val dto = GameLibraryFileDto(savedAtEpochMillis = 222L, games = listOf(game))
        store.write(dto)

        val result = store.read() as GameLibraryFileStore.ReadResult.Success
        assertEquals(1, result.dto.games.size)
        assertEquals(game.id, result.dto.games.single().id)
        assertEquals(game.titleKo, result.dto.games.single().titleKo)
    }

    @Test
    fun `corrupted json is reported without crashing`() = runBlocking {
        val file = File(tempFolder.root, GameLibraryFileStore.FILE_NAME)
        file.writeText("{ this is not valid json", Charsets.UTF_8)

        val result = store.read()
        assertTrue(result is GameLibraryFileStore.ReadResult.Corrupted)
    }

    @Test
    fun `failed write preserves existing file`() = runBlocking {
        val originalDto = GameLibraryFileDto(savedAtEpochMillis = 333L, games = listOf(sampleGameDto()))
        store.write(originalDto)

        // 임시 파일 경로를 디렉터리로 미리 만들어 다음 쓰기가 반드시 실패하도록 강제합니다.
        val tempPath = File(tempFolder.root, "${GameLibraryFileStore.FILE_NAME}.tmp")
        tempPath.mkdirs()

        val brokenWrite = store.write(GameLibraryFileDto(savedAtEpochMillis = 999L, games = emptyList()))
        assertTrue(brokenWrite.isFailure)

        val stillThere = store.read() as GameLibraryFileStore.ReadResult.Success
        assertEquals(1, stillThere.dto.games.size)
        assertEquals(333L, stillThere.dto.savedAtEpochMillis)
    }

    private fun sampleGameDto() = GameDto(
        id = "abc",
        sourceFolderId = "folder-1",
        documentUri = "content://tree/doc1",
        documentId = "doc1",
        fileName = "game.gba",
        titleKo = "game",
        originalTitle = "game",
        normalizedTitle = "game",
        genre = "UNKNOWN",
        platform = "GAME_BOY_ADVANCE",
        platformConfidence = "HIGH",
        classificationReason = "확장자 .gba",
        fileExtension = "gba",
        mimeType = null,
        sizeBytes = 1000,
        modifiedAtEpochMillis = null,
        coverUri = null,
        companionUris = emptyList(),
        isLaunchable = true,
        descriptionLines = emptyList(),
        isFavorite = false,
        addedAtEpochMillis = 1,
        lastSeenAtEpochMillis = 1,
        lastPlayedAtEpochMillis = null,
        totalPlayMinutes = 0,
        playCount = 0,
        progressLabel = null
    )
}
