package com.playroom.core.designsystem.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Every feature screen shows toast-like confirmations ("게임 8개를 찾았습니다",
 * "즐겨찾기에 추가했습니다", ...) through the single Scaffold-level
 * SnackbarHost that `:app` owns. Provided via CompositionLocal so feature
 * modules never need their own Scaffold/SnackbarHost.
 */
val LocalPlayroomSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided — wrap content in the app-level Scaffold first.")
}
