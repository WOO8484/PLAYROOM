package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Recommendation

interface RecommendationRepository {
    suspend fun recommendNext(
        games: List<Game>,
        excludedGameIds: Set<String> = emptySet()
    ): Recommendation?
}
