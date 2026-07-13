package com.starlight.expedition.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

/**
 * 빠른 시작 카드1의 "이어서 하기" 주 버튼입니다.
 */
@Composable
fun MainActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier
            .height(53.dp)
            .clip(StarlightTheme.shapes.mainAction)
            .background(
                Brush.linearGradient(listOf(colors.primaryVariant, colors.primary))
            )
            .scaleClickable(onClick = onClick)
            .padding(horizontal = 21.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = colors.onPrimary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                style = StarlightTheme.typography.mainActionLabel,
                color = colors.onPrimary,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

enum class CardActionEmphasis { NEUTRAL, PRIMARY, RANDOM }

/**
 * 빠른 시작 카드2 하단의 "게임 소개 / 바로 시작 / 랜덤 선택" 세 버튼에 공통으로 사용합니다.
 */
@Composable
fun CardActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasis: CardActionEmphasis = CardActionEmphasis.NEUTRAL
) {
    val colors = StarlightTheme.colors
    val background = when (emphasis) {
        CardActionEmphasis.NEUTRAL -> colors.cardActionBackground
        CardActionEmphasis.PRIMARY -> colors.primary
        CardActionEmphasis.RANDOM -> colors.cardActionRandomBackground
    }
    val textColor = when (emphasis) {
        CardActionEmphasis.NEUTRAL -> colors.cardActionText
        CardActionEmphasis.PRIMARY -> colors.onPrimary
        CardActionEmphasis.RANDOM -> colors.cardActionRandomText
    }

    Box(
        modifier = modifier
            .height(41.dp)
            .clip(StarlightTheme.shapes.cardAction)
            .background(background)
            .scaleClickable(onClick = onClick)
            .padding(horizontal = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = StarlightTheme.typography.cardActionLabel,
            color = textColor,
            maxLines = 1
        )
    }
}

/**
 * 상단 헤더의 설정 톱니바퀴입니다.
 * 배경 박스 없이, 누를 때만 보라색으로 바뀌고 살짝 회전·축소됩니다.
 */
@Composable
fun SettingsGearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val tint = if (isPressed) colors.primary else colors.textMuted
    val rotation by animateFloatAsState(if (isPressed) 11f else 0f, label = "gearRotation")
    val scale by animateFloatAsState(if (isPressed) 0.91f else 1f, label = "gearScale")

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .semantics { contentDescription = "설정 열기" },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
    }
}

/**
 * 상단 헤더의 게임 폴더 관리 아이콘입니다. 설정 아이콘과 동일한 크기를 쓰고,
 * 설정 아이콘이 숨겨진 화면(빠른 시작)에서도 항상 표시됩니다.
 */
@Composable
fun GameFolderIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val tint = if (isPressed) colors.primary else colors.textMuted
    val scale by animateFloatAsState(if (isPressed) 0.91f else 1f, label = "folderIconScale")

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .semantics { contentDescription = "게임 폴더 관리" },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.FolderOpen,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}
