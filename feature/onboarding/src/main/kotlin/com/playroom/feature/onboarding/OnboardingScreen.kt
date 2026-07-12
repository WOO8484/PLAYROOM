package com.playroom.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.component.PlayroomImageFrame
import com.playroom.core.designsystem.component.PlayroomPrimaryButton
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.designsystem.theme.isCompactWidth
import com.playroom.core.designsystem.R

/**
 * First-run welcome screen. Reuses the home hero card's visual language
 * (soft gradient card, tilted artwork, purple primary button) as required
 * by the work order.
 */
@Composable
fun OnboardingRoute(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = PlayroomTheme.palette
    val compact = isCompactWidth()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(PlayroomSpacing.Large),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(PlayroomCorner.ExtraLarge))
                .background(Brush.linearGradient(palette.cardGradient))
                .padding(PlayroomSpacing.ExtraLarge),
        ) {
            PlayroomImageFrame(
                imageRes = R.drawable.card_art_starlight,
                rotationDegrees = -6.5f,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f),
            )

            Column(modifier = Modifier.padding(top = PlayroomSpacing.ExtraLarge)) {
                Text(text = "WELCOME TO PLAYROOM", style = PlayroomType.Eyebrow, color = palette.primaryDark)
                Text(
                    text = "내 게임을\n한곳에",
                    style = if (compact) PlayroomType.HeroTitleCompact else PlayroomType.HeroTitle,
                    color = palette.textPrimary,
                    modifier = Modifier.padding(top = 10.dp, bottom = PlayroomSpacing.Small),
                )
                Text(
                    text = "게임 폴더를 한 번만 선택하면 자동으로 정리하고 바로 이어서 즐길 수 있습니다.",
                    style = PlayroomType.BodyMedium,
                    color = palette.textSecondary,
                )

                PlayroomPrimaryButton(
                    text = "▶ PLAYROOM 시작하기",
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = PlayroomSpacing.ExtraLarge),
                )
            }
        }
    }
}
