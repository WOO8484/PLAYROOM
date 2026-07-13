package com.starlight.expedition.core.model

import kotlinx.datetime.Instant

/**
 * 실제 폴더 검색으로 등록되는 게임입니다. `coverResName` 같은 정적 리소스 참조 대신
 * 실제 파일 위치(`documentUri`)와 매칭된 커버(`coverUri`)를 갖습니다.
 */
data class Game(
    val id: String,
    val sourceFolderId: String,
    val documentUri: String,
    val documentId: String?,
    val fileName: String,
    val titleKo: String,
    val originalTitle: String?,
    val normalizedTitle: String,
    val genre: GameGenre,
    val platform: Platform,
    val platformConfidence: ClassificationConfidence,
    val classificationReason: String?,
    val fileExtension: String,
    val mimeType: String?,
    val sizeBytes: Long?,
    val modifiedAtEpochMillis: Long?,
    val coverUri: String?,
    val companionUris: List<String>,
    val isLaunchable: Boolean,
    val descriptionLines: List<String>,
    val isFavorite: Boolean,
    val addedAtEpochMillis: Long,
    val lastSeenAtEpochMillis: Long,
    val lastPlayedAt: Instant?,
    val totalPlayMinutes: Int,
    val playCount: Int,
    val progressLabel: String?
)
