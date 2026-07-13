package com.starlight.expedition.core.model

data class GameScanSummary(
    val scannedFiles: Int,
    val supportedCandidates: Int,
    val addedGames: Int,
    val updatedGames: Int,
    val removedGames: Int,
    val skippedFiles: Int,
    val unclassifiedFiles: Int,
    val matchedCovers: Int,
    val durationMillis: Long
)
