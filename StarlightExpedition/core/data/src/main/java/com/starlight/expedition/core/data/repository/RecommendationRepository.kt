package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.Recommendation

interface RecommendationRepository {
    suspend fun recommendNext(
        excludedGameIds: Set<String> = emptySet()
    ): Recommendation
}
