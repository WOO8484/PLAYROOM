package com.playroom.app

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.playroom.app.navigation.PlayroomNavHost
import com.playroom.app.navigation.navigateToTopLevelDestination
import com.playroom.core.designsystem.component.LocalPlayroomSnackbarHostState
import com.playroom.core.designsystem.component.PlayroomBottomBar
import com.playroom.core.designsystem.component.PlayroomBottomBarItem
import com.playroom.core.designsystem.component.PlayroomTopBar
import com.playroom.core.model.AppSkin
import com.playroom.core.navigation.PlayroomDestinations
import com.playroom.core.navigation.TopLevelDestination

private val TOP_LEVEL_ROUTES = TopLevelDestination.entries.map { it.route }.toSet()

private fun iconFor(destination: TopLevelDestination) = when (destination) {
    TopLevelDestination.HOME -> Icons.Filled.Home
    TopLevelDestination.LIBRARY -> Icons.Filled.SportsEsports
    TopLevelDestination.FAVORITES -> Icons.Filled.Star
    TopLevelDestination.SETTINGS -> Icons.Filled.Settings
}

@Composable
fun MainScaffold(
    currentSkin: AppSkin,
    onSkinSelected: (AppSkin) -> Unit,
    onReplayOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showChrome = currentRoute in TOP_LEVEL_ROUTES

    // 뒤로가기 우선순위 5: 메인이 아니면 메인으로. Player/Search each own a
    // higher-priority BackHandler while they're on screen, so this one only
    // ever fires for the 4 bottom-tab routes — and does nothing at all on
    // 메인, letting the system's default "finish activity" behavior run
    // (우선순위 6, "메인이면 앱 종료").
    BackHandler(enabled = currentRoute != null && currentRoute in TOP_LEVEL_ROUTES && currentRoute != PlayroomDestinations.HOME_ROUTE) {
        navController.navigateToTopLevelDestination(TopLevelDestination.HOME)
    }

    CompositionLocalProvider(LocalPlayroomSnackbarHostState provides snackbarHostState) {
        Scaffold(
            modifier = modifier,
            topBar = {
                if (showChrome) {
                    PlayroomTopBar(onSearchClick = { navController.navigate(PlayroomDestinations.SEARCH_ROUTE) })
                }
            },
            bottomBar = {
                if (showChrome) {
                    PlayroomBottomBar(
                        items = TopLevelDestination.entries.map { destination ->
                            PlayroomBottomBarItem(
                                label = destination.label,
                                icon = iconFor(destination),
                                selected = currentRoute == destination.route,
                                onClick = { navController.navigateToTopLevelDestination(destination) },
                            )
                        },
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
        ) { innerPadding ->
            PlayroomNavHost(
                navController = navController,
                currentSkin = currentSkin,
                onSkinSelected = onSkinSelected,
                onReplayOnboarding = onReplayOnboarding,
                contentPadding = innerPadding,
            )
        }
    }
}
