package com.starlight.expedition.core.model

/**
 * 게임리스트 화면의 장르 필터 칩과 동일한 값을 사용합니다.
 * 폴더 검색만으로는 장르를 추측하지 않으므로, 실제 검색으로 등록된 게임은
 * 대부분 UNKNOWN을 갖습니다.
 */
enum class GameGenre(val displayName: String) {
    RPG("RPG"),
    PUZZLE("퍼즐"),
    ACTION("액션"),
    CASUAL("캐주얼"),
    UNKNOWN("장르 미분류")
}
