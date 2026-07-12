package com.playroom.core.data

import com.playroom.core.model.GameSystem
import com.playroom.core.model.LevelOption
import com.playroom.core.model.LibraryFilter
import com.playroom.core.model.PlayerOption
import com.playroom.core.model.PriorityOption
import com.playroom.core.model.RecommendationOptions
import com.playroom.core.model.TimeOption
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameQueriesTest {

    private val games = FakeGameData.games

    // 1. 즐겨찾기 필터
    @Test
    fun `library filter favorites returns only favorited games`() {
        val result = games.filterByLibraryFilter(LibraryFilter.Favorites)

        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.isFavorite })
        assertEquals(games.count { it.isFavorite }, result.size)
    }

    // 2. 기종 필터
    @Test
    fun `library filter by system returns only that system`() {
        val result = games.filterByLibraryFilter(LibraryFilter.BySystem(GameSystem.SNES))

        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.system == GameSystem.SNES })
    }

    // 3. 검색
    @Test
    fun `search matches by title and by system, ignores case`() {
        val byTitle = games.search("별빛")
        assertEquals(1, byTitle.size)
        assertEquals("별빛 모험대", byTitle.first().title)

        val bySystem = games.search("snes")
        assertTrue(bySystem.isNotEmpty())
        assertTrue(bySystem.all { it.system == GameSystem.SNES })

        val blank = games.search("   ")
        assertEquals(games.size, blank.size)

        val noMatch = games.search("존재하지않는게임")
        assertTrue(noMatch.isEmpty())
    }

    // 4. 추천 옵션 필터
    @Test
    fun `recommendation options filter keeps only matching games`() {
        val options = RecommendationOptions(
            time = TimeOption.SHORT,
            level = LevelOption.ANY,
            players = PlayerOption.MULTI,
            priority = PriorityOption.NEW_FIRST,
        )

        val result = games.matchesRecommendationOptions(options)

        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.playTimeCategory == com.playroom.core.model.PlayTimeCategory.SHORT })
        assertTrue(result.all { it.playerCategory == com.playroom.core.model.PlayerCategory.MULTI })
    }

    @Test
    fun `pickRecommendation never returns a game that is not launchable`() {
        val fixedRandom = Random(42)
        val options = RecommendationOptions()

        repeat(20) {
            val picked = games.pickRecommendation(options, excludeId = null, random = fixedRandom)
            assertTrue(picked != null)
            assertTrue(picked!!.runStatus.type == com.playroom.core.model.RunStatusType.READY)
        }
    }

    @Test
    fun `pickRecommendation avoids the excluded game when an alternative exists`() {
        val fixedRandom = Random(7)
        val options = RecommendationOptions(
            time = TimeOption.ANY,
            level = LevelOption.ANY,
            players = PlayerOption.ANY,
            priority = PriorityOption.NEW_FIRST,
        )

        repeat(20) {
            val picked = games.pickRecommendation(options, excludeId = 2, random = fixedRandom)
            assertTrue(picked != null && picked.id != 2)
        }
    }
}
