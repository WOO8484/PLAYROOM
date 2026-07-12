package com.playroom.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.playroom.core.designsystem.theme.PlayroomSpacing

/**
 * Full width on phones; capped at [PlayroomSpacing.MaxContentWidth] (520dp)
 * and centered on 600dp+ screens, per work order §9–10 ("태블릿에서는
 * 콘텐츠 최대 폭 520dp 후 중앙 정렬"). Every top-level screen applies this
 * once to its root scrollable container instead of re-deriving breakpoints.
 */
fun Modifier.playroomContentWidth(): Modifier = this
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .widthIn(max = PlayroomSpacing.MaxContentWidth)
