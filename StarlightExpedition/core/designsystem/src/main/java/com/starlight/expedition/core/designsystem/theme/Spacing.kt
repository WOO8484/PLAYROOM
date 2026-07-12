package com.starlight.expedition.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 화면 공통 간격 토큰입니다.
 */
@Immutable
data class StarlightSpacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 6.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 18.dp,
    val xxl: Dp = 22.dp,
    val screenHorizontal: Dp = 18.dp,
    val headerHeight: Dp = 106.dp,
    val bottomNavHeight: Dp = 78.dp,
    val bottomNavBottomGap: Dp = 12.dp,
    val bottomNavHorizontalMargin: Dp = 16.dp,
    val bottomNavContentExtra: Dp = 24.dp
) {
    /** HTML의 --nav-safe(108px)에 대응하는, 하단 메뉴에 가려지지 않기 위한 최소 여백입니다. */
    val navSafeHeight: Dp
        get() = bottomNavHeight + bottomNavBottomGap + 18.dp

    /**
     * 즐겨찾기·게임리스트처럼 목록만 스크롤되는 화면에서, 목록 콘텐츠 하단에 적용할 패딩입니다.
     * 하단 메뉴 높이 + 메뉴 하단 간격 + 여유 24dp 를 더합니다. 시스템 하단 안전 영역은
     * 화면에서 WindowInsets로 별도로 더합니다.
     */
    val scrollListContentBottomPadding: Dp
        get() = navSafeHeight + bottomNavContentExtra
}

val LocalStarlightSpacing = StarlightSpacing()

/**
 * 화면 기준 너비(393dp)입니다. 600dp 이상 화면에서는 이 너비로 중앙 고정합니다.
 */
val DesignReferenceWidth: Dp = 393.dp
val LargeScreenBreakpoint: Dp = 600.dp
