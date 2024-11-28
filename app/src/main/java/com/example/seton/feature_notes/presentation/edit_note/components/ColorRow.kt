package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seton.common.presentation.state.ContainerColor

@Composable
fun ColorsRow(
    containerColor: ContainerColor,
    onClick: (ContainerColor) -> Unit,
) {
    val colors = enumValues<ContainerColor>().toList()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = colors,
        ) { color ->
            ColorButton(
                isSelected = color == containerColor,
                lightColor = color.lightVariant,
                darkColor = color.darkVariant
            ) {
                onClick(color)
            }
        }
    }
}

