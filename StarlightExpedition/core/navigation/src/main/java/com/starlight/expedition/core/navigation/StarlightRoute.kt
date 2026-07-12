package com.starlight.expedition.core.navigation

/**
 * 하단 메뉴 4개 화면의 이동 경로입니다.
 * 확정 GUI 기준 순서: 빠른 시작 → 홈 → 즐겨찾기 → 게임리스트
 */
enum class StarlightRoute(val route: String) {
    QUICK_START("quick_start"),
    HOME("home"),
    FAVORITES("favorites"),
    GAME_LIST("game_list");

    /**
     * 빠른 시작 화면에서는 설정 톱니바퀴를 표시하지 않습니다.
     */
    val showsSettingsButton: Boolean
        get() = this != QUICK_START

    companion object {
        fun fromRoute(route: String?): StarlightRoute =
            entries.firstOrNull { it.route == route } ?: QUICK_START
    }
}
