package com.playroom.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.playroom.core.model.AppSkin

val LocalPlayroomPalette = compositionLocalOf { PlayroomPalettes.Light }

/** Convenience accessor: `PlayroomTheme.palette` inside any Composable. */
object PlayroomTheme {
    val palette: PlayroomPalette
        @Composable get() = LocalPlayroomPalette.current
}

private fun paletteFor(skin: AppSkin): PlayroomPalette = when (skin) {
    AppSkin.LIGHT -> PlayroomPalettes.Light
    AppSkin.DARK -> PlayroomPalettes.Dark
    AppSkin.RETRO -> PlayroomPalettes.Retro
    AppSkin.SIMPLE -> PlayroomPalettes.Simple
}

private fun PlayroomPalette.toColorScheme() = if (this === PlayroomPalettes.Dark || this === PlayroomPalettes.Retro) {
    darkColorScheme(
        primary = primary,
        onPrimary = textOnPrimary,
        secondary = aqua,
        background = background,
        onBackground = textPrimary,
        surface = surface,
        onSurface = textPrimary,
        surfaceVariant = surfaceSoft,
        onSurfaceVariant = textSecondary,
        error = danger,
        outline = outline,
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = textOnPrimary,
        secondary = aqua,
        background = background,
        onBackground = textPrimary,
        surface = surface,
        onSurface = textPrimary,
        surfaceVariant = surfaceSoft,
        onSurfaceVariant = textSecondary,
        error = danger,
        outline = outline,
    )
}

private val baseTypography = Typography()
private fun playroomTypography() = baseTypography.copy(
    titleLarge = baseTypography.titleLarge.copy(fontWeight = FontWeight.Black),
    titleMedium = baseTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
    bodyMedium = baseTypography.bodyMedium.copy(fontSize = 14.sp),
    labelLarge = baseTypography.labelLarge.copy(fontWeight = FontWeight.Bold),
)

/**
 * Root theme for the whole app. [skin] comes from settings (persisted via
 * SharedPreferences at the `:app` level) and actually swaps the color
 * palette — this is not a cosmetic accent change.
 */
@Composable
fun PlayroomTheme(
    skin: AppSkin = AppSkin.LIGHT,
    content: @Composable () -> Unit,
) {
    val palette = paletteFor(skin)
    CompositionLocalProvider(LocalPlayroomPalette provides palette) {
        MaterialTheme(
            colorScheme = palette.toColorScheme(),
            typography = playroomTypography(),
            content = content,
        )
    }
}

/** True for narrow phones (360dp~399dp) where compact type/spacing tokens apply. */
@Composable
fun isCompactWidth(): Boolean {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    return configuration.screenWidthDp < 400
}
