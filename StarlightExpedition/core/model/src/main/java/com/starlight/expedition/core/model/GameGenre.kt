package com.starlight.expedition.core.model

/**
 * 게임리스트 화면의 장르 필터 칩과 동일한 값을 사용합니다.
 */
enum class GameGenre(val displayName: String) {
    RPG("RPG"),
    PUZZLE("퍼즐"),
    ACTION("액션"),
    CASUAL("캐주얼")
}
