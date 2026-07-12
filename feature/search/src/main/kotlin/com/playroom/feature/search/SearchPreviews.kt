package com.playroom.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.playroom.core.designsystem.theme.PlayroomTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Search - compact 360dp")
@Composable
private fun SearchScreenPreview() {
    PlayroomTheme {
        SearchRoute(
            onNavigateToPlayer = {},
            onClose = {},
            viewModel = SearchViewModel(),
        )
    }
}
