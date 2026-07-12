package com.starlight.expedition.core.common

import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatTest {

    @Test
    fun `relativeKorean formats minutes`() {
        val now = Instant.parse("2026-07-13T12:00:00Z")
        val playedAt = Instant.parse("2026-07-13T11:48:00Z")
        assertEquals("12분 전", TimeFormat.relativeKorean(playedAt, now))
    }

    @Test
    fun `playMinutesLabel formats hours and minutes`() {
        assertEquals("45분", TimeFormat.playMinutesLabel(45))
        assertEquals("2시간", TimeFormat.playMinutesLabel(120))
        assertEquals("2시간 5분", TimeFormat.playMinutesLabel(125))
    }
}
