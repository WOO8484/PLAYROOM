package com.playroom.core.designsystem.component

import androidx.annotation.DrawableRes
import com.playroom.core.designsystem.R

/**
 * This UI-test build ships exactly 2 pieces of bundled cover art (see
 * `res/drawable/card_art_*`). Every screen that needs a game's cover image
 * goes through this single mapping instead of repeating game-id checks.
 */
@DrawableRes
fun coverImageResFor(gameId: Int): Int? = when (gameId) {
    1 -> R.drawable.card_art_starlight
    2 -> R.drawable.card_art_hero
    else -> null
}
