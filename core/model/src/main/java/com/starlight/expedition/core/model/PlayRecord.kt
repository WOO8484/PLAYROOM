package com.starlight.expedition.core.model

import kotlinx.datetime.Instant

/**
 * 홈 화면의 "최근 플레이" 목록에 표시되는 요약 정보입니다.
 */
data class PlayRecord(
    val gameId: String,
    val titleKo: String,
    val coverResName: String,
    val playedAt: Instant,
    val relativeTimeLabel: String,
    val progressLabel: String
)
