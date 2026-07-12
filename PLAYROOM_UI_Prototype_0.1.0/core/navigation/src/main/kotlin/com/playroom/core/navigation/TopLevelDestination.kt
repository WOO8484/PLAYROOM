package com.playroom.core.navigation

/** The 4 bottom-navigation destinations, in display order. */
enum class TopLevelDestination(val route: String, val label: String) {
    HOME(PlayroomDestinations.HOME_ROUTE, "메인"),
    LIBRARY(PlayroomDestinations.LIBRARY_ROUTE, "게임룸"),
    FAVORITES(PlayroomDestinations.FAVORITES_ROUTE, "즐겨찾기"),
    SETTINGS(PlayroomDestinations.SETTINGS_ROUTE, "설정"),
}
