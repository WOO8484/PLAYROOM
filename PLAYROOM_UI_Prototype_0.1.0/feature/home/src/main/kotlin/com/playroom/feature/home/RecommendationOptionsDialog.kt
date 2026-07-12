package com.playroom.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.playroom.core.designsystem.component.PlayroomDialog
import com.playroom.core.designsystem.component.PlayroomPrimaryButton
import com.playroom.core.designsystem.component.PlayroomSecondaryButton
import com.playroom.core.designsystem.theme.PlayroomSpacing
import com.playroom.core.designsystem.theme.PlayroomTheme
import com.playroom.core.designsystem.theme.PlayroomType
import com.playroom.core.model.LevelOption
import com.playroom.core.model.PlayerOption
import com.playroom.core.model.PriorityOption
import com.playroom.core.model.RecommendationOptions
import com.playroom.core.model.TimeOption

/** 추천 옵션 popup: 4 single-choice option groups + 취소/적용. */
@Composable
fun RecommendationOptionsDialog(
    options: RecommendationOptions,
    onOptionsChange: (RecommendationOptions) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
) {
    val palette = PlayroomTheme.palette

    PlayroomDialog(onDismissRequest = onDismiss) {
        Text(text = "추천 옵션", style = PlayroomType.SectionTitle, color = palette.textPrimary)
        Text(
            text = "원하는 조건을 고르면 랜덤 추천에 반영됩니다.",
            style = PlayroomType.BodyMedium,
            color = palette.textSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = PlayroomSpacing.Large),
        )

        OptionGroup(
            title = "플레이 시간",
            options = TimeOption.entries,
            selected = options.time,
            labelOf = { it.label },
            onSelect = { onOptionsChange(options.copy(time = it)) },
        )
        OptionGroup(
            title = "난이도",
            options = LevelOption.entries,
            selected = options.level,
            labelOf = { it.label },
            onSelect = { onOptionsChange(options.copy(level = it)) },
        )
        OptionGroup(
            title = "인원",
            options = PlayerOption.entries,
            selected = options.players,
            labelOf = { it.label },
            onSelect = { onOptionsChange(options.copy(players = it)) },
        )
        OptionGroup(
            title = "추천 방식",
            options = PriorityOption.entries,
            selected = options.priority,
            labelOf = { it.label },
            onSelect = { onOptionsChange(options.copy(priority = it)) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PlayroomSpacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small),
        ) {
            PlayroomSecondaryButton(text = "취소", onClick = onDismiss, modifier = Modifier.weight(1f))
            PlayroomPrimaryButton(text = "적용", onClick = onApply, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun <T> OptionGroup(
    title: String,
    options: List<T>,
    selected: T,
    labelOf: (T) -> String,
    onSelect: (T) -> Unit,
) {
    val palette = PlayroomTheme.palette
    Column(modifier = Modifier.padding(bottom = PlayroomSpacing.Medium)) {
        Text(text = title, style = PlayroomType.CardTitle, color = palette.textPrimary)
        Column(
            modifier = Modifier.padding(top = PlayroomSpacing.Small),
            verticalArrangement = Arrangement.spacedBy(PlayroomSpacing.ExtraSmall),
        ) {
            options.chunked(2).forEach { rowOptions ->
                Row(horizontalArrangement = Arrangement.spacedBy(PlayroomSpacing.Small)) {
                    rowOptions.forEach { option ->
                        com.playroom.core.designsystem.component.PlayroomFilterChip(
                            label = labelOf(option),
                            selected = option == selected,
                            onClick = { onSelect(option) },
                        )
                    }
                }
            }
        }
    }
}
