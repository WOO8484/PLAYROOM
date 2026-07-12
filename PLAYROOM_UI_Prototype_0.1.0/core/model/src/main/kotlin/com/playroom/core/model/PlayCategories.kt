package com.playroom.core.model

/** Rough play-session length used by search filters and recommendations. */
enum class PlayTimeCategory {
    SHORT,
    MEDIUM,
    LONG,
}

/** Whether a game is best enjoyed solo or with a second player. */
enum class PlayerCategory {
    SOLO,
    MULTI,
}

/** Whether the player has an existing save for this game. */
enum class PlayState {
    NEW,
    CONTINUE,
}
