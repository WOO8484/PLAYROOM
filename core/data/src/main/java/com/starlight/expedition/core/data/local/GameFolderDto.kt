package com.starlight.expedition.core.data.local

import kotlinx.serialization.Serializable

/**
 * DataStore에 JSON 문자열로 저장하는 등록 폴더 목록입니다(실행지시서 22.1절).
 */
@Serializable
data class GameFolderListDto(
    val folders: List<GameFolderDto> = emptyList()
)

@Serializable
data class GameFolderDto(
    val id: String,
    val treeUri: String,
    val displayName: String,
    val addedAtEpochMillis: Long,
    val lastScannedAtEpochMillis: Long? = null,
    val lastKnownGameCount: Int = 0,
    val enabled: Boolean = true
)
