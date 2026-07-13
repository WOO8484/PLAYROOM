package com.starlight.expedition.core.model

/**
 * 사용자가 등록한 게임 폴더입니다. Android `Uri` 타입을 직접 노출하지 않고
 * 문자열로만 다룹니다.
 */
data class GameSourceFolder(
    val id: String,
    val treeUri: String,
    val displayName: String,
    val addedAtEpochMillis: Long,
    val lastScannedAtEpochMillis: Long?,
    val lastKnownGameCount: Int,
    val enabled: Boolean,
    val permissionState: FolderPermissionState,
    val lastErrorMessage: String?
)
