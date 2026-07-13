package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.data.local.GameLibraryFileStore
import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre
import com.starlight.expedition.core.model.Platform
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

private class FakeFavoritesRepository(
    initialFavorites: Set<String>
) : FavoritesRepository {

    private val favorites = MutableStateFlow(initialFavorites)

    override fun observeFavoriteIds() = favorites

    override suspend fun setFavorite(gameId: String, favorite: Boolean) {
        favorites.value = if (favorite) favorites.value + gameId else favorites.value - gameId
    }
}

class GameRepositoryImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun game(id: String, lastPlayedAt: Instant? = null) = Game(
        id = id,
        sourceFolderId = "folder-1",
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
        addedAtEpochMillis = 1L,
        lastSeenAtEpochMillis = 1L,
        lastPlayedAt = lastPlayedAt,
        totalPlayMinutes = 0,
        playCount = 0,
        progressLabel = null
    )

    private suspend fun newLoadedRepository(favorites: FavoritesRepository, scope: CoroutineScope): GameRepositoryImpl {
        val store = GameLibraryFileStore(File(tempFolder.newFolder(), "library"))
        val repository = GameRepositoryImpl(store, favorites, scope)
        // 초기 파일 로드가 끝난 뒤에만 replaceAllGames를 호출해야, 늦게 끝나는 초기 로드가
        // 테스트에서 방금 넣은 값을 되돌려 덮어쓰는 경쟁 상태를 피할 수 있습니다.
        repository.isLoaded.first { it }
        return repository
    }

    @Test
    fun `observeGames combines library with favorites repository`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(setOf("a"))
        val repository = newLoadedRepository(fakeFavorites, this)
        repository.replaceAllGames(listOf(game("a"), game("b")))

        val games = repository.observeGames().first()

        assertTrue(games.first { it.id == "a" }.isFavorite)
        assertTrue(!games.first { it.id == "b" }.isFavorite)
    }

    @Test
    fun `setFavorite toggles state through favorites repository`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(emptySet())
        val repository = newLoadedRepository(fakeFavorites, this)
        repository.replaceAllGames(listOf(game("a")))

        repository.setFavorite("a", true)

        val games = repository.observeGames().first()
        assertTrue(games.first { it.id == "a" }.isFavorite)
    }

    @Test
    fun `observeRecentGames returns at most requested limit sorted by last played`() = runBlocking {
        val now = Instant.fromEpochMilliseconds(10_000L)
        val earlier = Instant.fromEpochMilliseconds(5_000L)
        val fakeFavorites = FakeFavoritesRepository(emptySet())
        val repository = newLoadedRepository(fakeFavorites, this)
        repository.replaceAllGames(
            listOf(
                game("a", lastPlayedAt = now),
                game("b", lastPlayedAt = earlier),
                game("c", lastPlayedAt = null)
            )
        )

        val recent = repository.observeRecentGames(limit = 3).first()

        assertEquals(2, recent.size)
        assertEquals(recent, recent.sortedByDescending { it.lastPlayedAt })
    }

    @Test
    fun `replaceAllGames persists through GameLibraryFileStore`() = runBlocking {
        val fakeFavorites = FakeFavoritesRepository(emptySet())
        val store = GameLibraryFileStore(File(tempFolder.newFolder(), "library"))
        val repository = GameRepositoryImpl(store, fakeFavorites, this)
        repository.isLoaded.first { it }

        repository.replaceAllGames(listOf(game("a")))

        val readBack = store.read()
        assertTrue(readBack is GameLibraryFileStore.ReadResult.Success)
        val success = readBack as GameLibraryFileStore.ReadResult.Success
        assertEquals(1, success.dto.games.size)
        assertEquals("a", success.dto.games.single().id)
    }
}
