package com.starlight.expedition.core.common

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchUtilsTest {

    @Test
    fun `blank query matches everything`() {
        assertTrue(SearchUtils.matches("별빛 모험대", ""))
        assertTrue(SearchUtils.matches("별빛 모험대", "   "))
    }

    @Test
    fun `matches ignores case and surrounding whitespace`() {
        assertTrue(SearchUtils.matches("Hero Legend", "  hero  "))
    }

    @Test
    fun `matches supports korean substrings`() {
        assertTrue(SearchUtils.matches("용사의 전설", "전설"))
        assertFalse(SearchUtils.matches("용사의 전설", "퍼즐"))
    }
}
