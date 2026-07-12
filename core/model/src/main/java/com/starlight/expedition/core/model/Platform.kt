package com.starlight.expedition.core.model

/**
 * 1차 개발에서는 실제 실행 기능이 없으므로 표시용 정보로만 사용합니다.
 */
enum class Platform(val displayName: String) {
    NATIVE("자체 게임"),
    RETRO("레트로 콘솔"),
    PORTABLE("휴대용 콘솔")
}
