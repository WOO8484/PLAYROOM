package com.playroom.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType

/** Main call-to-action button (`▶ 이어서 하기`, `PLAYROOM 시작하기`, `적용`, ...). Solid purple gradient. */
@Composable
fun PlayroomPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val palette = PlayroomTheme.palette
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = PlayroomSpacing.MinTouchTarget),
        shape = RoundedCornerShape(PlayroomCorner.Medium),
        contentPadding = PaddingValues(horizontal = PlayroomSpacing.Large),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = palette.textOnPrimary,
        ),
        elevation = null,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(palette.primary, palette.primaryDark)),
                    RoundedCornerShape(PlayroomCorner.Medium),
                )
                .padding(horizontal = 0.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            Text(text = text, style = PlayroomType.ButtonLabel, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

/** Secondary, low-emphasis button (`취소`, `게임 소개`, filter actions). Semi-transparent surface. */
@Composable
fun PlayroomSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val palette = PlayroomTheme.palette
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = PlayroomSpacing.MinTouchTarget),
        shape = RoundedCornerShape(PlayroomCorner.Small + 2.dp),
        contentPadding = PaddingValues(horizontal = PlayroomSpacing.Medium),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = palette.surfaceSoft,
            contentColor = palette.textPrimary,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.outline),
    ) {
        Text(text = text, style = PlayroomType.ButtonLabel, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * One of the 4 in-a-row action buttons under the home 2nd card
 * (게임 소개 / ▶ 바로 시작 / 랜덤 추천 / 옵션). [primary] makes it the solid
 * purple one (바로 시작); the rest stay translucent white.
 */
@Composable
fun RowScope.PlayroomActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
) {
    val palette = PlayroomTheme.palette
    val containerColor = if (primary) palette.primary else palette.surface.copy(alpha = 0.58f)
    val contentColor = if (primary) palette.textOnPrimary else palette.textPrimary

    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .weight(1f)
            .heightIn(min = 38.dp, max = 42.dp)
            .widthIn(min = PlayroomSpacing.MinTouchTarget),
        shape = RoundedCornerShape(PlayroomCorner.Small),
        contentPadding = PaddingValues(horizontal = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        elevation = null,
    ) {
        Text(
            text = text,
            style = PlayroomType.ActionButtonLabel,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
