package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 * 빠른 시작 카드 2개가 동일하게 사용하는 커버 프레임입니다.
 * HTML의 .cover-frame / .cover-image 비율을 Compose 좌표로 그대로 옮깁니다.
 *
 * - 상단 위치: 카드 높이의 10.24%
 * - 오른쪽 위치: 카드 너비의 -13.71% (카드 오른쪽 바깥으로 걸쳐지고 카드 clip으로 잘림)
 * - 프레임 높이: 카드 높이의 74.70%, 비율 1:1
 * - 모서리: 프레임 크기의 29.84%
 * - 기울기: -5도, 내부 콘텐츠는 +5도로 보정
 *
 * 이 Composable을 사용하는 쪽에서 카드 자체에 [Modifier.clip]을 적용해야
 * 카드 바깥으로 나가는 부분이 정상적으로 잘립니다.
 */
@Composable
fun CoverFrame(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    decorationBrush: Brush? = null
) {
    CoverFrame(modifier = modifier, decorationBrush = decorationBrush) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * 실제 게임 커버(비동기로 불러오는 [GameCoverImage] 등)를 넣을 수 있는 범용 버전입니다.
 * [content]는 프레임을 꽉 채우도록(`Modifier.fillMaxSize()`) 그려야 합니다.
 */
@Composable
fun CoverFrame(
    modifier: Modifier = Modifier,
    decorationBrush: Brush? = null,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val cardWidth = maxWidth
        val cardHeight = maxHeight

        val frameSize = cardHeight * 0.7470f
        val topOffset = cardHeight * 0.1024f
        // right: -13.71% -> 프레임 오른쪽 끝이 카드 오른쪽 경계보다 13.71%만큼 더 바깥에 위치합니다.
        val frameRightEdge = cardWidth * 1.1371f
        val leftOffset = frameRightEdge - frameSize

        val cornerRadius = frameSize * 0.2984f
        val imageOverscan = frameSize * 1.14f
        val imageShift = frameSize * 0.0615f

        Box(
            modifier = Modifier
                .offset(x = leftOffset, y = topOffset)
                .size(frameSize)
                .graphicsLayer { rotationZ = -5f }
                .clip(RoundedCornerShape(cornerRadius))
                .let { frameModifier ->
                    if (decorationBrush != null) frameModifier.background(decorationBrush) else frameModifier
                }
        ) {
            Box(
                modifier = Modifier
                    .size(imageOverscan)
                    .offset(x = -imageShift, y = -imageShift)
                    .graphicsLayer { rotationZ = 5f }
                    .align(Alignment.Center)
            ) {
                content()
            }
        }
    }
}
