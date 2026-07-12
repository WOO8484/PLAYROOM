package com.playroom.core.data

import com.playroom.core.model.Difficulty
import com.playroom.core.model.Game
import com.playroom.core.model.LevelOption
import com.playroom.core.model.LibraryFilter
import com.playroom.core.model.PlayState
import com.playroom.core.model.PlayTimeCategory
import com.playroom.core.model.PlayerCategory
import com.playroom.core.model.PlayerOption
import com.playroom.core.model.PriorityOption
import com.playroom.core.model.RecommendationOptions
import com.playroom.core.model.RunStatusType
import com.playroom.core.model.TimeOption
import kotlin.random.Random

/**
 * Pure, side-effect-free filter/search/recommendation logic shared by every
 * feature module. Kept as plain functions on [List] so they stay trivial to
 * unit-test on the JVM without touching Android or a [GameRepository].
 */

/** 게임룸 filter chips: 전체 / 즐겨찾기 / 기종. */
fun List<Game>.filterByLibraryFilter(filter: LibraryFilter): List<Game> = when (filter) {
    LibraryFilter.All -> this
    LibraryFilter.Favorites -> filter { it.isFavorite }
    is LibraryFilter.BySystem -> filter { it.system == filter.system }
}

/** Games marked as favorite, in their original order. */
fun List<Game>.favorites(): List<Game> = filter { it.isFavorite }

/** Title or system name search, case-insensitive, blank query returns everything. */
fun List<Game>.search(query: String): List<Game> {
    val normalized = query.trim().lowercase()
    if (normalized.isEmpty()) return this
    return filter { game ->
        game.title.lowercase().contains(normalized) ||
            game.system.label.lowercase().contains(normalized)
    }
}

/** Whether a game can be launched right now without a blocking file/BIOS issue. */
fun Game.isLaunchable(): Boolean = runStatus.type == RunStatusType.READY

/** Keeps only games whose category matches every non-"상관없음" recommendation option. */
fun List<Game>.matchesRecommendationOptions(options: RecommendationOptions): List<Game> = filter { game ->
    game.playTimeCategory.matches(options.time) &&
        game.difficulty.matches(options.level) &&
        game.playerCategory.matches(options.players)
}

private fun PlayTimeCategory.matches(option: TimeOption): Boolean = when (option) {
    TimeOption.ANY -> true
    TimeOption.SHORT -> this == PlayTimeCategory.SHORT
    TimeOption.MEDIUM -> this == PlayTimeCategory.MEDIUM
    TimeOption.LONG -> this == PlayTimeCategory.LONG
}

private fun Difficulty.matches(option: LevelOption): Boolean = when (option) {
    LevelOption.ANY -> true
    LevelOption.EASY -> this == Difficulty.EASY
    LevelOption.NORMAL -> this == Difficulty.NORMAL
    LevelOption.HARD -> this == Difficulty.HARD
}

private fun PlayerCategory.matches(option: PlayerOption): Boolean = when (option) {
    PlayerOption.ANY -> true
    PlayerOption.SOLO -> this == PlayerCategory.SOLO
    PlayerOption.MULTI -> this == PlayerCategory.MULTI
}

private fun priorityScore(game: Game, priority: PriorityOption): Int = when (priority) {
    PriorityOption.NEW_FIRST -> if (game.playState == PlayState.NEW) 0 else 1
    PriorityOption.CONTINUE_FIRST -> if (game.playState == PlayState.CONTINUE) 0 else 1
    PriorityOption.SHORT_FIRST -> if (game.playTimeCategory == PlayTimeCategory.SHORT) 0 else 1
}

/**
 * Picks one random launchable game matching [options], preferring games
 * that satisfy [RecommendationOptions.priority] and avoiding [excludeId]
 * (the game currently shown) when another choice exists. Falls back to the
 * full launchable library if nothing matches the filters, and to `null`
 * only when there is no launchable game at all.
 */
fun List<Game>.pickRecommendation(
    options: RecommendationOptions,
    excludeId: Int?,
    random: Random = Random.Default,
): Game? {
    val launchable = filter { it.runStatus.type == RunStatusType.READY }
    if (launchable.isEmpty()) return null

    val matching = launchable.matchesRecommendationOptions(options)
    val pool = matching.ifEmpty { launchable }
    val withoutCurrent = pool.filterNot { it.id == excludeId }
    val finalPool = withoutCurrent.ifEmpty { pool }

    val bestScore = finalPool.minOf { priorityScore(it, options.priority) }
    val bestGroup = finalPool.filter { priorityScore(it, options.priority) == bestScore }
    return bestGroup[random.nextInt(bestGroup.size)]
}
