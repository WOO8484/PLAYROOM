package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

data class BottomNavEntry(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)

/**
 * 화면 위에 떠 있는 카드형 하단 메뉴입니다.
 * 선택된 항목만 아이콘 뒤에 옅은 원형 배경과 보라색 글자를 사용하고,
 * 메뉴 전체를 덮는 배경은 사용하지 않습니다.
 */
@Composable
fun BottomNavBar(
    entries: List<BottomNavEntry>,
    selectedRoute: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(StarlightTheme.spacing.bottomNavHeight)
            .shadow(elevation = 18.dp, shape = StarlightTheme.shapes.bottomNav, clip = false)
            .clip(StarlightTheme.shapes.bottomNav)
            .background(colors.navBackground)
            .border(1.dp, colors.navBorder, StarlightTheme.shapes.bottomNav)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        entries.forEach { entry ->
            val isSelected = entry.route == selectedRoute
            NavItem(
                entry = entry,
                isSelected = isSelected,
                onClick = { onSelect(entry.route) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavItem(
    entry: BottomNavEntry,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val tint = if (isSelected) colors.primary else colors.navIconInactive

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(StarlightTheme.shapes.navIcon)
            .scaleClickable(onClick = onClick)
            .semantics {
                contentDescription = entry.contentDescription
                selected = isSelected
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(StarlightTheme.shapes.circle)
                .background(if (isSelected) colors.primarySoft else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = entry.label,
            style = StarlightTheme.typography.navLabel,
            color = tint,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
