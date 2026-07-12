package com.playroom.feature.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.playroom.core.designsystem.theme.PlayroomTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Library - compact 360dp")
@Composable
private fun LibraryScreenCompactPreview() {
    PlayroomTheme {
        LibraryRoute(
            onNavigateToPlayer = {},
            contentPadding = PaddingValues(0),
            viewModel = LibraryViewModel(),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "Library - regular 412dp")
@Composable
private fun LibraryScreenRegularPreview() {
    PlayroomTheme {
        LibraryRoute(
            onNavigateToPlayer = {},
            contentPadding = PaddingValues(0),
            viewModel = LibraryViewModel(),
        )
    }
}
