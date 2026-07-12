package com.playroom.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.model.AppSkin

@Preview(showBackground = true, widthDp = 360, heightDp = 900, name = "Settings - compact 360dp")
@Composable
private fun SettingsScreenPreview() {
    PlayroomTheme {
        SettingsRoute(
            currentSkin = AppSkin.LIGHT,
            onSkinSelected = {},
            onManageGameFolder = {},
            onReplayOnboarding = {},
            contentPadding = PaddingValues(0),
            viewModel = SettingsViewModel(),
        )
    }
}
