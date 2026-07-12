package com.starlight.expedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import com.starlight.expedition.core.data.repository.SettingsRepository
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

class MainActivity : ComponentActivity() {

    private val appContainer: AppContainer
        get() = (application as StarlightApplication).appContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsRepository: SettingsRepository = appContainer.settingsRepository
            val settings by settingsRepository.observeSettings()
                .collectAsState(initial = null)

            val darkModeEnabled = settings?.darkModeEnabled ?: false

            LaunchedEffect(darkModeEnabled) {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.isAppearanceLightStatusBars = !darkModeEnabled
                controller.isAppearanceLightNavigationBars = !darkModeEnabled
            }

            StarlightTheme(darkTheme = darkModeEnabled) {
                StarlightApp(appContainer = appContainer)
            }
        }
    }
}
