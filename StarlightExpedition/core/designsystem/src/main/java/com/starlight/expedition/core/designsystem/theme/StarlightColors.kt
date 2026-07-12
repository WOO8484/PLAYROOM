package com.starlight.expedition.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * HTML GUI(v7.4)의 CSS 커스텀 프로퍼티를 그대로 옮긴 색상 토큰입니다.
 * 화면 코드에서는 이 토큰만 사용하고, 임의 색상값을 직접 작성하지 않습니다.
 */
@Immutable
data class StarlightColors(
    val appBackground: Color,
    val windowBackground: Color,
    val surface: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val line: Color,
    val primary: Color,
    val primaryVariant: Color,
    val primarySoft: Color,
    val onPrimary: Color,
    val cardGradientStart: Color,
    val cardGradientMid: Color,
    val cardGradientEnd: Color,
    val cardBorder: Color,
    val thumbnailGradientStart: Color,
    val thumbnailGradientEnd: Color,
    val navBackground: Color,
    val navBorder: Color,
    val navIconInactive: Color,
    val favoriteActive: Color,
    val favoriteInactive: Color,
    val switchTrackOff: Color,
    val cardActionBackground: Color,
    val cardActionText: Color,
    val cardActionRandomBackground: Color,
    val cardActionRandomText: Color,
    val emptyBackground: Color,
    val emptyBorder: Color,
    val chipBorder: Color,
    val chipText: Color
)

val LightStarlightColors = StarlightColors(
    appBackground = Color(0xFFF4F6FF),
    windowBackground = Color(0xFFEEF1FB),
    surface = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF171A2A),
    textMuted = Color(0xFF747B8F),
    line = Color(0x1A292E4C),
    primary = Color(0xFF7045F6),
    primaryVariant = Color(0xFF8B67FF),
    primarySoft = Color(0x1C7045F6),
    onPrimary = Color(0xFFFFFFFF),
    cardGradientStart = Color(0xFFF5F2FF),
    cardGradientMid = Color(0xFFF0F0FF),
    cardGradientEnd = Color(0xFFE5F8F6),
    cardBorder = Color(0xEBFFFFFF),
    thumbnailGradientStart = Color(0xFFEEEAFF),
    thumbnailGradientEnd = Color(0xFFDCF8F5),
    navBackground = Color(0xD6FFFFFF),
    navBorder = Color(0xEBFFFFFF),
    navIconInactive = Color(0xFF858B9D),
    favoriteActive = Color(0xFFFFB538),
    favoriteInactive = Color(0xFFA2A7B7),
    switchTrackOff = Color(0xFFD8DBE5),
    cardActionBackground = Color(0xEDFFFFFF),
    cardActionText = Color(0xFF3D4358),
    cardActionRandomBackground = Color(0xF0F2EEFF),
    cardActionRandomText = Color(0xFF6752B8),
    emptyBackground = Color(0xADFFFFFF),
    emptyBorder = Color(0x387045F6),
    chipBorder = Color(0x1A292E4C),
    chipText = Color(0xFF71778A)
)

val DarkStarlightColors = StarlightColors(
    appBackground = Color(0xFF0D1120),
    windowBackground = Color(0xFF080B15),
    surface = Color(0xFF171C2F),
    textPrimary = Color(0xFFF3F5FF),
    textMuted = Color(0xFFA8AEC0),
    line = Color(0x1AE2E6FF),
    primary = Color(0xFF9A78FF),
    primaryVariant = Color(0xFF7D5CFF),
    primarySoft = Color(0x299A78FF),
    onPrimary = Color(0xFFFFFFFF),
    cardGradientStart = Color(0xFF1D2237),
    cardGradientMid = Color(0xFF181D31),
    cardGradientEnd = Color(0xFF143036),
    cardBorder = Color(0x14FFFFFF),
    thumbnailGradientStart = Color(0xFF2A2745),
    thumbnailGradientEnd = Color(0xFF183B42),
    navBackground = Color(0xD6131727),
    navBorder = Color(0x1AFFFFFF),
    navIconInactive = Color(0xFF858B9D),
    favoriteActive = Color(0xFFFFB538),
    favoriteInactive = Color(0xFF757C90),
    switchTrackOff = Color(0xFF3A4055),
    cardActionBackground = Color(0xED1C2236),
    cardActionText = Color(0xFFD9DDEC),
    cardActionRandomBackground = Color(0xB3483A75),
    cardActionRandomText = Color(0xFFD1C6FF),
    emptyBackground = Color(0xB8181D30),
    emptyBorder = Color(0x389A78FF),
    chipBorder = Color(0x1AE2E6FF),
    chipText = Color(0xFFA8AEC0)
)
