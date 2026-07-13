package com.starlight.expedition.core.data.repository

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.Recommendation
import com.starlight.expedition.core.model.RecommendationReason

/**
 * "오늘 뭐 하지?" 카드의 랜덤 추천입니다. 실제 등록 게임만 사용하며,
 * 고정 목록(SampleGameData)을 참조하지 않습니다.
 */
class RecommendationRepositoryImpl : RecommendationRepository {

    override suspend fun recommendNext(games: List<Game>, excludedGameIds: Set<String>): Recommendation? {
        // 설치 패키지(isLaunchable = false)는 우선 후보에서 제외하되, 그것만 남아 있다면 그대로 사용합니다.
        val launchablePool = games.filter { it.isLaunchable }.ifEmpty { games }
        val candidates = launchablePool.filter { it.id !in excludedGameIds }.ifEmpty { launchablePool }
        val picked = candidates.randomOrNull() ?: return null
        return Recommendation(game = picked, reason = RecommendationReason.RANDOM_PICK)
    }
}
