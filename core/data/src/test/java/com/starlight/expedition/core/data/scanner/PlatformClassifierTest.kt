package com.starlight.expedition.core.data.scanner

import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.Platform
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformClassifierTest {

    @Test
    fun `high confidence extensions classify directly`() {
        val gba = PlatformClassifier.classify("Pokemon.gba", parentFolderNames = emptyList())
        assertEquals(Platform.GAME_BOY_ADVANCE, gba.platform)
        assertEquals(ClassificationConfidence.HIGH, gba.confidence)

        val nds = PlatformClassifier.classify("Mario Kart.nds", parentFolderNames = emptyList())
        assertEquals(Platform.NINTENDO_DS, nds.platform)
        assertEquals(ClassificationConfidence.HIGH, nds.confidence)

        val cso = PlatformClassifier.classify("game.cso", parentFolderNames = emptyList())
        assertEquals(Platform.PSP, cso.platform)
        assertEquals(ClassificationConfidence.HIGH, cso.confidence)
    }

    @Test
    fun `iso uses folder hint when available`() {
        val psp = PlatformClassifier.classify("game.iso", parentFolderNames = listOf("PSP"))
        assertEquals(Platform.PSP, psp.platform)

        val ps2 = PlatformClassifier.classify("game.iso", parentFolderNames = listOf("PS2"))
        assertEquals(Platform.PLAYSTATION_2, ps2.platform)
    }

    @Test
    fun `iso without folder hint stays unknown`() {
        val result = PlatformClassifier.classify("game.iso", parentFolderNames = listOf("ROMs"))
        assertEquals(Platform.UNKNOWN, result.platform)
        assertEquals(ClassificationConfidence.LOW, result.confidence)
    }

    @Test
    fun `zip in mame folder is arcade`() {
        val result = PlatformClassifier.classify("rom.zip", parentFolderNames = listOf("MAME"))
        assertEquals(Platform.ARCADE, result.platform)
    }

    @Test
    fun `zip without arcade hint is excluded from auto registration`() {
        val result = PlatformClassifier.classify("rom.zip", parentFolderNames = listOf("Downloads"))
        assertEquals(Platform.UNKNOWN, result.platform)
        assertTrue(PlatformClassifier.isAutoRegisterExcluded("rom.zip", result))
    }

    @Test
    fun `cia is nintendo 3ds and not launchable`() {
        val result = PlatformClassifier.classify("game.cia", parentFolderNames = emptyList())
        assertEquals(Platform.NINTENDO_3DS, result.platform)
        assertFalse(result.launchable)
    }

    @Test
    fun `bin without cue sibling and without genesis hint is unknown low confidence`() {
        val result = PlatformClassifier.classify(
            "track.bin",
            parentFolderNames = listOf("ROMs"),
            hasMatchingCueSibling = false
        )
        assertEquals(Platform.UNKNOWN, result.platform)
    }

    @Test
    fun `bios file name is detected by exclusion rules`() {
        assertTrue(ScanExclusionRules.looksLikeBiosFileName("scph1001.bin"))
    }
}
