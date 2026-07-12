package com.playroom.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.playroom.core.designsystem.theme.PlayroomTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Home - compact 360dp")
@Composable
private fun HomeScreenCompactPreview() {
    PlayroomTheme {
        HomeRoute(
            onNavigateToPlayer = {},
            contentPadding = PaddingValues(0),
            viewModel = HomeViewModel(),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "Home - regular 412dp")
@Composable
private fun HomeScreenRegularPreview() {
    PlayroomTheme {
        HomeRoute(
            onNavigateToPlayer = {},
            contentPadding = PaddingValues(0),
            viewModel = HomeViewModel(),
        )
    }
}
