package com.starlight.expedition.core.data.local

import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.FolderPermissionState
import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre
import com.starlight.expedition.core.model.GameSourceFolder
import com.starlight.expedition.core.model.Platform
import kotlinx.datetime.Instant

fun GameDto.toDomain(): Game = Game(
    id = id,
    sourceFolderId = sourceFolderId,
    documentUri = documentUri,
    documentId = documentId,
    fileName = fileName,
    titleKo = titleKo,
    originalTitle = originalTitle,
    normalizedTitle = normalizedTitle,
    genre = runCatching { GameGenre.valueOf(genre) }.getOrDefault(GameGenre.UNKNOWN),
    platform = runCatching { Platform.valueOf(platform) }.getOrDefault(Platform.UNKNOWN),
    platformConfidence = runCatching { ClassificationConfidence.valueOf(platformConfidence) }
        .getOrDefault(ClassificationConfidence.UNKNOWN),
    classificationReason = classificationReason,
    fileExtension = fileExtension,
    mimeType = mimeType,
    sizeBytes = sizeBytes,
    modifiedAtEpochMillis = modifiedAtEpochMillis,
    coverUri = coverUri,
    companionUris = companionUris,
    isLaunchable = isLaunchable,
    descriptionLines = descriptionLines,
    isFavorite = isFavorite,
    addedAtEpochMillis = addedAtEpochMillis,
    lastSeenAtEpochMillis = lastSeenAtEpochMillis,
    lastPlayedAt = lastPlayedAtEpochMillis?.let { Instant.fromEpochMilliseconds(it) },
    totalPlayMinutes = totalPlayMinutes,
    playCount = playCount,
    progressLabel = progressLabel
)

fun Game.toDto(): GameDto = GameDto(
    id = id,
    sourceFolderId = sourceFolderId,
    documentUri = documentUri,
    documentId = documentId,
    fileName = fileName,
    titleKo = titleKo,
    originalTitle = originalTitle,
    normalizedTitle = normalizedTitle,
    genre = genre.name,
    platform = platform.name,
    platformConfidence = platformConfidence.name,
    classificationReason = classificationReason,
    fileExtension = fileExtension,
    mimeType = mimeType,
    sizeBytes = sizeBytes,
    modifiedAtEpochMillis = modifiedAtEpochMillis,
    coverUri = coverUri,
    companionUris = companionUris,
    isLaunchable = isLaunchable,
    descriptionLines = descriptionLines,
    isFavorite = isFavorite,
    addedAtEpochMillis = addedAtEpochMillis,
    lastSeenAtEpochMillis = lastSeenAtEpochMillis,
    lastPlayedAtEpochMillis = lastPlayedAt?.toEpochMilliseconds(),
    totalPlayMinutes = totalPlayMinutes,
    playCount = playCount,
    progressLabel = progressLabel
)

fun GameFolderDto.toDomain(permissionState: FolderPermissionState, lastErrorMessage: String?): GameSourceFolder =
    GameSourceFolder(
        id = id,
        treeUri = treeUri,
        displayName = displayName,
        addedAtEpochMillis = addedAtEpochMillis,
        lastScannedAtEpochMillis = lastScannedAtEpochMillis,
        lastKnownGameCount = lastKnownGameCount,
        enabled = enabled,
        permissionState = permissionState,
        lastErrorMessage = lastErrorMessage
    )

fun GameSourceFolder.toDto(): GameFolderDto = GameFolderDto(
    id = id,
    treeUri = treeUri,
    displayName = displayName,
    addedAtEpochMillis = addedAtEpochMillis,
    lastScannedAtEpochMillis = lastScannedAtEpochMillis,
    lastKnownGameCount = lastKnownGameCount,
    enabled = enabled
)
