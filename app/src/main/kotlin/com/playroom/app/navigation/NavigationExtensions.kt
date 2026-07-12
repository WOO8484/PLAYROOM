package com.playroom.app.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.playroom.core.navigation.TopLevelDestination

/** Bottom-tab switch: single top, restores previous scroll/filter state per tab. */
fun NavHostController.navigateToTopLevelDestination(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
