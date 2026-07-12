package com.playroom.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.isCompactWidth

/**
 * Shared top bar: brand mark + "PLAYROOM" wordmark on the left, an optional
 * search button on the right. Pads itself for the status bar / camera
 * cutout so it never sits under the system UI, even though it lives in a
 * `Scaffold`'s `topBar` slot (which — unlike `content` — does not get
 * `contentWindowInsets` applied automatically).
 */
@Composable
fun PlayroomTopBar(
    modifier: Modifier = Modifier,
    onSearchClick: (() -> Unit)? = null,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .height(PlayroomSpacing.TopBarHeight)
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Brush.linearGradient(listOf(palette.primary, palette.primaryDark)),
                        RoundedCornerShape(PlayroomCorner.Medium),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = null,
                    tint = palette.textOnPrimary,
                )
            }
            Text(
                text = "PLAYROOM",
                modifier = Modifier.padding(start = PlayroomSpacing.Medium),
                color = palette.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (onSearchClick != null) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(palette.surface.copy(alpha = 0.7f), RoundedCornerShape(PlayroomCorner.Medium)),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "검색",
                        tint = palette.textPrimary,
                    )
                }
            }
        }
    }
}
