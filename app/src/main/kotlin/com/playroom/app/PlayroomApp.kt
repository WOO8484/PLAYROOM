package com.playroom.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.feature.onboarding.OnboardingRoute

/** App root: picks the color skin, then either 온보딩 or the 4-tab main scaffold. */
@Composable
fun PlayroomApp(appViewModel: PlayroomAppViewModel = viewModel()) {
    val skin by appViewModel.skin.collectAsStateWithLifecycle()
    val hasSeenOnboarding by appViewModel.hasSeenOnboarding.collectAsStateWithLifecycle()

    PlayroomTheme(skin = skin) {
        if (hasSeenOnboarding) {
            MainScaffold(
                currentSkin = skin,
                onSkinSelected = appViewModel::setSkin,
                onReplayOnboarding = appViewModel::replayOnboarding,
            )
        } else {
            OnboardingRoute(onStartClick = appViewModel::markOnboardingSeen)
        }
    }
}
