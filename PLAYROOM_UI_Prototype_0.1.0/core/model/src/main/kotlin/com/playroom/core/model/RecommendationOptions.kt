package com.playroom.core.model

enum class TimeOption(val label: String) {
    ANY("상관없음"),
    SHORT("20분 이내"),
    MEDIUM("30~60분"),
    LONG("1시간 이상"),
}

enum class LevelOption(val label: String) {
    ANY("상관없음"),
    EASY("쉬운 게임"),
    NORMAL("보통 난이도"),
    HARD("도전적인 게임"),
}

enum class PlayerOption(val label: String) {
    ANY("상관없음"),
    SOLO("혼자"),
    MULTI("둘이 함께"),
}

enum class PriorityOption(val label: String) {
    NEW_FIRST("안 해본 게임 우선"),
    CONTINUE_FIRST("플레이 중 우선"),
    SHORT_FIRST("짧은 게임 우선"),
}

/** Filters applied by the "추천 옵션" popup before picking a random game. */
data class RecommendationOptions(
    val time: TimeOption = TimeOption.MEDIUM,
    val level: LevelOption = LevelOption.EASY,
    val players: PlayerOption = PlayerOption.ANY,
    val priority: PriorityOption = PriorityOption.NEW_FIRST,
)
