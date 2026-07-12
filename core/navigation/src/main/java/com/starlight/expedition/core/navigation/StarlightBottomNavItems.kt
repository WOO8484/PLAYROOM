package com.starlight.expedition.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.List
import com.starlight.expedition.core.designsystem.component.BottomNavEntry

/**
 * 하단 메뉴 4개 항목의 라벨·아이콘 정의입니다.
 */
val starlightBottomNavEntries: List<BottomNavEntry> = listOf(
    BottomNavEntry(
        route = StarlightRoute.QUICK_START.route,
        label = "빠른 시작",
        icon = Icons.Filled.Bolt,
        contentDescription = "빠른 시작으로 이동"
    ),
    BottomNavEntry(
        route = StarlightRoute.HOME.route,
        label = "홈",
        icon = Icons.Filled.Home,
        contentDescription = "홈으로 이동"
    ),
    BottomNavEntry(
        route = StarlightRoute.FAVORITES.route,
        label = "즐겨찾기",
        icon = Icons.Filled.Star,
        contentDescription = "즐겨찾기로 이동"
    ),
    BottomNavEntry(
        route = StarlightRoute.GAME_LIST.route,
        label = "게임리스트",
        icon = Icons.AutoMirrored.Filled.List,
        contentDescription = "게임리스트로 이동"
    )
)
