package com.playroom.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomCorner
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType

/** Gradient header banner used at the top of 게임룸 and 즐겨찾기. */
@Composable
fun PlayroomLibraryBanner(
    eyebrow: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int? = null,
) {
    val palette = PlayroomTheme.palette
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PlayroomCorner.Large))
            .background(Brush.linearGradient(palette.cardGradient))
            .padding(PlayroomSpacing.Large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = eyebrow, style = PlayroomType.Eyebrow, color = palette.primaryDark)
            Text(
                text = title,
                style = PlayroomType.SectionTitle,
                color = palette.textPrimary,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
            )
            Text(
                text = description,
                style = PlayroomType.SectionSubtitle,
                color = palette.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (imageRes != null) {
            PlayroomImageFrame(
                imageRes = imageRes,
                rotationDegrees = -6f,
                modifier = Modifier
                    .padding(start = PlayroomSpacing.Medium)
                    .width(78.dp)
                    .height(78.dp),
            )
        }
    }
}
