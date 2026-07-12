package com.starlight.expedition.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalStarlightColors = staticCompositionLocalOf { LightStarlightColors }
val LocalStarlightSpacingValues = staticCompositionLocalOf { LocalStarlightSpacing }
val LocalStarlightShapes = staticCompositionLocalOf { LocalStarlightShapesInstance }
val LocalStarlightTypography = staticCompositionLocalOf { LocalStarlightTypographyInstance }

/**
 * 별빛 탐험대 앱 전역 테마입니다.
 * 다크모드는 시스템 설정을 강제하지 않고, [darkTheme] 파라미터로 전달되는
 * 사용자 저장 설정을 그대로 따릅니다.
 */
@Composable
fun StarlightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val starlightColors = if (darkTheme) DarkStarlightColors else LightStarlightColors

    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = starlightColors.primary,
            onPrimary = starlightColors.onPrimary,
            background = starlightColors.appBackground,
            onBackground = starlightColors.textPrimary,
            surface = starlightColors.surface,
            onSurface = starlightColors.textPrimary
        )
    } else {
        lightColorScheme(
            primary = starlightColors.primary,
            onPrimary = starlightColors.onPrimary,
            background = starlightColors.appBackground,
            onBackground = starlightColors.textPrimary,
            surface = starlightColors.surface,
            onSurface = starlightColors.textPrimary
        )
    }

    CompositionLocalProvider(
        LocalStarlightColors provides starlightColors,
        LocalStarlightSpacingValues provides LocalStarlightSpacing,
        LocalStarlightShapes provides LocalStarlightShapesInstance,
        LocalStarlightTypography provides LocalStarlightTypographyInstance
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}

/**
 * 화면 Composable에서 `StarlightTheme.colors` 형태로 접근하기 위한 진입점입니다.
 */
object StarlightTheme {
    val colors: StarlightColors
        @Composable
        get() = LocalStarlightColors.current

    val spacing: StarlightSpacing
        @Composable
        get() = LocalStarlightSpacingValues.current

    val shapes: StarlightShapes
        @Composable
        get() = LocalStarlightShapes.current

    val typography: StarlightTypography
        @Composable
        get() = LocalStarlightTypography.current
}
