package com.starlight.expedition.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 설정 Dialog 안의 항목 한 줄입니다. (효과음 / 자동 저장 / 다크 모드 / 앱 정보)
 */
@Composable
fun SettingRow(
    icon: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit
) {
    val colors = StarlightTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(StarlightTheme.shapes.settingIcon)
                    .background(colors.primarySoft),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, color = colors.primary)
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = title, style = StarlightTheme.typography.settingRowTitle, color = colors.textPrimary)
                Text(
                    text = description,
                    style = StarlightTheme.typography.settingRowSubtitle,
                    color = colors.textMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        trailing()
    }
}

@Composable
fun SettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val trackColor = if (checked) colors.primary else colors.switchTrackOff
    val thumbOffset by animateDpAsState(if (checked) 21.dp else 0.dp, label = "switchThumb")

    Box(
        modifier = modifier
            .width(50.dp)
            .height(29.dp)
            .clip(StarlightTheme.shapes.circle)
            .background(trackColor)
            .scaleClickable(onClick = { onCheckedChange(!checked) })
            .semantics {
                this.contentDescription = contentDescription
            }
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(23.dp)
                .clip(StarlightTheme.shapes.circle)
                .background(Color.White)
        )
    }
}

@Composable
fun SettingsCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(StarlightTheme.shapes.circle)
            .background(colors.primarySoft)
            .scaleClickable(onClick = onClick)
            .semantics { contentDescription = "설정 닫기" },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
