package com.starlight.expedition.core.designsystem.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starlight.expedition.core.data.image.LocalCoverImageLoader
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.model.Platform

/**
 * 실제 게임 커버(있으면) 또는 플랫폼 기본 표시(없으면)를 보여줍니다.
 * 다른 게임의 정적 커버를 대신 사용하지 않습니다(실행지시서 24절).
 */
@Composable
fun GameCoverImage(
    coverUri: String?,
    platform: Platform,
    imageLoader: LocalCoverImageLoader?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = StarlightTheme.shapes.thumbnail
) {
    var bitmap by remember(coverUri) { mutableStateOf<Bitmap?>(null) }
    var sizePx by remember { mutableIntStateOf(0) }

    if (coverUri != null && imageLoader != null) {
        LaunchedEffect(coverUri, sizePx) {
            if (sizePx > 0) {
                bitmap = imageLoader.loadThumbnail(coverUri, sizePx)
            }
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { size -> sizePx = maxOf(size.width, size.height) }
            .clip(shape)
    ) {
        val currentBitmap = bitmap
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PlatformFallbackCover(platform = platform, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun PlatformFallbackCover(platform: Platform, modifier: Modifier = Modifier) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(colors.thumbnailGradientStart, colors.thumbnailGradientEnd),
                start = Offset(0f, 0f),
                end = Offset(1f, 1f)
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = platform.displayName,
            style = StarlightTheme.typography.gameRowSubtitle,
            color = colors.textMuted,
            maxLines = 2,
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}
