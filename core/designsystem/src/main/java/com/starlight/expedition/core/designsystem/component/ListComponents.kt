package com.starlight.expedition.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.starlight.expedition.core.designsystem.theme.StarlightTheme

@Composable
fun GameSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(StarlightTheme.shapes.searchBox)
            .background(colors.surface)
            .border(1.dp, colors.line, StarlightTheme.shapes.searchBox)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.padding(end = 10.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = StarlightTheme.typography.searchInput,
                    color = colors.textMuted
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = colors.textPrimary,
                    fontSize = StarlightTheme.typography.searchInput.fontSize
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.primary),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GenreChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    val background = if (isSelected) colors.primary else colors.surface
    val textColor = if (isSelected) colors.onPrimary else colors.chipText
    val borderColor = if (isSelected) colors.primary else colors.chipBorder

    Box(
        modifier = modifier
            .height(34.dp)
            .clip(StarlightTheme.shapes.chip)
            .background(background)
            .border(1.dp, borderColor, StarlightTheme.shapes.chip)
            .scaleClickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = StarlightTheme.typography.chipLabel, color = textColor)
    }
}

@Composable
fun EmptyResultState(
    message: String,
    modifier: Modifier = Modifier
) {
    val colors = StarlightTheme.colors
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(StarlightTheme.shapes.emptyState)
            .background(colors.emptyBackground)
            .border(1.dp, colors.emptyBorder, StarlightTheme.shapes.emptyState)
            .padding(vertical = 42.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, style = StarlightTheme.typography.emptyLabel, color = colors.textMuted)
    }
}
