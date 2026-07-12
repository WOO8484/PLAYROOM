package com.playroom.core.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Bespoke text styles for PLAYROOM's card-heavy layout. These sit alongside
 * (not instead of) [androidx.compose.material3.MaterialTheme.typography] —
 * card titles, eyebrows and action-button labels don't map cleanly onto the
 * standard Material3 type scale, so they're named tokens here instead.
 */
object PlayroomType {
    val Eyebrow = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.3.sp)

    val HeroTitle = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.6).sp, lineHeight = 38.sp)
    val HeroTitleCompact = TextStyle(fontSize = 29.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp, lineHeight = 33.sp)

    val RecommendTitle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp, lineHeight = 32.sp)
    val RecommendTitleCompact = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.4).sp, lineHeight = 29.sp)

    val CardMeta = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
    val CardDescription = TextStyle(fontSize = 13.sp, lineHeight = 19.sp, fontWeight = FontWeight.Normal)

    val SectionTitle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.3).sp)
    val SectionSubtitle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)

    val CardTitle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
    val CardCaption = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)

    val ButtonLabel = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
    val ActionButtonLabel = TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Black)

    val BodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp)
    val Caption = TextStyle(fontSize = 12.sp, lineHeight = 16.sp)
    val NavLabel = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold)
    val Badge = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold)
}
