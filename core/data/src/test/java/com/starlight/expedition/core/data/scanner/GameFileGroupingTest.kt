package com.starlight.expedition.core.data.scanner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameFileGroupingTest {

    private fun entry(
        documentId: String,
        fileName: String,
        parents: List<String> = listOf("ROMs")
    ) = ScannedFileEntry(
        documentId = documentId,
        fileName = fileName,
        parentFolderNames = parents,
        documentUri = "content://tree/$documentId",
        mimeType = null,
        sizeBytes = null,
        modifiedAtEpochMillis = null
    )

    @Test
    fun `cue and bin with matching base name group into one representative cue`() {
        val entries = listOf(
            entry("1", "game.cue"),
            entry("2", "game.bin")
        )

        val groups = GameFileGrouping.group(entries)

        assertEquals(1, groups.size)
        assertEquals("game.cue", groups.single().representative.fileName)
        assertEquals(1, groups.single().companions.size)
        assertEquals("game.bin", groups.single().companions.single().fileName)
    }

    @Test
    fun `gdi and track bin group into one representative gdi`() {
        val entries = listOf(
            entry("1", "game.gdi"),
            entry("2", "track.bin")
        )

        val groups = GameFileGrouping.group(entries)

        assertEquals(1, groups.size)
        assertEquals("game.gdi", groups.single().representative.fileName)
        assertEquals(1, groups.single().companions.size)
    }

    @Test
    fun `m3u with cue discs group into one representative m3u`() {
        val entries = listOf(
            entry("1", "game.m3u"),
            entry("2", "game (Disc 1).cue"),
            entry("3", "game (Disc 2).cue")
        )

        val groups = GameFileGrouping.group(entries)

        assertEquals(1, groups.size)
        assertEquals("game.m3u", groups.single().representative.fileName)
        assertEquals(2, groups.single().companions.size)
    }

    @Test
    fun `different games in same folder are not merged together`() {
        val entries = listOf(
            entry("1", "gameA.cue"),
            entry("2", "gameA.bin"),
            entry("3", "gameB.cue"),
            entry("4", "gameB.bin")
        )

        val groups = GameFileGrouping.group(entries)

        assertEquals(2, groups.size)
        val representativeNames = groups.map { it.representative.fileName }.toSet()
        assertTrue(representativeNames.containsAll(setOf("gameA.cue", "gameB.cue")))
    }

    @Test
    fun `same file name in different folders is kept as separate games`() {
        val entries = listOf(
            entry("1", "game.nes", parents = listOf("ROMs", "SetA")),
            entry("2", "game.nes", parents = listOf("ROMs", "SetB"))
        )

        val groups = GameFileGrouping.group(entries)

        assertEquals(2, groups.size)
    }
}
