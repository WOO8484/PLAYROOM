package com.playroom.core.data

/**
 * Minimal manual dependency access point (Hilt is not allowed in this
 * build). Every feature ViewModel reads the same singleton repository
 * through this object instead of constructing its own instance.
 */
object GameRepositoryProvider {
    val repository: GameRepository by lazy { InMemoryGameRepository() }
}
