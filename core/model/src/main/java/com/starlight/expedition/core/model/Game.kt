package com.starlight.expedition.core.model

import kotlinx.datetime.Instant

data class Game(
    val id: String,
    val titleKo: String,
    val originalTitle: String?,
    val genre: GameGenre,
    val platform: Platform,
    val descriptionLines: List<String>,
    val coverResName: String,
    val isFavorite: Boolean,
    val lastPlayedAt: Instant?,
    val totalPlayMinutes: Int,
    val playCount: Int,
    val progressLabel: String?
)
