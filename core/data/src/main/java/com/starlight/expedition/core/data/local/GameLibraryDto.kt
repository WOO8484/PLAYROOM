package com.starlight.expedition.core.data.local

import kotlinx.serialization.Serializable

/**
 * `filesDir/starlight/game_library_v1.json`에 저장하는 게임 목록 파일의 최상위 구조입니다(실행지시서 22.2절).
 * 화면에서 쓰는 도메인 모델(`Game`)과 분리된 저장 전용 DTO입니다. `Instant`는 epoch millis로 저장합니다.
 */
@Serializable
data class GameLibraryFileDto(
    val schemaVersion: Int = SCHEMA_VERSION,
    val savedAtEpochMillis: Long = 0,
    val games: List<GameDto> = emptyList()
) {
    companion object {
        const val SCHEMA_VERSION = 1
    }
}

@Serializable
data class GameDto(
    val id: String,
    val sourceFolderId: String,
    val documentUri: String,
    val documentId: String? = null,
    val fileName: String,
    val titleKo: String,
    val originalTitle: String? = null,
    val normalizedTitle: String,
    val genre: String,
    val platform: String,
    val platformConfidence: String,
    val classificationReason: String? = null,
    val fileExtension: String,
    val mimeType: String? = null,
    val sizeBytes: Long? = null,
    val modifiedAtEpochMillis: Long? = null,
    val coverUri: String? = null,
    val companionUris: List<String> = emptyList(),
    val isLaunchable: Boolean = true,
    val descriptionLines: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val addedAtEpochMillis: Long,
    val lastSeenAtEpochMillis: Long,
    val lastPlayedAtEpochMillis: Long? = null,
    val totalPlayMinutes: Int = 0,
    val playCount: Int = 0,
    val progressLabel: String? = null
)
