package com.starlight.expedition.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role

/**
 * 기본 직사각형 Ripple 대신, 눌렀을 때 0.97배로 살짝 줄어드는 클릭 효과입니다.
 * 카드형·아이콘형 버튼처럼 별도의 모양별 Ripple을 그리기 어려운 곳에서 사용합니다.
 */
fun Modifier.scaleClickable(
    role: Role? = Role.Button,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    minScale: Float = 0.97f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) minScale else 1f, label = "scaleClickable")

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            role = role,
            onClickLabel = onClickLabel,
            onClick = onClick
        )
}
