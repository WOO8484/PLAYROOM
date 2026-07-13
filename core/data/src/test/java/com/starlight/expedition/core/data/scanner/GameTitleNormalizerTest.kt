package com.starlight.expedition.core.data.scanner

import org.junit.Assert.assertEquals
import org.junit.Test

class GameTitleNormalizerTest {

    @Test
    fun `removes region and disc tags`() {
        assertEquals(
            "Final Fantasy VII",
            GameTitleNormalizer.normalize("Final Fantasy VII (USA) (Disc 1).cue")
        )
    }

    @Test
    fun `preserves korean title while removing region tag`() {
        assertEquals(
            "포켓몬스터 골드",
            GameTitleNormalizer.normalize("포켓몬스터 골드 (Korea).gbc")
        )
    }

    @Test
    fun `removes verification and revision tags`() {
        assertEquals(
            "Game Name",
            GameTitleNormalizer.normalize("Game Name [!] (Rev A).sfc")
        )
    }

    @Test
    fun `keeps plain title unchanged`() {
        assertEquals("Chrono Trigger", GameTitleNormalizer.normalize("Chrono Trigger.sfc"))
    }
}
