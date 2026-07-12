package com.playroom.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.playroom.core.designsystem.theme.PlayroomTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Player - compact 360dp")
@Composable
private fun PlayerScreenPreview() {
    PlayroomTheme {
        PlayerRoute(gameId = 1, onExit = {})
    }
}
