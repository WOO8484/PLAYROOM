package com.starlight.expedition.core.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 최근 플레이 시각을 "12분 전", "어제", "3일 전"과 같은 한글 상대 시간 문구로 변환합니다.
 */
object TimeFormat {

    fun relativeKorean(playedAt: Instant, now: Instant): String {
        val diffMinutes = (now - playedAt).inWholeMinutes
        return when {
            diffMinutes < 1 -> "방금 전"
            diffMinutes < 60 -> "${diffMinutes}분 전"
            diffMinutes < 60 * 24 -> "${diffMinutes / 60}시간 전"
            diffMinutes < 60 * 24 * 2 -> "어제"
            else -> "${diffMinutes / (60 * 24)}일 전"
        }
    }

    fun playMinutesLabel(totalMinutes: Int): String {
        if (totalMinutes < 60) {
            return "${totalMinutes}분"
        }
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (minutes == 0) "${hours}시간" else "${hours}시간 ${minutes}분"
    }

    fun toLocalLabel(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "%04d.%02d.%02d".format(local.year, local.monthNumber, local.dayOfMonth)
    }
}
