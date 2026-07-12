package com.playroom.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.playroom.core.designsystem.theme.PlayroomCorner

/**
 * The right-side tilted artwork frame used on the home hero/recommend cards
 * and library/favorites banners. Rotated slightly, cropped to fill, with a
 * soft highlight so it reads as a floating panel rather than a flat photo —
 * matching `PLAYROOM_main_target.jpg`.
 */
@Composable
fun PlayroomImageFrame(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    rotationDegrees: Float = -7f,
    cornerRadius: Dp = PlayroomCorner.ExtraLarge,
) {
    Box(
        modifier = modifier
            .graphicsLayer { rotationZ = rotationDegrees }
            .clip(RoundedCornerShape(cornerRadius)),
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.16f), Color.Transparent),
                    ),
                ),
        )
    }
}
