package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 홈 화면 상단의 요약 카드 3개(최근 게임 / 즐겨찾기 / 오늘 플레이)에 사용합니다.
 */
@Composable
fun SummaryCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(StarlightTheme.shapes.summaryCard)
            .background(colors.surface)
            .border(1.dp, colors.line, StarlightTheme.shapes.summaryCard)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = value, style = StarlightTheme.typography.summaryValue, color = colors.primary)
        Text(
            text = label,
            style = StarlightTheme.typography.summaryLabel,
            color = colors.textMuted,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}
