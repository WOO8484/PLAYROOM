package com.playroom.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth

/**
 * One entry in [PlayroomBottomBar]. Deliberately independent from
 * `:core:navigation`'s `TopLevelDestination` — `:core:designsystem` may only
 * depend on `:core:model`, so `:app` maps destinations to these items.
 */
data class PlayroomBottomBarItem(
    val label: String,
    val icon: ImageVector,
    val selected: Boolean,
    val onClick: () -> Unit,
)

/**
 * Shared bottom navigation bar: 메인 / 게임룸 / 즐겨찾기 / 설정, drawn as a
 * floating rounded card. Pads itself for the gesture bar / 3-button nav bar
 * so the card never sits under system UI — a `Scaffold`'s `bottomBar` slot
 * does not get `contentWindowInsets` applied automatically.
 */
@Composable
fun PlayroomBottomBar(
    items: List<PlayroomBottomBarItem>,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()
    val horizontalPadding = if (compact) PlayroomSpacing.ScreenHorizontalCompact else PlayroomSpacing.ScreenHorizontalRegular

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
            .padding(horizontal = horizontalPadding, vertical = PlayroomSpacing.Small)
            .height(PlayroomSpacing.BottomBarHeight)
            .background(palette.surface, RoundedCornerShape(PlayroomCorner.ExtraLarge))
            .padding(horizontal = PlayroomSpacing.Small),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            BottomBarEntry(item)
        }
    }
}

@Composable
private fun RowScope.BottomBarEntry(item: PlayroomBottomBarItem) {
    val palette = PlayroomTheme.palette
    val background = if (item.selected) palette.chipSelected else Color.Transparent
    val contentColor = if (item.selected) palette.primary else palette.textSecondary

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .background(background, RoundedCornerShape(PlayroomCorner.Medium))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = item.onClick,
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(imageVector = item.icon, contentDescription = item.label, tint = contentColor)
        Text(
            text = item.label,
            style = PlayroomType.NavLabel,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
