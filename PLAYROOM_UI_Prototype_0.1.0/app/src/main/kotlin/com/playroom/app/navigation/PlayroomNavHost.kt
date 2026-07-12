package com.playroom.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.playroom.core.model.AppSkin
import com.playroom.core.navigation.PlayroomDestinations
import com.playroom.core.navigation.TopLevelDestination
import com.playroom.feature.favorites.FavoritesRoute
import com.playroom.feature.home.HomeRoute
import com.playroom.feature.library.LibraryRoute
import com.playroom.feature.player.PlayerRoute
import com.playroom.feature.search.SearchRoute
import com.playroom.feature.settings.SettingsRoute

private const val GAME_ID_ARG = "gameId"

/**
 * Every screen the app can show, wired together. Feature modules never call
 * each other directly — all cross-screen navigation is expressed as plain
 * lambdas resolved here, the only place that knows about every route.
 */
@Composable
fun PlayroomNavHost(
    navController: NavHostController,
    currentSkin: AppSkin,
    onSkinSelected: (AppSkin) -> Unit,
    onReplayOnboarding: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = PlayroomDestinations.HOME_ROUTE,
        modifier = modifier,
    ) {
        composable(PlayroomDestinations.HOME_ROUTE) {
            HomeRoute(
                onNavigateToPlayer = { gameId -> navController.navigate(PlayroomDestinations.playerRoute(gameId)) },
                contentPadding = contentPadding,
            )
        }

        composable(PlayroomDestinations.LIBRARY_ROUTE) {
            LibraryRoute(
                onNavigateToPlayer = { gameId -> navController.navigate(PlayroomDestinations.playerRoute(gameId)) },
                contentPadding = contentPadding,
            )
        }

        composable(PlayroomDestinations.FAVORITES_ROUTE) {
            FavoritesRoute(
                onNavigateToPlayer = { gameId -> navController.navigate(PlayroomDestinations.playerRoute(gameId)) },
                contentPadding = contentPadding,
            )
        }

        composable(PlayroomDestinations.SETTINGS_ROUTE) {
            SettingsRoute(
                currentSkin = currentSkin,
                onSkinSelected = onSkinSelected,
                onManageGameFolder = { navController.navigateToTopLevelDestination(TopLevelDestination.LIBRARY) },
                onReplayOnboarding = onReplayOnboarding,
                contentPadding = contentPadding,
            )
        }

        composable(PlayroomDestinations.SEARCH_ROUTE) {
            SearchRoute(
                onNavigateToPlayer = { gameId -> navController.navigate(PlayroomDestinations.playerRoute(gameId)) },
                onClose = { navController.popBackStack() },
            )
        }

        composable(
            route = PlayroomDestinations.PLAYER_ROUTE_PATTERN,
            arguments = listOf(navArgument(GAME_ID_ARG) { type = NavType.IntType }),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt(GAME_ID_ARG) ?: return@composable
            PlayerRoute(
                gameId = gameId,
                onExit = { navController.popBackStack() },
            )
        }
    }
}
