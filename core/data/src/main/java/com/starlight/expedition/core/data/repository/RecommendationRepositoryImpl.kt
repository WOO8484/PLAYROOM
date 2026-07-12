package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.data.local.SampleGameData
import com.starlight.expedition.core.model.Recommendation
import com.starlight.expedition.core.model.RecommendationReason

/**
 * "오늘 뭐 하지?" 카드의 랜덤 추천을 제공합니다.
 * 이어 하기 카드에 이미 사용 중인 게임은 추천 후보에서 제외합니다.
 */
class RecommendationRepositoryImpl : RecommendationRepository {

    private val pool = SampleGameData.games.filter { it.id != SampleGameData.CONTINUE_GAME_ID }

    override suspend fun recommendNext(excludedGameIds: Set<String>): Recommendation {
        val candidates = pool.filter { it.id !in excludedGameIds }.ifEmpty { pool }
        val picked = candidates.random()
        return Recommendation(game = picked, reason = RecommendationReason.RANDOM_PICK)
    }
}
