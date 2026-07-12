package com.playroom.core.model

/**
 * A single (fake) game in the PLAYROOM library. All copy is Korean, matching
 * the reference design; this UI-test build never touches real ROM files.
 */
data class Game(
    val id: Int,
    val title: String,
    val system: GameSystem,
    val lastPlayedLabel: String,
    val isFavorite: Boolean,
    val runStatus: RunStatus,
    val difficulty: Difficulty,
    val estimatedPlayTime: String,
    val coreFun: String,
    val recommendedFor: String,
    val summary: String,
    val description: String,
    val beginnerTip: String,
    val playTimeCategory: PlayTimeCategory,
    val playerCategory: PlayerCategory,
    val playState: PlayState,
)
