package com.playroom.core.navigation

/**
 * Route contract shared by `:app` and every feature module. Feature modules
 * never reference each other's screens directly — they navigate only
 * through these string routes, wired together by `:app`.
 */
object PlayroomDestinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val HOME_ROUTE = "home"
    const val LIBRARY_ROUTE = "library"
    const val FAVORITES_ROUTE = "favorites"
    const val SEARCH_ROUTE = "search"
    const val SETTINGS_ROUTE = "settings"
    const val PLAYER_ROUTE_PATTERN = "player/{gameId}"

    fun playerRoute(gameId: Int): String = "player/$gameId"
}
