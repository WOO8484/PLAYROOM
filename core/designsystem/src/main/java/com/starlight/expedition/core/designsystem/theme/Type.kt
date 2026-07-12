package com.starlight.expedition.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * HTML GUI의 텍스트 스타일을 화면별 의미 단위로 옮긴 타이포그래피 토큰입니다.
 */
@Immutable
data class StarlightTypography(
    val brandTitle: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp),
    val brandSubtitle: TextStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp),
    val featureLabel: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
    val featureTitle: TextStyle = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.6).sp),
    val featureTitleSecondary: TextStyle = TextStyle(fontSize = 23.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.6).sp),
    val featureDescription: TextStyle = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, lineHeight = 18.sp),
    val playStatus: TextStyle = TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Bold),
    val mainActionLabel: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold),
    val cardActionLabel: TextStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
    val pageTitle: TextStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp),
    val pageSubtitle: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
    val summaryValue: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black),
    val summaryLabel: TextStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
    val sectionTitle: TextStyle = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.4).sp),
    val textButton: TextStyle = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold),
    val gameRowTitle: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold),
    val gameRowSubtitle: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
    val navLabel: TextStyle = TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Bold),
    val settingsTitle: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black),
    val settingRowTitle: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
    val settingRowSubtitle: TextStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium),
    val chipLabel: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.ExtraBold),
    val searchInput: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    val emptyLabel: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
)

val LocalStarlightTypographyInstance = StarlightTypography()
