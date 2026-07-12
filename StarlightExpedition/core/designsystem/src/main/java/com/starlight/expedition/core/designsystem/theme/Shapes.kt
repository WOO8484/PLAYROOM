package com.starlight.expedition.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class StarlightShapes(
    val featureCard: RoundedCornerShape = RoundedCornerShape(31.dp),
    val mainAction: RoundedCornerShape = RoundedCornerShape(19.dp),
    val cardAction: RoundedCornerShape = RoundedCornerShape(14.dp),
    val summaryCard: RoundedCornerShape = RoundedCornerShape(22.dp),
    val gameRow: RoundedCornerShape = RoundedCornerShape(24.dp),
    val thumbnail: RoundedCornerShape = RoundedCornerShape(19.dp),
    val playButton: RoundedCornerShape = RoundedCornerShape(15.dp),
    val favoriteButton: RoundedCornerShape = RoundedCornerShape(14.dp),
    val searchBox: RoundedCornerShape = RoundedCornerShape(18.dp),
    val chip: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val bottomNav: RoundedCornerShape = RoundedCornerShape(28.dp),
    val navIcon: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val settingsPanel: RoundedCornerShape = RoundedCornerShape(30.dp),
    val settingIcon: RoundedCornerShape = RoundedCornerShape(13.dp),
    val emptyState: RoundedCornerShape = RoundedCornerShape(24.dp),
    val circle: RoundedCornerShape = RoundedCornerShape(percent = 50)
)

val LocalStarlightShapesInstance = StarlightShapes()
