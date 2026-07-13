package com.starlight.expedition.core.model

/**
 * 플랫폼 판별 결과의 신뢰도입니다. 근거가 약하면 억지로 확정하지 않고 UNKNOWN을 사용합니다.
 */
enum class ClassificationConfidence {
    HIGH,
    MEDIUM,
    LOW,
    UNKNOWN
}
