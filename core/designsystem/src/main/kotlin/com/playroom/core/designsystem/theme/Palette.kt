package com.playroom.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Every color PLAYROOM needs, grouped per skin. [com.playroom.core.model.AppSkin]
 * picks one of these; switching skins in 설정 swaps the whole palette so the
 * theme change is real, not just a single accent tweak.
 */
data class PlayroomPalette(
    val background: Color,
    val surface: Color,
    val surfaceSoft: Color,
    val outline: Color,
    val primary: Color,
    val primaryDark: Color,
    val aqua: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textOnPrimary: Color,
    val danger: Color,
    val success: Color,
    val warning: Color,
    val cardGradient: List<Color>,
    val chipSelected: Color,
)

object PlayroomPalettes {

    val Light = PlayroomPalette(
        background = Color(0xFFEEF3F8),
        surface = Color(0xFFFFFFFF),
        surfaceSoft = Color(0xFFF7F8FD),
        outline = Color(0x1A36466C),
        primary = Color(0xFF7259FF),
        primaryDark = Color(0xFF5B43F1),
        aqua = Color(0xFF47D6C8),
        textPrimary = Color(0xFF11192B),
        textSecondary = Color(0xFF69758B),
        textOnPrimary = Color(0xFFFFFFFF),
        danger = Color(0xFFE86679),
        success = Color(0xFF3FC488),
        warning = Color(0xFFE0A93F),
        cardGradient = listOf(Color(0xFFFAF8FF), Color(0xFFE1DDFF), Color(0xFFC8EDF1)),
        chipSelected = Color(0xFFE7E1FF),
    )

    val Dark = PlayroomPalette(
        background = Color(0xFF11131C),
        surface = Color(0xFF1B1F2E),
        surfaceSoft = Color(0xFF232840),
        outline = Color(0x33FFFFFF),
        primary = Color(0xFF8C76FF),
        primaryDark = Color(0xFF6C54F5),
        aqua = Color(0xFF56E4D6),
        textPrimary = Color(0xFFEFF1FB),
        textSecondary = Color(0xFF9BA4C2),
        textOnPrimary = Color(0xFFFFFFFF),
        danger = Color(0xFFFF8A99),
        success = Color(0xFF52D69A),
        warning = Color(0xFFF0BF5C),
        cardGradient = listOf(Color(0xFF2A2550), Color(0xFF221C3C), Color(0xFF163134)),
        chipSelected = Color(0xFF39316B),
    )

    val Retro = PlayroomPalette(
        background = Color(0xFF14110A),
        surface = Color(0xFF221C12),
        surfaceSoft = Color(0xFF2B2417),
        outline = Color(0x33FFB238),
        primary = Color(0xFFFFB238),
        primaryDark = Color(0xFFE0901A),
        aqua = Color(0xFF6BCF8F),
        textPrimary = Color(0xFFF6ECD8),
        textSecondary = Color(0xFFBBA98A),
        textOnPrimary = Color(0xFF241A05),
        danger = Color(0xFFFF6B5B),
        success = Color(0xFF6BCF8F),
        warning = Color(0xFFFFB238),
        cardGradient = listOf(Color(0xFF2E2410), Color(0xFF20301A), Color(0xFF13332A)),
        chipSelected = Color(0xFF473411),
    )

    val Simple = PlayroomPalette(
        background = Color(0xFFF5F6F8),
        surface = Color(0xFFFFFFFF),
        surfaceSoft = Color(0xFFEFF1F4),
        outline = Color(0x1A1F2430),
        primary = Color(0xFF3D4A5C),
        primaryDark = Color(0xFF262F3B),
        aqua = Color(0xFF8FA3B8),
        textPrimary = Color(0xFF1F2430),
        textSecondary = Color(0xFF6B7280),
        textOnPrimary = Color(0xFFFFFFFF),
        danger = Color(0xFFD9534F),
        success = Color(0xFF4CA37B),
        warning = Color(0xFFC79A3F),
        cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFEEF0F3), Color(0xFFE4E7EC)),
        chipSelected = Color(0xFFE3E6EB),
    )
}
