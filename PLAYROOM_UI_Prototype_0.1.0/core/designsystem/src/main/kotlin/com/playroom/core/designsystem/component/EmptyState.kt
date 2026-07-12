package com.playroom.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType

/** Centered Korean empty-state message, e.g. "검색 결과가 없습니다." */
@Composable
fun PlayroomEmptyState(message: String, modifier: Modifier = Modifier) {
    val palette = PlayroomTheme.palette
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PlayroomSpacing.Huge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = PlayroomType.BodyMedium,
            color = palette.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
