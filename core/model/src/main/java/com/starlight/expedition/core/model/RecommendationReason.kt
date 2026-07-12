package com.starlight.expedition.core.model

/**
 * "오늘 뭐 하지?" 카드에 노출되는 추천 사유입니다.
 * 1차 개발에서는 사유를 화면에 직접 노출하지 않지만,
 * 추천 로직과 향후 확장을 위해 함께 정의합니다.
 */
enum class RecommendationReason {
    RANDOM_PICK,
    NOT_PLAYED_RECENTLY,
    SHORT_SESSION,
    LONG_SESSION,
    FAVORITE_GENRE
}
