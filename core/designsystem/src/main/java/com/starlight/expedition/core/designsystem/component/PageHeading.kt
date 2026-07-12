package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 홈 / 즐겨찾기 / 게임리스트 화면 상단의 제목 + 설명 영역입니다.
 */
@Composable
fun PageHeading(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(text = title, style = StarlightTheme.typography.pageTitle, color = colors.textPrimary)
        Text(
            text = description,
            style = StarlightTheme.typography.pageSubtitle,
            color = colors.textMuted,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
