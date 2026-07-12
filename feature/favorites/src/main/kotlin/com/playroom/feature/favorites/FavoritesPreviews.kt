package com.playroom.feature.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.theme.PlayroomTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Favorites - compact 360dp")
@Composable
private fun FavoritesScreenPreview() {
    PlayroomTheme {
        FavoritesRoute(
            onNavigateToPlayer = {},
            contentPadding = PaddingValues(0.dp),
            viewModel = FavoritesViewModel(),
        )
    }
}
