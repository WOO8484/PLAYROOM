package com.playroom.core.designsystem.theme

import androidx.compose.ui.unit.dp

/** Shared dp tokens. All sizes in this app come from here — no raw dp literals in screens. */
object PlayroomSpacing {
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val SmallMedium = 10.dp
    val Medium = 14.dp
    val Large = 18.dp
    val ExtraLarge = 24.dp
    val Huge = 32.dp

    /** Horizontal screen padding for compact widths (360dp~399dp). */
    val ScreenHorizontalCompact = 14.dp

    /** Horizontal screen padding for normal widths (400dp~599dp). */
    val ScreenHorizontalRegular = 16.dp

    /** Card grid gap for compact widths. */
    val CardGapCompact = 10.dp

    /** Card grid gap for normal widths. */
    val CardGapRegular = 12.dp

    /** Max content width on tablets (600dp+), centered. */
    val MaxContentWidth = 520.dp

    val MinTouchTarget = 44.dp
    val BottomBarHeight = 72.dp
    val TopBarHeight = 62.dp
}

object PlayroomCorner {
    val Small = 13.dp
    val Medium = 17.dp
    val Large = 26.dp
    val ExtraLarge = 30.dp
    val Pill = 100.dp
}
