package com.starlight.expedition.core.model

/**
 * 로컬 커버 자동 매칭 단계에서 게임과 이미지 파일을 연결한 결과입니다.
 * 매칭 우선순위가 높을수록 [rank] 값이 작습니다(0이 가장 신뢰도 높음).
 */
data class GameCoverMatch(
    val gameId: String,
    val coverUri: String,
    val rank: Int
)
