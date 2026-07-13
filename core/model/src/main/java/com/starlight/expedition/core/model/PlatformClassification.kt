package com.starlight.expedition.core.model

/**
 * 플랫폼 판별기가 반환하는 결과입니다. 플랫폼만 반환하지 않고
 * 신뢰도·근거·실행 가능 여부를 함께 반환합니다.
 */
data class PlatformClassification(
    val platform: Platform,
    val confidence: ClassificationConfidence,
    val reason: String,
    val launchable: Boolean
)
