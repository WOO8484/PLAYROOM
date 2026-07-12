package com.starlight.expedition.core.model

/**
 * 게임을 가리키는 식별자 타입입니다.
 * Repository 계약은 문자열 id를 그대로 사용하므로, 이 타입은
 * 화면 상태나 향후 상세 화면 등 타입 안전성이 필요한 지점에서 선택적으로 사용합니다.
 */
@JvmInline
value class GameId(val value: String)
